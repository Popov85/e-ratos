package ua.edu.ratos;

import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(
        listeners = {
                //Custom listener to migrate/clean DB
                DatabaseTestExecutionListener.class
        },
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public abstract class BaseIT {
}