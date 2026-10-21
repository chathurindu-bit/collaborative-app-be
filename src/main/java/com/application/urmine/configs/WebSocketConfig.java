package com.application.urmine.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * A stroke is sent as a single STOMP frame. A long freehand
 * drag collects thousands of points and the resulting JSON is
 * far larger than the 8KB text frame Tomcat accepts by default.
 *
 * Tomcat answers an oversized frame by closing the socket,
 * which drops the STOMP session and loses the whole stroke.
 * These limits are raised so a large stroke can no longer
 * break the connection.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Largest single STOMP frame accepted, in bytes.
     */
    private static final int MESSAGE_SIZE_LIMIT = 256 * 1024;

    /**
     * Underlying container text frame limit. Must stay above
     * MESSAGE_SIZE_LIMIT or the container rejects first.
     */
    private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE = 512 * 1024;

    private static final int MAX_BINARY_MESSAGE_BUFFER_SIZE = 512 * 1024;

    private static final int SEND_BUFFER_SIZE_LIMIT = 512 * 1024;

    private static final int SEND_TIME_LIMIT = 20_000;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic");

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/drawing")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

        registry.setMessageSizeLimit(MESSAGE_SIZE_LIMIT);

        registry.setSendBufferSizeLimit(SEND_BUFFER_SIZE_LIMIT);

        registry.setSendTimeLimit(SEND_TIME_LIMIT);
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {

        ServletServerContainerFactoryBean container =
                new ServletServerContainerFactoryBean();

        container.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_BUFFER_SIZE);

        container.setMaxBinaryMessageBufferSize(MAX_BINARY_MESSAGE_BUFFER_SIZE);

        return container;
    }

}
