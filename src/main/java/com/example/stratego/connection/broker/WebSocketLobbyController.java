package com.example.stratego.connection.broker;

import com.example.stratego.session.GameState;
import com.example.stratego.session.Piece;
import com.example.stratego.session.SessionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.example.stratego.session.Player;

import java.util.List;
import java.util.ArrayList;

@Controller
public class WebSocketLobbyController {

    private List<Player> waitingPlayers = new ArrayList<>();

    @MessageMapping("/join")
    @SendTo("/topic/lobby")
    public int joinLobby(Player player) {
        waitingPlayers.add(player);
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

    @MessageMapping("/leave")
    public void leaveLobby(Player player){
        //check if player exists
        //check if in active lobby -> send to lobby that closed
    }

    @MessageMapping("/ready")
    public void playerReady(Player player){}

    @MessageMapping("/update")
    @SendTo("7topic/")
    public void updateGame(Player player, Piece piece, int newY, int newX){}

    public String createTopic(int id){
        return String.valueOf(id);
    }

    public String getTopic(){
        return "";
    }
}


