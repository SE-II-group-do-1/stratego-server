package com.example.stratego.connection;

import com.example.stratego.session.*;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Controller
public class WebSocketLobbyController {

    private final SimpMessagingTemplate template;

    public WebSocketLobbyController(SimpMessagingTemplate template) {
        this.template = template;
    }


    @MessageMapping("/join")
    @SendToUser("/topic/reply")
    public Map<String, Object> joinLobby(String username) {
        //check for active sessions
        //if one in waiting = add to that lobby, else create new with corresponding topic
        //return lobby ID, assigned color, player info
        Player player = SessionService.newPlayer(username);
        Map<String, Object> toReturn = new HashMap<>();
        List<SessionService> active = SessionService.getActiveSessions();
        for(SessionService session: active){
            if(session.getCurrentGameState() == GameState.WAITING){
                session.setPlayerRed(player);
                toReturn.put("id", session.getId());
                toReturn.put("color", Color.RED);
                toReturn.put("user", player);
                return toReturn;
            }
        }
        SessionService newSession = new SessionService(player);
        toReturn.put("id", newSession.getId());
        toReturn.put("color", Color.BLUE);
        return toReturn;
    }

    @MessageMapping("/setup")
    public void setBoard(Map<String, Object> message){
        Player sender = (Player) message.get("player");
        Board board = (Board) message.get("board");

        SessionService session = SessionService.getActiveSessions().stream()
                .filter( s -> s.getPlayerBlue() == sender || s.getPlayerRed() == sender)
                .toList()
                .get(1);
        //checking if red/blue client side problem lol
        session.getBoard().setBoard(board);
        Map<String, Object> toReturn = Map.of(
            "player", sender,
            "board", session.getBoard()
        );
        this.template.convertAndSend("/topic/setup-"+session.getId(), toReturn);
    }

    @MessageMapping("/update")
    public void updateGame(Map<String, Object> message){
        int y = (int) message.get("y");
        int x = (int) message.get("x");
        Piece piece = (Piece) message.get("piece");
        Player initiator = (Player) message.get("initiator");

        SessionService session = SessionService.getActiveSessions().stream()
                .filter( s -> s.getPlayerBlue() == initiator || s.getPlayerRed() == initiator)
                .toList()
                .get(1);
        try {
            session.updateBoard(y,x,piece,initiator);
            this.template.convertAndSend("/topic/lobby-"+session.getId(),updateToObject(y,x,piece));
        } catch (InvalidPlayerTurnException e) {
            sendException(e);
        }
    }



    @MessageMapping("/leave")
    public void leaveLobby(Map<String, Object> message){
        //check if player exists
        //check if in active lobby -> send to lobby that closed
        int sessionID = (int) message.get("id");
        Player player = (Player) message.get("player");
        if(SessionService.getActivePlayers().contains(player)){
            SessionService session = SessionService.getActiveSessions().stream()
                    .filter( s -> s.getId() == sessionID)
                    .toList()
                    .get(1);
            this.template.convertAndSend("/topic/lobby-"+session.getId(), "close");
            session.close();
            SessionService.removePlayer(player);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String sendException(Throwable exception){
        return exception.toString();
    }

    private Map<String, Object> updateToObject(int y, int x, Piece piece){
        return Map.ofEntries(
            entry("y", y),
            entry("x", x),
            entry("piece", piece)
        );
    }

}


