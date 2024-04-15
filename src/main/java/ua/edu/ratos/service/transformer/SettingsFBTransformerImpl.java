package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Language;
import ua.edu.ratos.dao.entity.SettingsFB;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.SettingsFBInDto;
import ua.edu.ratos.service.transformer.SettingsFBTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class SettingsFBTransformerImpl implements SettingsFBTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public SettingsFB toEntity(@NonNull final SettingsFBInDto dto) {
        SettingsFB settings = modelMapper.map(dto, SettingsFB.class);
        settings.setLang(em.getReference(Language.class, dto.getLang()));
        settings.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        return settings;
    }
}
