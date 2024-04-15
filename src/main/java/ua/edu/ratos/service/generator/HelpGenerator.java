package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.repository.HelpRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev"})
public class HelpGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private RealSentenceGenerator realSentenceGenerator;

    @TrackTime
    @Transactional
    public List<Help> generate(int quantity) {
        List<Help> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Help help = createOne(i);
            result.add(help);
        }
        helpRepository.saveAll(result);
        return result;
    }

    private Help createOne(int number) {
        Help help = new Help();
        help.setName("Help name "+number);
        String content = realSentenceGenerator.createText(10, 20, false);
        help.setHelp(content);
        help.setStaff(em.getReference(Staff.class, 1L));
        return help;
    }
}
