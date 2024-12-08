package com.backend.elearning.domain.chat;

import java.time.LocalDateTime;

public class Message {
    private String sender;
    private String content;

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
