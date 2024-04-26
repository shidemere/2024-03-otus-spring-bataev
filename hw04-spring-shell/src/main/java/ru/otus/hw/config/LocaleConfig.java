package ru.otus.hw.config;

import java.util.Locale;

/**
 * Предоставляет локаль, считанную из application.yml
 */
public interface LocaleConfig {
    Locale getLocale();
}
