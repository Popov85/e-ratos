package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.Phrase;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.repository.PhraseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev"})
public class PhraseGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private Rnd rnd;

    @TrackTime
    @Transactional
    public List<Phrase> generate(int quantity, List<Resource> resources) {
        List<Phrase> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
            Phrase question = createOne(i, resource);
            result.add(question);
        }
        phraseRepository.saveAll(result);
        return result;
    }

    private Phrase createOne(int i, Resource resource) {
        Phrase phrase = new Phrase();
        phrase.setPhrase("Phrase "+i);
        phrase.addResource(em.getReference(Resource.class, resource.getResourceId()));
        phrase.setStaff(em.getReference(Staff.class, 1L));
        return phrase;
    }
}
