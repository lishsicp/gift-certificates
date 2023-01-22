package com.epam.esm.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionMessageI18n {

    private static MessageSource messageSource;

    @Autowired
    public ExceptionMessageI18n(MessageSource messageSource) {
        ExceptionMessageI18n.messageSource = messageSource;
    }

    public static String toLocale(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }
}
