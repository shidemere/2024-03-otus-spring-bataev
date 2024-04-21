package integration.service;

import integration.configuration.IntegrationTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class TestServiceImplTest {
    @Autowired
    QuestionDao questionDao;
    @Autowired
    TestServiceImpl testService;



    @Test
    public void executeTestFor_correctReturn() throws NoSuchMethodException {
        Student student = new Student("Salavat", "Bataev");
        TestResult testResult = testService.executeTestFor(student);
        List<Question> answeredQuestions = testResult.getAnsweredQuestions();
        Assertions.assertEquals(5, answeredQuestions.size());
    }
}
