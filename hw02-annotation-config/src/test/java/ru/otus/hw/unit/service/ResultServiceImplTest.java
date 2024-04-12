package ru.otus.hw.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.ResultServiceImpl;
import ru.otus.hw.service.StreamsIOService;

class ResultServiceImplTest {

    private IOService ioService;
    private ResultService resultService;

    @BeforeEach
    void setUp() {
        TestConfig testConfig = Mockito.mock(AppProperties.class);
        ioService = Mockito.mock(StreamsIOService.class);
        resultService = new ResultServiceImpl(testConfig, ioService);
    }

    /**
     * Проверяет: {@link ResultService#showResult(TestResult)}
     * Условие: в параметрах передаётся корректный объект {@link TestResult}
     * Результат: методы для вывода вызовутся, в сумме, 6 раз.
     */
    @Test
    void showResult_verifyIoServiceIsCalling() {
        Student student = new Student("Salavat", "Bataev");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(4);

        resultService.showResult(testResult);

        Mockito.verify(ioService, Mockito.times(3)).printFormattedLine(Mockito.anyString(), Mockito.any());
        Mockito.verify(ioService, Mockito.times(3)).printLine(Mockito.anyString());
    }
}