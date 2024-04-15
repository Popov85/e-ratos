package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ThemeInDto;
import ua.edu.ratos.service.transformer.ThemeTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
public class ThemeTransformerImpl implements ThemeTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public Theme toEntity(@NonNull final ThemeInDto dto) {
        Theme theme = modelMapper.map(dto, Theme.class);
        theme.setAccess(em.getReference(Access.class, dto.getAccessId()));
        theme.setCourse(em.getReference(Course.class, dto.getCourseId()));
        theme.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        theme.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        theme.setCreated(OffsetDateTime.now());
        return theme;
    }

    @Override
    public Theme toEntity(@NonNull final Theme entity, @NonNull final ThemeInDto dto) {
        Theme theme = entity;
        theme.setThemeId(dto.getThemeId());
        theme.setName(dto.getName());
        theme.setAccess(em.getReference(Access.class, dto.getAccessId()));
        theme.setCourse(em.getReference(Course.class, dto.getCourseId()));
        return theme;
    }
}
