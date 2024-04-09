package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.io.InputStream;
import java.util.List;

class CsvQuestionDaoTest {
    private TestFileNameProvider fileNameProvider;

    private QuestionDao questionDao;

    @BeforeEach
    void setUp(){
        fileNameProvider = Mockito.mock(AppProperties.class);
        questionDao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    void findAll_correctWork(){
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("test.csv");
        Assertions.assertNotNull(resourceAsStream);

        Mockito.when(fileNameProvider.getTestFileName()).thenReturn("test.csv");

        List<Question> all = questionDao.findAll();

        Assertions.assertEquals(3, all.size());
    }
}