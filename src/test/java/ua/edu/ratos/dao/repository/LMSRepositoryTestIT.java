package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.lms.LMS;
import ua.edu.ratos.dao.repository.lms.LMSRepository;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class LMSRepositoryTestIT {

    @Autowired
    private LMSRepository lmsRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByConsumerKeyTest() {
        Optional<LMS> optional = lmsRepository.findByConsumerKey("ratos_consumer_key_5");
        assertTrue(optional.isPresent(), "LMS was not found for consumer key = ratos_consumer_key_5");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForDropdownByOrgIdTest() {
        Set<LMS> result = lmsRepository.findAllForDropdownByOrgId(1L);
        assertThat("Set of LMS is not of size = 3", result, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForTableByOrgIdTest() {
        Set<LMS> result = lmsRepository.findAllForDropdownByOrgId(2L);
        assertThat("Set of LMS is not of size = 3", result, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllAdminTest() {
        assertThat("Slice of LMS is not of size = 6",
                lmsRepository.findAllAdmin(PageRequest.of(0, 50)).getContent(), hasSize(6));
    }
}
