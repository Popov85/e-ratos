package ua.edu.ratos.service.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.grading.Grading;
import ua.edu.ratos.dao.repository.SchemeRepository;
import ua.edu.ratos.service.grading.SchemeGradingManagerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generator of Schemes for STEP-like scenarios!
 */
@Slf4j
@Component
@Profile({"dev"})
public class SchemeGeneratorStep {

    @Autowired
    private Rnd rnd;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private SchemeGradingManagerService gradingManagerService;

    @Autowired
    private RealWordGenerator realWordGenerator;

    @TrackTime
    @Transactional
    public List<Scheme> generate(int quantity, List<Theme> themes, List<Department> departments, List<Course> courses) {
        List<Scheme> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Department department = departments.get(0);
            Course course = courses.get(0);
            Scheme scheme = createOne(themes, department, course);
            schemeRepository.save(scheme);
            gradingManagerService.save(scheme.getSchemeId(), scheme.getGrading().getGradingId(), 1L);
            result.add(scheme);
        }
        return result;
    }

    private Scheme createOne(List<Theme> themes, Department department, Course course) {
        Scheme scheme = new Scheme();
        String name = realWordGenerator.createSentence(10, 30, false);
        scheme.setName("Scheme for STEP: "+ name);
        scheme.setStaff(em.getReference(Staff.class, 1L));
        scheme.setDepartment(department);
        scheme.setCourse(course);
        scheme.setGrading(em.getReference(Grading.class, 1L));
        scheme.setSettings(em.getReference(Settings.class, 1L));
        scheme.setStrategy(em.getReference(Strategy.class, 1L));
        scheme.setOptions(em.getReference(Options.class, 1L));
        scheme.setMode(em.getReference(Mode.class, 1L));
        scheme.setAccess(em.getReference(Access.class, 1L));
        scheme.setCreated(OffsetDateTime.now().minusDays(rnd.rnd(1, 1000)));
        scheme.setActive(true);
        scheme.setThemes(getThemes(themes, scheme));
        return scheme;
    }


    private List<SchemeTheme> getThemes(List<Theme> themes, Scheme scheme) {
        List<SchemeTheme> result = new ArrayList<>(themes.size());
        for (Theme theme : themes) {
            SchemeTheme s = new SchemeTheme();
            s.setScheme(scheme);
            s.setTheme(theme);
            s.setSettings(getSettings(s));
            result.add(s);
        }
        return result;
    }

    private Set<SchemeThemeSettings> getSettings(SchemeTheme schemeTheme) {
        Set<SchemeThemeSettings> result = new HashSet<>();
        SchemeThemeSettings s = new SchemeThemeSettings();
        s.setSchemeTheme(schemeTheme);
        s.setType(em.getReference(QuestionType.class, 1L));
        s.setLevel1((short) 20);
        result.add(s);
        return result;
    }
}
