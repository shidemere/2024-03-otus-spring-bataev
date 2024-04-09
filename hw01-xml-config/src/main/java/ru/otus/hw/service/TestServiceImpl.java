package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor

public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.requireUserInfo("Enter your name, please: ");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> all = questionDao.findAll();
        prettyOutput(all);
    }

    public void prettyOutput(List<Question> list) {
        int questionCounter = 1;
        for (Question question : list) {
            ioService.printLine("");
            int tabulationForAnswerCounter = 1;
            System.out.printf("Question #%d: %s%s", questionCounter++, question.text(), "\n");
            for (int i = 0; i < question.answers().size(); i++) {
                ioService.printFormattedLine(
                        "%sAnswer #%d: %s",
                        "\t".repeat(tabulationForAnswerCounter),
                        tabulationForAnswerCounter++,
                        question.answers().get(i)
                );

            }
            ioService.printLine("");
        }
    }
}
