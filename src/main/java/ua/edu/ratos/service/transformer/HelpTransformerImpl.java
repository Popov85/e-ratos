package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.HelpInDto;
import ua.edu.ratos.service.transformer.HelpTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class HelpTransformerImpl implements HelpTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final SecurityUtils securityUtils;

    @Override
    public Help toEntity(@NonNull final HelpInDto dto) {
        Help help = new Help();
        help.setHelpId(dto.getHelpId());
        help.setName(dto.getName());
        help.setHelp(dto.getHelp());
        help.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        if (dto.getResourceId()!=0) {
            help.addResource(em.find(Resource.class, dto.getResourceId()));
        }
        return help;
    }
}
