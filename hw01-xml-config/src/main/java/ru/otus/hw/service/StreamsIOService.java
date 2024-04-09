package ru.otus.hw.service;

import java.io.PrintStream;
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


}
