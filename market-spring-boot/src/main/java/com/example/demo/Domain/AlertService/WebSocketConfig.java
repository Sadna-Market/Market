package com.example.demo.Domain.AlertService;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * Our client app will read messages from our broker by using “/notification“
     * It will address its messages to the server with “/ws“
     * It will use the STOMP protocol to interact with the “/notifications” end point.
     * Furthermore, it enables SockJS to fall back on other methods if WebSocket is not supported.
     */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/notification");
        registry.setApplicationDestinationPrefixes("/ws");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/notifications")
                .setAllowedOrigins("http://localhost:8090", "http://127.0.0.1:8090")
                .withSockJS();
    }


    @Bean
    public TomcatServletWebServerFactory tomcatContainerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();;
        factory.setTomcatContextCustomizers(Collections.singletonList(tomcatContextCustomizer()));
        return factory;
    }

    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addServletContainerInitializer(new WsSci(), null);
            }
        };
    }
}
