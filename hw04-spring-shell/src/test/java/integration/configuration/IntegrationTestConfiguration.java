package integration.configuration;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.service.*;

import java.util.Locale;

@Configuration
@PropertySource("classpath:application.yml")
public class IntegrationTestConfiguration {

    @MockBean
    AppProperties appProperties;

    @MockBean
    LocalizedMessagesService localizedMessagesService;

    @MockBean
    StreamsIOService streamsIOService;

    @MockBean
    LocalizedIOService localizedIOService;

    @Bean
    public QuestionDao questionDao() {
        Mockito.when(appProperties.getLocale()).thenReturn(Locale.forLanguageTag("en-US"));
        Mockito.when(appProperties.getRightAnswersCountToPass()).thenReturn(3);
        Mockito.when(appProperties.getTestFileName()).thenReturn("questions.csv");
        return new CsvQuestionDao(appProperties);
    }

    @Bean
    public LocalizedIOService ioService() {
        return new LocalizedIOServiceImpl(localizedMessagesService, streamsIOService);
    }

    @Bean
    public TestServiceImpl testServiceImplTest(QuestionDao questionDao) {
        Mockito.when(localizedIOService.readIntForRangeWithPromptLocalized(
                Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn(1);
        return new TestServiceImpl(localizedIOService, questionDao);
    }
}
