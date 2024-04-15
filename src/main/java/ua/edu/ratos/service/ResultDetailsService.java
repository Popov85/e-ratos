package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Result;
import ua.edu.ratos.dao.entity.ResultDetails;
import ua.edu.ratos.dao.repository.ResultDetailsRepository;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.session.SessionDataSerializerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class ResultDetailsService {

    @PersistenceContext
    private final EntityManager em;

    private final ResultDetailsRepository resultDetailsRepository;

    private final SessionDataSerializerService serializer;

    @Transactional
    public void save(@NonNull final SessionData sessionData, @NonNull final Long resultId) {
        ResultDetails resultDetails = new ResultDetails();
        // no resultDetails.getDetailId() should be set, it causes exception
        resultDetails.setResult(em.getReference(Result.class, resultId));
        resultDetails.setWhenRemove(calculateWhenRemove(sessionData));
        resultDetails.setJsonData(serializer.serialize(sessionData));
        resultDetailsRepository.save(resultDetails);
    }

    private LocalDateTime calculateWhenRemove(@NonNull final SessionData sessionData) {
        final short daysKeepResultDetails = sessionData
                .getSchemeDomain()
                .getSettingsDomain()
                .getDaysKeepResultDetails();
        return LocalDateTime.now().plusDays(daysKeepResultDetails);
    }

    // Scheduled {daily, weekly, monthly, yearly}
    @Transactional
    public void cleanExpired() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTimeNow = LocalDateTime.now().format(formatter);
        resultDetailsRepository.cleanExpired(formattedDateTimeNow);
    }
}
