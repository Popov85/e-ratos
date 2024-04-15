package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Result;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.Theme;
import ua.edu.ratos.dao.entity.User;
import ua.edu.ratos.dao.entity.lms.LMS;
import ua.edu.ratos.dao.repository.ResultRepository;
import ua.edu.ratos.service.domain.ResultDomain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class ResultService {

    @PersistenceContext
    private final EntityManager em;

    private final ResultRepository resultRepository;

    @Transactional
    public Long save(@NonNull final ResultDomain resultDomain) {
        Result r = new Result();
        Scheme scheme = em.find(Scheme.class, resultDomain.getScheme().getSchemeId());
        r.setScheme(scheme);
        r.setUser(em.getReference(User.class, resultDomain.getUser().getUserId()));
        r.setDepartment(scheme.getDepartment());
        if (resultDomain.getLmsId().isPresent())
            r.setLms(em.getReference(LMS.class, resultDomain.getLmsId().get()));
        r.setPassed(resultDomain.isPassed());
        r.setPoints(resultDomain.hasPoints());
        r.setGrade(resultDomain.getGrade());
        r.setPercent(resultDomain.getPercent());
        r.setSessionLasted(resultDomain.getTimeSpent());
        r.setSessionEnded(OffsetDateTime.now());
        r.setTimeOuted(resultDomain.isTimeOuted());
        r.setCancelled(resultDomain.isCancelled());
        resultDomain.getThemeResults().forEach(t -> {
            Theme theme = em.getReference(Theme.class, t.getTheme().getThemeId());
            r.addResultTheme(theme, t.getPercent(), t.getQuantity());
        });
        resultRepository.save(r);

        return r.getResultId();
    }
}
