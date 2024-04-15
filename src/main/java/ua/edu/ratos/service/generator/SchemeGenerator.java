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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generator of Schemes for performance testing only!
 * Given:
 * N Themes;
 * 1-3 themes associated with a Scheme;
 * 1-5 settings associated with a SchemeTheme;
 */
@Slf4j
@Component
@Profile({"dev"})
public class SchemeGenerator {

    //private static final int MAX_THEMES_PER_SCHEME = 3;

    private static final int MAX_QUESTION_TYPE_ID = 1;

    private static final int MIN_LEVEL_1 = 10;
    private static final int MAX_LEVEL_1 = 20;

    private static final int MIN_LEVEL_2 = 0;
    private static final int MAX_LEVEL_2 = 0;
    private static final int MIN_LEVEL_3 = 0;
    private static final int MAX_LEVEL_3 = 0;


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
    public List<Scheme> generate(int quantity, List<Theme> themes, List<Department> departments, List<Course> courses, int maxThemes) {
        List<Scheme> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Department department;
            if (departments.size()==1) {
                department = departments.get(0);
            } else {
                department = departments.get(rnd.rnd(0, departments.size()));
            }
            Course course;
            if (courses.size()==1) {
                course = courses.get(0);
            } else {
                course = courses.get(rnd.rnd(0, courses.size() - 1));
            }
            Scheme scheme = createOne(themes, department, course, maxThemes);
            schemeRepository.save(scheme);
            gradingManagerService.save(scheme.getSchemeId(), scheme.getGrading().getGradingId(), 1L);
            result.add(scheme);
        }
        return result;
    }

    private Scheme createOne(List<Theme> themes, Department department, Course course, int maxThemes) {
        Scheme scheme = new Scheme();
        String name = realWordGenerator.createSentence(10, 30, false);
        scheme.setName("Scheme: "+name);
        scheme.setStaff(em.getReference(Staff.class, 1L));
        scheme.setDepartment(department);
        scheme.setCourse(course);
        scheme.setGrading(em.getReference(Grading.class, rnd.rndOne(4)));
        scheme.setSettings(em.getReference(Settings.class, 1L));
        scheme.setOptions(em.getReference(Options.class, 2L));
        scheme.setStrategy(em.getReference(Strategy.class, rnd.rndOne(4)));
        scheme.setMode(em.getReference(Mode.class, rnd.rndOne(3)));
        scheme.setAccess(em.getReference(Access.class, rnd.rndOne(3)));
        scheme.setCreated(OffsetDateTime.now().minusDays(rnd.rnd(1, 1000)));
        scheme.setActive(true);
        scheme.setThemes(getThemes(themes, scheme, maxThemes));
        return scheme;
    }


    private List<SchemeTheme> getThemes(List<Theme> themes, Scheme scheme, int maxThemes) {
        int quantity = rnd.rnd(1, maxThemes+1);
        List<SchemeTheme> result = new ArrayList<>(quantity);
        List<Integer> themesIndices = getUniqueThemeIndices(quantity, themes);
        log.debug("Schemes themes indices = {}", themesIndices);
        for (int i = 0; i < quantity; i++) {
            SchemeTheme s = new SchemeTheme();
            s.setScheme(scheme);
            s.setTheme(themes.get(themesIndices.get(i)));
            s.setSettings(getSettings(s));
            result.add(s);
        }
        return result;
    }

    private List<Integer> getUniqueThemeIndices(int quantity, List<Theme> themes) {
        if (themes.size()==1) return Arrays.asList(0);
        Set<Integer> result = new HashSet<>();
        while (result.size()<quantity) {
            int index = this.rnd.rnd(0, themes.size());
            result.add(index);
        }
        return result.stream().collect(Collectors.toList());
    }

    private Set<SchemeThemeSettings> getSettings(SchemeTheme schemeTheme) {
        Set<SchemeThemeSettings> result = new HashSet<>();
        for (int i = 1; i <= rnd.rnd(1, MAX_QUESTION_TYPE_ID+1); i++) {
            SchemeThemeSettings s = new SchemeThemeSettings();
            s.setSchemeTheme(schemeTheme);
            s.setType(em.getReference(QuestionType.class, (long)i));
            s.setLevel1((short) rnd.rnd(MIN_LEVEL_1, MAX_LEVEL_1+1));
            s.setLevel2((short) rnd.rnd(MIN_LEVEL_2, MAX_LEVEL_2+1));
            s.setLevel3((short) rnd.rnd(MIN_LEVEL_3, MAX_LEVEL_3+1));
            result.add(s);
        }
        return result;
    }
}
