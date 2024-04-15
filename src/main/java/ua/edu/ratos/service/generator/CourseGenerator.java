package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.Course;
import ua.edu.ratos.dao.repository.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev"})
public class CourseGenerator {
    @Autowired
    private Rnd rnd;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CourseRepository courseRepository;

    @TrackTime
    @Transactional
    public List<Course> generate(int quantity, List<Department> list) {
        List<Course> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            int index;
            if (list.size()==1) {
                index = 0;
            } else {
                index = rnd.rnd(0, list.size());
            }
            Department dep = list.get(index);
            Course course = createOne(i, dep);
            result.add(course);
        }
        courseRepository.saveAll(result);
        return result;
    }

    private Course createOne(int i, Department department) {
        Course course = new Course();
        course.setName("Course_#"+i);
        course.setCreated(OffsetDateTime.now().minusDays(rnd.rnd(1, 1000)));
        course.setAccess(em.getReference(Access.class, rnd.rndOne(2)));
        course.setStaff(em.getReference(Staff.class, 1L));
        course.setDepartment(department);
        return course;
    }
}
