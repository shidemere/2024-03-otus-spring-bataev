package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.CsvQuestionDao;

class TestServiceImplTest {
    /**
     * Через аннотацию @Mock почему-то не работает...
     */
    private StreamsIOService ioService;
    private CsvQuestionDao questionDao;
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = Mockito.mock(StreamsIOService.class);
        questionDao = Mockito.mock(CsvQuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTest_verifyWork() {
        Mockito.when(questionDao.findAll()).thenReturn(Mockito.anyList());
        testService.executeTest();
        Mockito.verify(ioService, Mockito.times(1)).prettyOutput(Mockito.anyList());
    }

}