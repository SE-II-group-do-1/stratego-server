package com.example.stratego.connection;

import com.example.stratego.session.*;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.logging.Level;

@Controller
public class WebSocketLobbyController {

    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

    private final SimpMessagingTemplate template;

    public WebSocketLobbyController(SimpMessagingTemplate template) {
        this.template = template;
    }

    private static final String LOBBY = "/topic/lobby-";

    @MessageMapping("/join")
    public void joinLobby(String username) {
        logger.log(Level.INFO, "join endpoint reached. received: {0}", username);
        try {

            Player player = SessionService.newPlayer(username);
            LobbyMessage response = new LobbyMessage();
            //only send response (to both players) if lobby is full
            if(SessionService.assignToSession(player)){
                SessionService session = SessionService.getSessionByPlayer(player);
                response.setBlue(session.getPlayerBlue());
                response.setRed(session.getPlayerRed());
                response.setLobbyID(session.getId());
                this.template.convertAndSend("/topic/reply", response);
            }

        } catch (Exception e) {
            sendException(e);
        }
    }

    @MessageMapping("/update")
    public void updateGame(UpdateMessage updateMessage) {
        logger.log(Level.INFO, "update endpoint reached. received: {}", updateMessage);
        try {
            int initiator = updateMessage.getInitiator();
            Board board = updateMessage.getBoard();
            int lobbyID = updateMessage.getLobbyID();
            boolean cheat = updateMessage.getCheat();
            boolean check = updateMessage.getCheck();

            SessionService session = SessionService.getSessionByID(lobbyID);
            session.setCheat(initiator, cheat);
            session.checkCheat(check, initiator);
            session.updateBoard(board, initiator);

            UpdateMessage update = new UpdateMessage();
            update.setBoard(session.getBoard());
            update.setWinner(session.getWinner());
            this.template.convertAndSend(LOBBY + lobbyID, update);
        } catch (InvalidPlayerTurnException e) {
            sendException(e);
        }
    }

    @MessageMapping("/setup")
    public void setupGame(UpdateMessage updateMessage) {
        try {
            int initiator = updateMessage.getInitiator();
            Board board = updateMessage.getBoard();
            int lobbyID = updateMessage.getLobbyID();

            SessionService session = SessionService.getSessionByID(lobbyID);
            boolean bothPlayersSet = session.setPlayerBoard(initiator, board);
            if(bothPlayersSet){
                UpdateMessage update = new UpdateMessage();
                update.setBoard(session.getBoard());
                this.template.convertAndSend(LOBBY + lobbyID, update);
            }

        } catch (Exception e) {
            sendException(e);
        }
    }


    @MessageMapping("/leave")
    public void leaveLobby(int message) {
        logger.log(Level.INFO, "leave endpoint reached. received: {0}", message);        //check if player exists
        //check if in active lobby -> send to lobby that closed
        try {
            Player player = SessionService.getPlayerByID(message);
            if (SessionService.getActivePlayers().contains(player)) {

                SessionService session = SessionService.getSessionByPlayer(player);
                UpdateMessage u = new UpdateMessage();
                u.setClose(true);

                this.template.convertAndSend(LOBBY + session.getId(), u);
                session.close();
            }
        } catch (Exception e) {
            sendException(e);
        }
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String sendException(Throwable exception) {
        return exception.toString();
    }
}