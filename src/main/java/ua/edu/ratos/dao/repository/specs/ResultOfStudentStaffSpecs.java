package ua.edu.ratos.dao.repository.specs;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ua.edu.ratos.dao.entity.*;
import jakarta.persistence.criteria.*;

/**
 * https://stackoverflow.com/questions/30630642/querying-a-nullable-onetoone-relationship-with-jpa
 */
public class ResultOfStudentStaffSpecs {

    public static Specification<ResultOfStudent> ofDepartment(@NonNull final Long depId) {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            Path<Department> departmentPath = root.get(ResultOfStudent_.department);
            fetchDetails(root, query);
            return builder.equal(departmentPath.get(Department_.depId), depId);
        };
    }

    public static Specification<ResultOfStudent> ofDepartmentForReport(@NonNull final Long depId) {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            Path<Department> departmentPath = root.get(ResultOfStudent_.department);
            fetchDetails(root, query);
            fetchAffiliationDetails(root, query);
            return builder.equal(departmentPath.get(Department_.depId), depId);
        };
    }

    // For reports on results
    public static Specification<ResultOfStudent> ofFacultyForReport(@NonNull final Long facId) {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            Path<Faculty> facultyPath = root.get(ResultOfStudent_.department).get(Department_.faculty);
            fetchDetails(root, query);
            fetchAffiliationDetails(root, query);
            return builder.equal(facultyPath.get(Faculty_.facId), facId);
        };
    }

    // For reports on results
    public static Specification<ResultOfStudent> ofOrganisationForReport(@NonNull final Long orgId) {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            Path<Organisation> organisationPath = root.get(ResultOfStudent_.department).get(Department_.faculty).get(Faculty_.organisation);
            fetchDetails(root, query);
            fetchAffiliationDetails(root, query);
            return builder.equal(organisationPath.get(Organisation_.orgId), orgId);
        };
    }

    // For reports on results
    public static Specification<ResultOfStudent> ofRatosForReport() {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            Path<Department> departmentPath = root.get(ResultOfStudent_.department);
            fetchDetails(root, query);
            fetchAffiliationDetails(root, query);
            return builder.notEqual(departmentPath.get(Department_.depId), 0L);
        };
    }

    private static void fetchDetails(Root<ResultOfStudent> root, CriteriaQuery<?> query) {
        if (Long.class != query.getResultType()) {
            root.fetch(ResultOfStudent_.scheme, JoinType.INNER)
                    .fetch(Scheme_.course, JoinType.INNER).fetch(Course_.lmsCourse, JoinType.LEFT);

            Fetch<ResultOfStudent, Student> fs = root.fetch(ResultOfStudent_.student, JoinType.INNER);
            fs.fetch(Student_.user, JoinType.INNER);
            fs.fetch(Student_.studentClass, JoinType.INNER);
            fs.fetch(Student_.faculty, JoinType.INNER);
            fs.fetch(Student_.organisation, JoinType.INNER);

            root.fetch(ResultOfStudent_.resultDetails, JoinType.LEFT);
        }
    }

    private static void fetchAffiliationDetails(Root<ResultOfStudent> root, CriteriaQuery<?> query) {
        if (Long.class != query.getResultType()) {
            Fetch<ResultOfStudent, Department> fetchDep = root.fetch(ResultOfStudent_.department, JoinType.INNER);
            Fetch<Department, Faculty> fetchFac = fetchDep.fetch(Department_.faculty, JoinType.INNER);
            fetchFac.fetch(Faculty_.organisation, JoinType.INNER);
        }
    }
}
