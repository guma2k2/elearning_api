package com.backend.elearning.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    private static MessageSourceAccessor accessor;

    public MessageUtil(MessageSource messageSource) {
        MessageUtil.accessor = new MessageSourceAccessor(messageSource);
    }

    public static String getMessage(String code, Object... args) {
        return accessor.getMessage(code, args, code);
    }
}