package com.example.stratego.connection.broker;

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

    @MessageMapping("/update")
    public void updateGame(int y, int x, Piece piece, Player initiator){
        SessionService session = SessionService.getActiveSessions().stream()
                .filter( s -> s.getPlayerBlue() == initiator || s.getPlayerRed() == initiator)
                .toList()
                .get(1);
        try {
            session.updateBoard(y,x,piece,initiator);
            this.template.convertAndSend("/topic/lobby-"+session.getId(),"");
        } catch (InvalidPlayerTurnException e) {
            sendException(e);
        }
    }



    @MessageMapping("/leave")
    public void leaveLobby(Player player){
        //check if player exists
        //check if in active lobby -> send to lobby that closed
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String sendException(Throwable exception){
        return exception.toString();
    }

}


