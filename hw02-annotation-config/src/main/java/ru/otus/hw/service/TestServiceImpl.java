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
            ioService.printFormattedLine("Вопрос #%d: %s", questions.indexOf(question) + 1, question.text());
            var poorAnswers = getPoorAnswers(question);
            var correctAnswer = getCorrectAnswer(question);

            poorAnswers
                    .forEach(answer -> ioService.printFormattedLine(
                            "Вариант ответа #%d: %s", poorAnswers.indexOf(answer) + 1, answer)
                    );

            var errorMsg = String.format(" Введённое число должно быть от 1 (включительно) до %d",
                    question.answers().size()) + 1;

            var givenAnswer = ioService.readIntForRangeWithPrompt(1, 3, "Введите порядковый номер ответа: ", errorMsg);

            var isAnswerValid = question.answers().indexOf(correctAnswer.orElseThrow()) + 1;
            testResult.applyAnswer(question, givenAnswer == isAnswerValid);
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
