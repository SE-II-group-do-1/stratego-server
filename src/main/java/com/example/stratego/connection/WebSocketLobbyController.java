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

@Controller
public class WebSocketLobbyController {

    private final SimpMessagingTemplate template;

    public WebSocketLobbyController(SimpMessagingTemplate template) {
        this.template = template;
    }


    @MessageMapping("/join")
    public void joinLobby(String username) {
        //check for active sessions
        //if one in waiting = add to that lobby, else create new with corresponding topic
        //usernames must be different
        //return lobby ID, both players (return only when session full)
        Player player = SessionService.newPlayer(username);
        Map<String, Object> toReturn = new HashMap<>();
        List<SessionService> active = SessionService.getActiveSessions();
        for(SessionService session: active){
            if(session.getCurrentGameState() == GameState.WAITING && !session.getPlayerBlue().getUsername().equals(player.getUsername())){
                session.setPlayerRed(player);
                toReturn.put("id", session.getId());
                toReturn.put("playerBlueName", session.getPlayerBlue().getUsername());
                toReturn.put("playerBlueID", session.getPlayerBlue().getId());
                toReturn.put("playerRedName", player.getUsername());
                toReturn.put("playerRedID", player.getId());
                this.template.convertAndSend("/topic/reply", toReturn);
            }
        }
        new SessionService(player);
    }

    //TODO: remove SETUP from GameState ENUM (Setup is either already saved or random generated client side)

    @MessageMapping("/update")
    public void updateGame(Map<String, Object> message){
        int initiator = (int) message.get("initiator");
        Board board = (Board) message.get("board");

        SessionService session = SessionService.getActiveSessions().stream()
                .filter( s -> s.getPlayerBlue().getId() == initiator || s.getPlayerRed().getId() == initiator)
                .toList()
                .get(1);
        try {
            session.updateBoard(board); //TODO: updateBoard to take Board
            this.template.convertAndSend("/topic/lobby-"+session.getId(),session.getBoard());
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
}