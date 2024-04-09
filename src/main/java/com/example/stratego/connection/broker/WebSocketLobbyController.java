package com.example.stratego.connection.broker;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import session.Player;

import java.util.List;
import java.util.ArrayList;

@Controller
public class WebSocketLobbyController {

    private List<Player> waitingPlayers = new ArrayList<>();

    @MessageMapping("/join")
    @SendTo("/topic/lobby")
    public List<Player> joinLobby(Player player) {
        waitingPlayers.add(player);
        return waitingPlayers;
    }
}


