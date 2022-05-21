package com.example.demo.Domain.AlertService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;


@Controller
public class NotificationController {

    private final AlertService handler;

    @Autowired
    public NotificationController(AlertService handler) {
        this.handler = handler;
    }
    // when the client sends a message to “/ws/start” then the start() method is invoked.
    @MessageMapping("/start/{uuid}")
    public void start(StompHeaderAccessor stompHeaderAccessor, @DestinationVariable String uuid) {
        handler.addListener(UUID.fromString(uuid),stompHeaderAccessor.getSessionId());
    }
    //when the client sends a message to “/ws/stop” then the stop() method is invoked.
    @MessageMapping("/stop/{uuid}")
    public void stop(StompHeaderAccessor stompHeaderAccessor, @DestinationVariable String uuid) {
        handler.removeListener(UUID.fromString(uuid), stompHeaderAccessor.getSessionId());
    }

}
