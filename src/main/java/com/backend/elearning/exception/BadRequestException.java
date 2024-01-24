package com.backend.elearning.exception;

import com.backend.elearning.utils.MessageUtil;

public class BadRequestException extends RuntimeException{
    private String message;
    public BadRequestException(String errorCode, Object... var2) {
        this.message = MessageUtil.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
