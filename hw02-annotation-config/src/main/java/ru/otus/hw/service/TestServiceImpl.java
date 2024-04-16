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
        for (var question : questions) {
            var poorAnswers = getPoorAnswers(question);
            var correctAnswer = getCorrectAnswer(question);

            ioService.printFormattedLine("Вопрос #%d: %s", questions.indexOf(question) + 1, question.text());
            poorAnswers.forEach(answer -> ioService.printFormattedLine(
                    "Вариант ответа #%d: %s", poorAnswers.indexOf(answer) + 1, answer.text())
            );

            var answer = getUserAnswer(poorAnswers);
            var isRightAnswer = answer.equals(correctAnswer.orElseThrow());

            testResult.applyAnswer(question, isRightAnswer);
        }
        return testResult;
    }

    private Answer getUserAnswer(List<Answer> answers) {

        int maximalAnswerNumber = answers.size() + 1;
        var errorMsg = String.format(" Введённое число должно быть от 1 (включительно) до %d", maximalAnswerNumber);

        int givenAnswer = ioService.readIntForRangeWithPrompt(
                1, maximalAnswerNumber, "Введите порядковый номер ответа: ", errorMsg
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
