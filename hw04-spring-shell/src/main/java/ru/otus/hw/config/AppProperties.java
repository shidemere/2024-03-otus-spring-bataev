package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    /**
     * Ручная установка локали после поиска нужно по строковому коду.
     */
    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    /**
     * Получение имени файлика с вопросами через считывание мапы.
     * Мапа заполнена значениями, считанными из application.yml -> код_локали : название_файлика
     */
    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }
}
