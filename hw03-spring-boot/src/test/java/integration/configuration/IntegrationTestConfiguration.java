package integration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;

import java.util.Map;

@Configuration
@PropertySource("classpath:application.yml")
public class IntegrationTestConfiguration {
    @Bean
    public AppProperties testFileNameProvider() {
        AppProperties appProperties = new AppProperties();
        // todo Я не понимаю, как это засетить через Spring.
        // Есть ли какой то инжект в поля бинов для интеграционных тестов?
        appProperties.setLocale("en-US");
        appProperties.setRightAnswersCountToPass(3);
        Map<String, String> localMap = Map.of("ru-RU", "questions_ru.csv", "en-US", "questions.csv");
        appProperties.setFileNameByLocaleTag(localMap);
        return appProperties;
    }

    @Bean
    public QuestionDao questionDao(AppProperties appProperties) {
        return new CsvQuestionDao(appProperties);
    }
}
