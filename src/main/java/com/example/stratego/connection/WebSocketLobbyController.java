package com.example.stratego.connection;

import com.example.stratego.session.GameState;
import com.example.stratego.session.Piece;
import com.example.stratego.session.SessionService;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import com.example.stratego.session.Player;

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
    @SendToUser
    public int joinLobby(Player player) {
        //check for active sessions
        //if one in waiting = add to that lobby, else create new with corresponding topic
        //return lobby ID
        List<SessionService> active = SessionService.getActiveSessions();
        for(SessionService session: active){
            if(session.getCurrentGameState() == GameState.WAITING){
                session.setPlayerRed(player);
                return session.getId();
            }
        }
        SessionService newSession = new SessionService(player);
        return newSession.getId();
    }

    // has to recieve a Map with data in
    @MessageMapping("/update")
    public void updateGame(int y, int x, Piece piece, Player initiator){
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


    //has to recieve Map

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

