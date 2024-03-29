package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Нужен только для вывода.
 */
public class StreamsIOService implements IOService {
    private final PrintStream printStream;

    public StreamsIOService(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public void requireUserInfo(String name) {
        System.out.print(name);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        printStream.printf("Hello,%s!\n", input);
    }

    @Override
    public void prettyOutput(List<Question> list) {
        for (Question question : list) {
            if (!question.answers().isEmpty()) {
                System.out.println("Question: " + question.text());
                System.out.println("\t " + question.answers().get(0));
                System.out.println("\t " + question.answers().get(1));
            }
        }
    }
}
