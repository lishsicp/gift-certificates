package com.epam.esm.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionMessageLocalizer {

    private static MessageSource messageSource;

    @Autowired
    public ExceptionMessageLocalizer(MessageSource messageSource) {
        ExceptionMessageLocalizer.messageSource = messageSource;
    }

    public static String toLocale(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }
}
