package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;


    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        int questionCounter = 1;
        for (var question : questions) {
            var poorAnswers = question.answers();
            ioService.printFormattedLineLocalized("TestService.count.the.question", questionCounter++, question.text());

            for (int i = 0; i < poorAnswers.size(); i++) {
                ioService.printFormattedLineLocalized("TestService.count.the.answer", i + 1, poorAnswers.get(i).text());
            }

            var answer = getUserAnswer(poorAnswers);
            var isRightAnswer = answer.isCorrect();

            testResult.applyAnswer(question, isRightAnswer);
        }
        return testResult;
    }

    private Answer getUserAnswer(List<Answer> answers) {

        int maximalAnswerNumber = answers.size() + 1;


        String errorMsgFromResource = "TestService.error.message";

        int givenAnswer = ioService.readIntForRangeWithPromptLocalized(
                1, maximalAnswerNumber, "TestService.sequential.number.of.the.answer", errorMsgFromResource
        );

        return answers.get(givenAnswer - 1);
    }

}
