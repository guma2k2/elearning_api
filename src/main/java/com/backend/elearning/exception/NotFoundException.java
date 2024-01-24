package com.backend.elearning.exception;

import com.backend.elearning.utils.MessageUtil;

public class NotFoundException extends RuntimeException{
    private String message;
    public NotFoundException(String errorCode, Object... var2) {
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
