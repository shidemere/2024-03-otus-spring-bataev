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
    private TestConfig testConfig;

    private IOService ioService;
    private ResultService resultService;

    @BeforeEach
    void setUp() {
        testConfig = Mockito.mock(AppProperties.class);
        ioService = Mockito.mock(StreamsIOService.class);
        resultService = new ResultServiceImpl(testConfig, ioService);
    }

    @Test
    void showResult_verifyIoServiceIsCalling() {
        Student student = new Student("Salavat", "Bataev");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(4);

        resultService.showResult(testResult);

        Mockito.verify(ioService, Mockito.times(3)).printFormattedLine(Mockito.anyString(), Mockito.any());
    }
}