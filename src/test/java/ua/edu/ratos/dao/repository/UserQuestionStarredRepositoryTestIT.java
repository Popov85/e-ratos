package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserQuestionStarredRepositoryTestIT {

    @Autowired
    private UserQuestionStarredRepository userQuestionStarredRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/starred_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countByUserIdTest() {
        assertThat("Quantity of starred by a user ID=3 questions is not 5",
                userQuestionStarredRepository.countByUserId(3L), equalTo(5L));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/starred_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByUserIdTest() {
        assertThat("Quantity of starred by a user ID=2 questions is not 5",
                userQuestionStarredRepository.findAllByUserId(2L, PageRequest.of(0, 10)).getContent(), hasSize(5));
    }

}
