package ua.edu.ratos;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;


/**
 * Listener to execute:
 * 1) DB migration before each test method
 * 2) Clean DB after each test method
 * Overhead is compensated by the guarantee of executing each test in a fresh db;
 */
@Order(0)
public class DatabaseTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestMethod(@NotNull TestContext testContext) {
        Flyway flyway = testContext.getApplicationContext().getBean(Flyway.class);
        flyway.migrate();
    }

    @Override
    public void afterTestMethod(@NotNull TestContext testContext) {
        Flyway flyway = testContext.getApplicationContext().getBean(Flyway.class);
        flyway.clean();
        System.out.printf("Completed test class = %s, method = %s%n",
                testContext.getTestClass(), testContext.getTestMethod());
    }
}
