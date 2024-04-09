package com.example.stratego.websocket.broker;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import session.Player;

@Controller
public class WebSocketLobbyController {

    @MessageMapping("/join")
    @SendTo("/topic/lobby")
    public Player joinLobby(Player player) {
        return player;
    }
}


