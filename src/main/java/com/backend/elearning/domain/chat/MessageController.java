package com.backend.elearning.domain.chat;

import com.backend.elearning.domain.questionLecture.AnswerLecture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @MessageMapping("/chat/{questionId}")
    public void sendMessage(@DestinationVariable String questionId, AnswerLecture message) {
        log.info("received questionId: {}, answer content: {}", questionId, message);
        messagingTemplate.convertAndSend("/topic/questions/" + questionId, message);
    }


}
