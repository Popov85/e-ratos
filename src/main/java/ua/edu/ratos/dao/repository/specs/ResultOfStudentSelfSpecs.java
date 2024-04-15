package ua.edu.ratos.dao.repository.specs;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ua.edu.ratos.dao.entity.*;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;


public class ResultOfStudentSelfSpecs {

    public static Specification<ResultOfStudent> byStudent(@NonNull final Long studId) {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            Path<Student> studentPath = root.get(ResultOfStudent_.student);
            if (Long.class != query.getResultType()) {
                root.fetch(ResultOfStudent_.scheme.getName(), JoinType.LEFT)
                        .fetch(Scheme_.course.getName(), JoinType.LEFT);
                root.fetch(ResultOfStudent_.department.getName(), JoinType.LEFT);
            }
            return builder.equal(studentPath.get(Student_.studId), studId);
        };
    }

}
