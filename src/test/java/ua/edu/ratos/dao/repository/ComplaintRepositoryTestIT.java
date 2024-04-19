package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Complaint;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ComplaintRepositoryTestIT extends BaseIT {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/complaint_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertTimeout(Duration.ofMillis(5000), () -> {
            Page<Complaint> page = complaintRepository.findAllByDepartmentId(1L,
                    PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "timesComplained")));
            assertThat("Page of Complaints is not as expected", page, allOf(
                    hasProperty("content", hasSize(5)),
                    hasProperty("totalPages", equalTo(1)),
                    hasProperty("totalElements", equalTo(5L))));
        });
    }
}
