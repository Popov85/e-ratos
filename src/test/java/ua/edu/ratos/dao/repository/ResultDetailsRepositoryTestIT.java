package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ResultDetailsRepositoryTestIT {

    @Autowired
    private ResultDetailsRepository resultDetailsRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/result_details_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void cleanExpiredTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime of = LocalDateTime.of(2019, 1, 1, 1, 1);
        String formattedDateTimeNow = of.format(formatter);
        resultDetailsRepository.cleanExpired(formattedDateTimeNow);
        assertThat("List of all ResultDetails after cleaning is not of size = 7",
                resultDetailsRepository.count(), equalTo(7L));
    }
}
