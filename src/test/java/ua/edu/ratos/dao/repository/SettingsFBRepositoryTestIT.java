package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SettingsFBRepositoryTestIT extends BaseIT {

    @Autowired
    private SettingsFBRepository settingsFBRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findOneForEditTest() {
        assertTrue(settingsFBRepository.findOneForEdit(1L).isPresent(), "SettingsFB is not found");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByStaffIdTest() {
        assertThat("Slice of SettingsFB is not of size = 7",
                settingsFBRepository.findAllByStaffId(1L, PageRequest.of(0, 50)).getContent(), hasSize(7));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Slice of SettingsFB is not of size = 4",
                settingsFBRepository.findAllByDepartmentId(2L, PageRequest.of(0, 50)).getContent(), hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByStaffIdAndNameLettersContainsTest() {
        assertThat("Slice of SettingsFB is not of size = 3",
                settingsFBRepository.findAllByStaffIdAndNameLettersContains(1L, "default", PageRequest.of(0, 50)).getContent(), hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndNameLettersContainsTest() {
        assertThat("Slice of SettingsFB is not of size = 2",
                settingsFBRepository.findAllByDepartmentIdAndNameLettersContains(1L, "uni", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndNameLettersContainsNegativeOutcomeTest() {
        assertThat("Slice of SettingsFB is not empty",
                settingsFBRepository.findAllByDepartmentIdAndNameLettersContains(2L, "multi", PageRequest.of(0, 50)).getContent(), empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/settings_fb_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllAdminTest() {
        assertThat("Slice of SettingsFB is not of size = 11",
                settingsFBRepository.findAllAdmin(PageRequest.of(0, 50)).getContent(), hasSize(11));
    }

}
