package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.service.dto.out.report.OrgFacDep;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentRepositoryTestIT extends BaseIT {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/department_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByFacIdForDropDownTest() {
        Set<Department> result = departmentRepository.findAllByFacIdForDropDown(2L);
        assertThat("Set of departments by facId is not of right size", result, hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/department_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByFacIdForReportTest() {
        Set<OrgFacDep> result = departmentRepository.findAllByFacIdForReport(2L);
        assertThat("Set of OrgFacDep by facId is not of right size", result, hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/department_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByOrgIdForReportTest() {
        Set<OrgFacDep> result = departmentRepository.findAllByOrgIdForReport(2L);
        assertThat("Set of OrgFacDep by orgId is not of right size", result, hasSize(12));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/department_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByRatosForReportTest() {
        Set<OrgFacDep> result = departmentRepository.findAllByRatosForReport();
        assertThat("Set of OrgFacDep by ratos is not of right size", result, hasSize(37));
    }

}
