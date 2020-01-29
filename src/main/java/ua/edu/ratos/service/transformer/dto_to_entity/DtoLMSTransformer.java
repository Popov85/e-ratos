package ua.edu.ratos.service.transformer.dto_to_entity;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Organisation;
import ua.edu.ratos.dao.entity.lms.LMS;
import ua.edu.ratos.dao.entity.lms.LTICredentials;
import ua.edu.ratos.dao.entity.lms.LTIVersion;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.LMSInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class DtoLMSTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final SecurityUtils securityUtils;

    // For new
    public LMS toEntity(@NonNull final LMSInDto dto) {
        LMS lms = new LMS();
        lms.setLmsId(dto.getLmsId());
        lms.setName(dto.getName());
        lms.setLtiVersion(em.getReference(LTIVersion.class, dto.getVersionId()));
        lms.setOrganisation(em.getReference(Organisation.class, securityUtils.getAuthOrgId()));
        em.persist(lms);
        LTICredentials credentials = new LTICredentials();
        credentials.setCredId(lms.getLmsId());
        credentials.setKey(dto.getKey());
        credentials.setSecret(dto.getSecret());
        lms.setCredentials(credentials);
        return lms;
    }

    // For update
    public LMS toEntity(@NonNull final LMS lms, @NonNull final LMSInDto dto) {
        lms.setName(dto.getName());
        lms.setLtiVersion(em.getReference(LTIVersion.class, dto.getVersionId()));
        LTICredentials credentials = lms.getCredentials();
        credentials.setKey(dto.getKey());
        credentials.setSecret(dto.getSecret());
        return lms;
    }
}
