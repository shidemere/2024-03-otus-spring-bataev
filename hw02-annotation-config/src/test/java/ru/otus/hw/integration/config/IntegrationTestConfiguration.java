package ru.otus.hw.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;

@Configuration
@PropertySource("classpath:application.properties")
public class IntegrationTestConfiguration {
    @Bean
    public String hello() {
        return "Hello";
    }


    @Bean
    public TestFileNameProvider testFileNameProvider() {
        return new AppProperties();
    }

    @Bean
    public QuestionDao questionDao(TestFileNameProvider testFileNameProvider) {
        return new CsvQuestionDao(testFileNameProvider);
    }
}
