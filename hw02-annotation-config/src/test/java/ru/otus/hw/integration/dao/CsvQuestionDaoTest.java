package ru.otus.hw.integration.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.integration.config.IntegrationTestConfiguration;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
class CsvQuestionDaoTest {

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