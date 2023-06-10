package com.epam.esm.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ExceptionMessageI18n {

    private final MessageSource messageSource;

    public String toLocale(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }
}
