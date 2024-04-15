package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Complaint;
import ua.edu.ratos.dao.entity.ComplaintId;
import ua.edu.ratos.dao.entity.ComplaintType;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.dao.repository.ComplaintRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.out.ComplaintOutDto;
import ua.edu.ratos.service.transformer.mapper.ComplaintMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ComplaintService {

    @PersistenceContext
    private final EntityManager em;

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    private final SecurityUtils securityUtils;

    //------------------------------------------------CRUD--------------------------------------------------------------
    /**
     * A user can simultaneously send multiple types of complaints about single question.
     * All complaints of the same type and about the same question are summarized.
     * @param questionId question a user complains on
     * @param complaintTypes a set of complaint types
     * @param depId department to which the question belongs (derives from a session scheme's department)
     */
    @Transactional
    public void save(@NonNull final Long questionId, @NonNull final Set<Long> complaintTypes, @NonNull final Long depId) {
        for (Long complaintType : complaintTypes) {
            ComplaintId complaintId = new ComplaintId(questionId, complaintType);
            Optional<Complaint> complaint = complaintRepository.findById(complaintId);
            if (complaint.isPresent()) {
                Complaint c = complaint.get();
                c.setLastComplained(LocalDateTime.now());
                c.setTimesComplained(c.getTimesComplained()+1);
            } else {
                Complaint c = new Complaint();
                c.setComplaintId(complaintId);
                c.setComplaintType(em.getReference(ComplaintType.class, complaintType));
                c.setQuestion(em.getReference(Question.class, questionId));
                c.setDepartment(em.getReference(Department.class, depId));
                c.setLastComplained(LocalDateTime.now());
                c.setTimesComplained(1);
                complaintRepository.save(c);
            }
        }
    }

    @Transactional
    public void deleteById(@NonNull final Long questionId, @NonNull final Long complaintTypeId) {
        ComplaintId complaintId = new ComplaintId(questionId, complaintTypeId);
        complaintRepository.deleteById(complaintId);
    }

    //-------------------------------------------------Staff table------------------------------------------------------
    @Transactional(readOnly = true)
    public Page<ComplaintOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        return complaintRepository.findAllByDepartmentId(securityUtils.getAuthDepId(), pageable).map(complaintMapper::toDto);
    }

}
