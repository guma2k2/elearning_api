package com.backend.elearning.domain.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@Slf4j
public class CommentController {

    @MessageMapping("/chat/{eventId}/add")
    @SendTo("/topic/event/{eventId}")
    public Comment addUser(
            @Payload Comment chatMessage,
            SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable("eventId") Long eventId
    ) {
        // Add username in web socket session
//        log.info(chatMessage.getSender() + " joined");
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("eventId", eventId);
        return chatMessage;
    }
}
