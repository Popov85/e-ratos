package ua.edu.ratos.service.session;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.QuestionDomain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SessionData mutator!
 * Changes the state of the questions list
 */
@Slf4j
@Service
public class ShiftService {

    /**
     * Mutates the state of SessionData
     * Implements the possibility to skip a given question by moving it
     * to the end of the session list
     * @param idToShift single ID to be shifted
     * @param sessionData session data
     */
    public void doShift(@NonNull final Long idToShift, @NonNull final SessionData sessionData) {
        // Unique numbers as precondition
        final List<QuestionDomain> all = sessionData.getSequence();
        final Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        final QuestionDomain toShift = questionsMap.get(idToShift);
        if (toShift==null) return; // Do nothing is not found
        // 1. Remove the skipped question from the list
        all.remove(toShift);
        // & add it to the end
        all.add(toShift);
        // 2. Update index
        final int currentIndex = sessionData.getCurrentIndex();
        sessionData.setCurrentIndex(currentIndex-1);
        log.debug("Shifted question with ID = {}", idToShift);
    }

    /**
     * Mutates the state of SessionData
     * Implements Pyramid algorithm: moves the incorrectly answered questions to the end of the questions list
     * And
     * Updates the current index
     * @param idsToShift list of ID-s to be shifted
     * @param sessionData session data
     */
    public void doShift(@NonNull final List<Long> idsToShift, @NonNull final SessionData sessionData) {
        final List<QuestionDomain> all = sessionData.getSequence();
        final Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        final List<QuestionDomain> toShift = idsToShift
                .stream()
                .map(id -> questionsMap.get(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (toShift.isEmpty()) return; // // Do nothing is not found
        // 1. Remove all the shifted questions from the list
        all.removeAll(toShift);
        // & add them to the end
        all.addAll(toShift);
        // 2. Update index
        final int currentIndex = sessionData.getCurrentIndex();
        final int quantity = toShift.size();
        sessionData.setCurrentIndex(currentIndex-quantity);
        log.debug("Shifted = {} questions", toShift.size());
    }
}
