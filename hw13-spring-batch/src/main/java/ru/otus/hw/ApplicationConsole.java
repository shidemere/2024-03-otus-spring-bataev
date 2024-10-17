package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent("Application console control")
@RequiredArgsConstructor
public class ApplicationConsole {

    private final JobLauncher jobLauncher;
    private final Job importAuthorJob;
    private final Job importGenreJob;
    private final Job importBookJob;
    private final Job importCommentJob;
    private final Job clearDatabaseJob;

    private void showAvailableCommands() {
        System.out.println("Доступные команды:");
        System.out.println("1. sm-jl - Запуск задачи миграции данных (авторы, жанры, книги, комментарии)");
        System.out.println("2. clear - Очистка базы данных H2 после миграции (для повторного прогона)");
        System.out.println("Введите команду:");
    }

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution authors = jobLauncher.run(importAuthorJob, new JobParametersBuilder().toJobParameters());
        JobExecution genres = jobLauncher.run(importGenreJob, new JobParametersBuilder().toJobParameters());
        JobExecution books = jobLauncher.run(importBookJob, new JobParametersBuilder().toJobParameters());
        JobExecution comments = jobLauncher.run(importCommentJob, new JobParametersBuilder().toJobParameters());

        System.out.println(authors);
        System.out.println(genres);
        System.out.println(books);
        System.out.println(comments);
        showAvailableCommands();
    }

    @ShellMethod(value = "clearH2DatabaseAfterMigration", key = "clear")
    public void clearH2DatabaseAfterMigration() throws Exception {
        jobLauncher.run(clearDatabaseJob, new JobParametersBuilder().toJobParameters());
    }
}
