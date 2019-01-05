package ua.edu.ratos.service.transformer.dto_to_entity;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.service.dto.in.ThemeInDto;

import javax.persistence.EntityManager;

@Component
public class DtoThemeTransformer {

    @Autowired
    private EntityManager em;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public Theme toEntity(@NonNull ThemeInDto dto) {
        return toEntity(null, dto);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Theme toEntity(Long themeId, @NonNull ThemeInDto dto) {
        Theme theme = modelMapper.map(dto, Theme.class);
        theme.setThemeId(themeId);
        theme.setCourse(em.getReference(Course.class, dto.getCourseId()));
        return theme;
    }
}