package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ResourceRepositoryTestIT extends BaseIT {

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findOneForEditTest() {
        assertTrue(resourceRepository.findOneForEdit(1L).isPresent(), "Resource is not found");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByStaffIdTest() {
        assertThat("Page of Resource is not of size = 2",
                resourceRepository.findByStaffId(1L, PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByDepartmentIdTest() {
        final Page<Resource> resources = resourceRepository.findByDepartmentId(1L, PageRequest.of(0, 50));
        assertEquals(2, resources.getContent().size());
        assertThat("Page of Resource is not of size = 2",
                resourceRepository.findByDepartmentId(1L, PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByStaffIdAndDescriptionLettersContainsTest() {
        assertThat("Page of Resource is not of size = 2",
                resourceRepository.findByStaffIdAndDescriptionLettersContains(2L, "Image", PageRequest.of(0, 20)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByStaffIdAndDescriptionLettersContainsNegativeTest() {
        assertThat("Page of Resource is not empty",
                resourceRepository.findByStaffIdAndDescriptionLettersContains(1L, "Diagram", PageRequest.of(0, 20)).getContent(), empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByDepartmentIdAndDescriptionLettersContainsTest() {
        assertThat("Page of Resource is not of size = 2",
                resourceRepository.findByDepartmentIdAndDescriptionLettersContains(2L, "Image", PageRequest.of(0, 20)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByDepartmentIdAndDescriptionLettersContainsNegativeTest() {
        assertThat("Page of Resource is not empty",
                resourceRepository.findByDepartmentIdAndDescriptionLettersContains(1L, "Diagram", PageRequest.of(0, 20)).getContent(), empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/resource_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of Resource is not of size = 7",
                resourceRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(7));
    }
}
