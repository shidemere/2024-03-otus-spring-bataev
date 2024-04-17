package integration.dao;

import integration.configuration.IntegrationTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class CsvQuestionDaoTest {
    @Autowired
    private QuestionDao questionDao;

    /**
     * Проверяет: {@link QuestionDao#findAll()}
     * Условие: в ресурсах находится файл с вопросами.
     * Результат: получено 5 вопросов из файла с вопросами.
     */
    @Test
    void findAll_get5question() {
        List<Question> questions = questionDao.findAll();
        Assertions.assertEquals(5, questions.size());
    }
}
