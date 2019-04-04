package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.repository.ThemeRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ThemeGenerator {

    @Autowired
    private Rnd rnd;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ThemeRepository themeRepository;

    @TrackTime
    @Transactional
    public List<Theme> generate(int quantity, List<Course> list) {
        List<Theme> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            int index;
            if (list.size()==1) {
                index = 0;
            } else {
                index = rnd.rnd(0, list.size() - 1);
            }
            Course course = list.get(index);
            Theme theme = createOne(i, course);
            result.add(theme);
        }
        themeRepository.saveAll(result);
        return result;
    }

    private Theme createOne(int i, Course course) {
        Theme theme = new Theme();
        theme.setName("Theme_#"+ UUID.randomUUID());
        theme.setAccess(em.getReference(Access.class, rnd.rndOne(2)));
        theme.setStaff(em.getReference(Staff.class, 1L));
        theme.setCourse(course);
        theme.setDepartment(em.getReference(Department.class, course.getDepartment().getDepId()));
        theme.setCreated(LocalDateTime.now().minusDays(rnd.rnd(1, 1000)));
        return theme;
    }
}
