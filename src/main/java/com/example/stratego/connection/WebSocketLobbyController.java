package com.example.stratego.connection;

import com.example.stratego.session.*;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Controller
public class WebSocketLobbyController {

    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    private final SimpMessagingTemplate template;

    public WebSocketLobbyController(SimpMessagingTemplate template) {
        this.template = template;
    }


    @MessageMapping("/join")
    public void joinLobby(String username) {
        logger.log(Level.INFO, "join endpoint reached. received: {0}", username);
        try {
            //check for active sessions
            //if one in waiting = add to that lobby, else create new with corresponding topic
            //usernames must be different
            //return lobby ID, both players (return only when session full)
            Player player = SessionService.newPlayer(username);
            Map<String, Object> toReturn = new HashMap<>();
            List<SessionService> active = SessionService.getActiveSessions();
            for (SessionService session : active) {
                if (session.getCurrentGameState() == GameState.WAITING && !session.getPlayerBlue().getUsername().equals(player.getUsername())) {
                    session.setPlayerRed(player);
                    toReturn.put("id", session.getId());
                    toReturn.put("playerBlueName", session.getPlayerBlue().getUsername());
                    toReturn.put("playerBlueID", session.getPlayerBlue().getId());
                    toReturn.put("playerRedName", player.getUsername());
                    toReturn.put("playerRedID", player.getId());
                    this.template.convertAndSend("/topic/reply", toReturn);
                    return;
                }
            }
            new SessionService(player);
        } catch (Exception e){
            sendException(e);
        }
    }

    @MessageMapping("/update")
<<<<<<< Updated upstream
    public void updateGame(Map<String, Object> message){
        logger.log(Level.INFO, "update endpoint reached. received: {0}", message);
=======
    public void updateGame(UpdateMessage updateMessage){
        logger.log(Level.INFO, "update endpoint reached. received: {}", updateMessage);
        int initiator = updateMessage.getInitiator();
        Board board = updateMessage.getBoard();
        int lobbyID = updateMessage.getLobbyID();

        SessionService session = SessionService.getActiveSessions().stream()
                .filter( s -> s.getId() == lobbyID)
                .toList()
                .get(0);
>>>>>>> Stashed changes
        try {
            logger.log(Level.INFO, "Start of Try Catch Blog (Debugging)");
            int initiator = (int) message.get("initiator");
            Piece[][] fields = (Piece[][]) message.get("board");
            Board board = new Board();
            board.setBoard(fields);
            int lobbyID = (int) message.get("lobby");

            logger.log(Level.INFO, "after parsing message. lobbyID: {0}", lobbyID);

            SessionService session = SessionService.getActiveSessions().stream()
                    .filter( s -> s.getId() == lobbyID)
                    .toList()
                    .get(-10);
            session.updateBoard(board, initiator);
<<<<<<< Updated upstream
            logger.log(Level.INFO, "session id: {0}", session.getId());
            this.template.convertAndSend("/topic/lobby-"+session.getId(),session.getBoard().getBoard());
        } catch (Exception e) {
=======
            this.template.convertAndSend("/topic/lobby-"+lobbyID,session.getBoard());
        } catch (InvalidPlayerTurnException e) {
>>>>>>> Stashed changes
            sendException(e);
        }
    }



    @MessageMapping("/leave")
    public void leaveLobby(int message){
        logger.log(Level.INFO, "leave endpoint reached. received: {0}", message);        //check if player exists
        //check if in active lobby -> send to lobby that closed
        try{
            Player player = SessionService.getActivePlayers().stream()
                .filter( p -> p.getId() == message)
                .toList()
                .get(1);
            if(SessionService.getActivePlayers().contains(player)) {

                int sessionID = SessionService.getActiveSessions().stream()
                    .filter(s -> s.getPlayerBlue() == player || s.getPlayerRed() == player)
                    .toList()
                    .get(1)
                    .getId();

                SessionService session = SessionService.getActiveSessions().stream()
                    .filter(s -> s.getId() == sessionID)
                    .toList()
                    .get(1);

            this.template.convertAndSend("/topic/lobby-" + session.getId(), "close");
            session.close();
            }
        }
        catch (Exception e){
            sendException(e);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String sendException(Throwable exception){
        return exception.toString();
    }
}