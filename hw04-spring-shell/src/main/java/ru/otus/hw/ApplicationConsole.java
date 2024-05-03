package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent("Application console control")
@RequiredArgsConstructor
public class ApplicationConsole {

    private final TestRunnerService testRunnerService;

    private final LoginContext loginContext;

    @ShellMethod(value = "Examine command", key = {"e", "examine"})
    @ShellMethodAvailability(value = "userHasBeenIdentified")
    public void examine() {
        testRunnerService.run();
    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        loginContext.login(userName);
        return String.format("Добро пожаловать: %s", userName);
    }


    private Availability userHasBeenIdentified() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Сначала залогиньтесь");
    }
}
