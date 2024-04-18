package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final LocalizedMessagesService localizedMessagesServiceImpl;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question : questions) {
            var poorAnswers = getPoorAnswers(question);
            var correctAnswer = getCorrectAnswer(question);

            ioService.printFormattedLineLocalized(
                    "TestService.count.the.question", questions.indexOf(question) + 1, question.text()
            );
            poorAnswers.forEach(answer -> ioService.printFormattedLineLocalized(
                    "TestService.count.the.answer", poorAnswers.indexOf(answer) + 1, answer.text())
            );

            var answer = getUserAnswer(poorAnswers);
            var isRightAnswer = answer.equals(correctAnswer.orElseThrow());

            testResult.applyAnswer(question, isRightAnswer);
        }
        return testResult;
    }

    private Answer getUserAnswer(List<Answer> answers) {

        int maximalAnswerNumber = answers.size() + 1;
        // todo Я не понимаю как это пофиксить. Попроси помощи.
        /**
         * Суть проблемы:
         * Нет понимания как можно каким нибудь String.format подставить количество ответов ко значению,
         *      которое будет подтягиваться из bundle.
         * "The number entered must be from 1 (inclusive) to {0}" - как вместо {0} подставить maximalAnswerNumber в
         *      String.format или каком нибудь аналоге?
         */
//        String errorMsg = localizedMessagesServiceImpl.getMessage("TestService.error.message", maximalAnswerNumber);

        String errorMsgFromResource = "TestService.error.message";

        int givenAnswer = ioService.readIntForRangeWithPromptLocalized(
                1, maximalAnswerNumber, "TestService.sequential.number.of.the.answer", errorMsgFromResource
        );

        return answers.get(givenAnswer - 1);
    }

    private List<Answer> getPoorAnswers(Question question) {
        return question.answers()
                .stream()
                .toList();
    }

    private Optional<Answer> getCorrectAnswer(Question question) {
        return question.answers().stream()
                .filter(Answer::isCorrect)
                .findFirst();
    }

}
