package com.example.demo.Domain.AlertService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;


@Controller
public class NotificationController {
    private static final Logger logger = Logger.getLogger(NotificationController.class);
    private final AlertService handler;

    @Autowired
    public NotificationController(AlertService handler) {
        this.handler = handler;
    }
    // when the client sends a message to “/ws/start” then the start() method is invoked.
    @MessageMapping("/start/{uuid}")
    public void start(StompHeaderAccessor stompHeaderAccessor, @DestinationVariable("uuid") String uuid) {
        logger.debug(String.format("got start session with uuid: %s",uuid));
        handler.addListener(UUID.fromString(uuid),stompHeaderAccessor.getSessionId());
    }
    //when the client sends a message to “/ws/stop” then the stop() method is invoked.
    @MessageMapping("/stop/{uuid}")
    public void stop(StompHeaderAccessor stompHeaderAccessor, @DestinationVariable("uuid") String uuid) {
        logger.debug(String.format("got stop session with uuid: %s",uuid));
        handler.removeListener(UUID.fromString(uuid), stompHeaderAccessor.getSessionId());
    }

}
