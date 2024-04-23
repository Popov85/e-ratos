package ua.edu.ratos.service.transformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.service.dto.in.StudentInDto;
import ua.edu.ratos.service.transformer.mapper.UserMapper;

import java.util.Set;

@Component
@AllArgsConstructor
public class StudTransformerImpl implements StudTransformer {

    private final static String DEFAULT_ROLE_STUDENT = "ROLE_STUDENT";

    @PersistenceContext
    private final EntityManager em;

    private final UserMapper userMapper;

    @Override
    public Student toEntity(@NonNull final StudentInDto dto) {
        Student stud = new Student();
        stud.setStudId(dto.getStudId());
        User user = userMapper.toEntity(dto.getUser());
        user.setRoles(Set.of(DEFAULT_ROLE_STUDENT));
        stud.setUser(user);
        stud.setEntranceYear(dto.getEntranceYear());
        stud.setStudentClass(em.getReference(Clazz.class, dto.getClassId()));
        stud.setFaculty(em.getReference(Faculty.class, dto.getFacId()));
        stud.setOrganisation(em.getReference(Organisation.class, dto.getOrgId()));
        return stud;
    }
}
