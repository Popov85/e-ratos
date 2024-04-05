package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.Group;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class GroupRepositoryTestIT {

    @Autowired
    private GroupRepository groupRepository;

    //-------------------------------------------------ONE for edit-----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForEditTest() {
        Optional<Group> optional = groupRepository.findOneForEdit(1L);
        assertTrue(optional.isPresent(), "Group was not found with groupId = 1L");
    }

    //------------------------------------------------INSTRUCTOR table--------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByStaffIdTest() {
        assertThat("Page of Group is not of size = 4",
                groupRepository.findAllByStaffId(1L, PageRequest.of(0, 50)).getContent(), hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Page of Group is not of size = 4",
                groupRepository.findAllByDepartmentId(2L, PageRequest.of(0, 50)).getContent(), hasSize(4));
    }

    //-----------------------------------------------------Search-------------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByStaffIdAndNameLettersContainsTest() {
        assertThat("Page of Group is not of size = 2",
                groupRepository.findAllByStaffIdAndNameLettersContains(1L, "19/20", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdAndNameLettersContainsTest() {
        assertThat("Page of Group is not of size = 2",
                groupRepository.findAllByDepartmentIdAndNameLettersContains(2L, "1st", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    //-----------------------------------------------DROPDOWN slice-----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForDropDownByStaffIdTest() {
        assertThat("Slice of Group is not of size = 4",
                groupRepository.findAllForDropDownByStaffId(1L, PageRequest.of(0, 100)).getContent(), hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForDropDownByDepartmentIdTest() {
        assertThat("Slice of Group is not of size = 4",
                groupRepository.findAllForDropDownByDepartmentId(2L, PageRequest.of(0, 100)).getContent(), hasSize(4));
    }

    //----------------------------------------------------ADMIN---------------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/group_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllAdminTest() {
        assertThat("Page of Group is not of size = 8",
                groupRepository.findAllAdmin(PageRequest.of(0, 50)).getContent(), hasSize(8));
    }
}
