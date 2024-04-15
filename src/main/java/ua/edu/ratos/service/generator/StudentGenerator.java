package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.Clazz;
import ua.edu.ratos.dao.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.*;

@Component
@Profile({"dev"})
public class StudentGenerator {

    private static final int[] YEAR = {2016, 2017, 2018, 2019};

    @Autowired
    private Rnd rnd;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private StudentRepository studentRepository;

    @TrackTime
    @Transactional
    public List<Student> generate(int quantity, List<Clazz> list) {
        List<Role> roles =  Arrays.asList(em.getReference(Role.class, 2L));
        List<Student> results = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Clazz cl;
            if (list.size() == 1) {
                cl = list.get(0);
            } else {
                cl = list.get(rnd.rnd(0, list.size() - 1));
            }
            Student stud = createOne(i, cl, roles);
            results.add(stud);
        }
        studentRepository.saveAll(results);
        return results;
    }

    private Student createOne(int i, Clazz cl, List<Role> roles) {
        Student stud = new Student();
        User user = new User();
        user.setName("name"+i);
        user.setSurname("surname"+i);
        user.setEmail(user.getName()+"."+user.getSurname()+"@example.com");
        user.setPassword(("{noop}name&surname"+i).toCharArray());
        user.setActive(true);
        user.setRoles(new HashSet<>(roles));
        stud.setUser(user);
        stud.setEntranceYear(YEAR[rnd.rnd(0, 3)]);
        stud.setStudentClass(cl);
        stud.setFaculty(em.getReference(Faculty.class, 1L));
        stud.setOrganisation(em.getReference(Organisation.class, 1L));
        return stud;
    }

}
