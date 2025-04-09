package com.backend.elearning.domain.chat;

import com.backend.elearning.domain.questionLecture.AnswerLecture;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @MessageMapping("/chat/{questionId}") // Match room-specific endpoints
    public void sendMessage(@DestinationVariable String questionId, AnswerLecture message) {
        // Broadcast message to room-specific topic
        messagingTemplate.convertAndSend("/topic/questions/" + questionId, message);
    }


}
