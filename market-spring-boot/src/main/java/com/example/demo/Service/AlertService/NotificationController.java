package com.example.demo.Service.AlertService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
public class NotificationController {

    private final AlertHandler handler;

    @Autowired
    public NotificationController(AlertHandler handler) {
        this.handler = handler;
    }
    // when the client sends a message to “/ws/start” then the start() method is invoked.
    @MessageMapping("/start/{uuid}")
    public void start(StompHeaderAccessor stompHeaderAccessor, @DestinationVariable String uuid) {
        handler.addListener(uuid,stompHeaderAccessor.getSessionId());
    }
    //when the client sends a message to “/ws/stop” then the stop() method is invoked.
    @MessageMapping("/stop/{uuid}")
    public void stop(StompHeaderAccessor stompHeaderAccessor, @DestinationVariable String uuid) {
        handler.removeListener(uuid, stompHeaderAccessor.getSessionId());
    }
}
