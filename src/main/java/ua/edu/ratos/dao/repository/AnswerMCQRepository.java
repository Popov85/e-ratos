package ua.edu.ratos.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ratos.dao.entity.answer.AnswerMCQ;

public interface AnswerMCQRepository extends JpaRepository<AnswerMCQ, Long> {
}
