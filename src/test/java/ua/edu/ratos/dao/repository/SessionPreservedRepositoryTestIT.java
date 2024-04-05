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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class SessionPreservedRepositoryTestIT {

    @Autowired
    private SessionPreservedRepository sessionPreservedRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/preserved_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countByUserIdTest() {
        assertThat("SessionPreserved quantity is not = 5", sessionPreservedRepository.countByUserId(3L), equalTo(5L));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/preserved_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByUserIdTest() {
        assertThat("Slice of SessionPreserved is not of size = 5",
                sessionPreservedRepository.findAllByUserId(2L, PageRequest.of(0, 5)).getContent(), hasSize(5));
    }
}
