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

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        var questionNumber = 1;
        for (var question : questions) {
            ioService.printFormattedLine("Вопрос #%d: %s", questionNumber++, question.text());

            var poorAnswer = getPoorAnswers(question);

            var correctAnswer = getCorrectAnswer(question);

            ioService.printFormattedLine("Варианты ответа: %s", poorAnswer);

            var givenAnswer = ioService.readStringWithPrompt("Введите ответ: ");

            var isAnswerValid = correctAnswer.filter(answer -> givenAnswer.equalsIgnoreCase(answer.text())).isPresent();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private List<String> getPoorAnswers(Question question) {
        return question.answers().stream()
                .map(Answer::text)
                .toList();
    }

    private Optional<Answer> getCorrectAnswer(Question question) {
        return question.answers().stream()
                .filter(Answer::isCorrect)
                .findFirst();
    }
}
