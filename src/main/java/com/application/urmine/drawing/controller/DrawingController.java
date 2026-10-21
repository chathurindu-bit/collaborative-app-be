package com.application.urmine.drawing.controller;

import com.application.urmine.drawing.dto.DrawingEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class DrawingController {

    private final SimpMessagingTemplate messagingTemplate;

    public DrawingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/draw")
    public void draw(DrawingEvent event) {
        messagingTemplate.convertAndSend("/topic/drawing", event);
    }
}
