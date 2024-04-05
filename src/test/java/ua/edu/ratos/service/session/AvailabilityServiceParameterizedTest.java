package ua.edu.ratos.service.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.dao.entity.Group;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.Student;
import ua.edu.ratos.dao.entity.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AvailabilityServiceParameterizedTest {

    private static Scheme scheme;

    @BeforeEach
    public void setUp() {
        scheme = new Scheme();
        scheme.setSchemeId(1L);

        Group g1 = new Group();
        g1.setStudents(new HashSet<>(Arrays.asList(
                createStudent(1L, true),
                createStudent(5L, true),
                createStudent(17L, true),
                createStudent(141L, true),
                createStudent(489L, false))));

        Group g2 = new Group();
        g2.setStudents(new HashSet<>(Arrays.asList(
                createStudent(2L, true),
                createStudent(8L, true),
                createStudent(11L, true),
                createStudent(99L, true),
                createStudent(215L, false))));

        scheme.setGroups(new HashSet<>(Arrays.asList(g1, g2)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void isSchemeAvailablePositiveCaseTest(Long userId, boolean expectedResult) {
        AvailabilityService availabilityService = new AvailabilityService();
        assertThat("Scheme is not available where it must be for user=" + userId,
                availabilityService.isSchemeAvailable(scheme, userId), is(expectedResult));
    }

    public static Stream<Object[]> data() {
        return Stream.of(
                new Object[]{1L, true},
                new Object[]{5L, true},
                new Object[]{17L, true},
                new Object[]{141L, true},
                new Object[]{489L, false},
                new Object[]{2L, true},
                new Object[]{8L, true},
                new Object[]{11L, true},
                new Object[]{99L, true},
                new Object[]{215L, false},
                new Object[]{3L, false},
                new Object[]{33L, false},
                new Object[]{100L, false},
                new Object[]{501L, false}
        );
    }

    private static Student createStudent(Long studId, boolean active) {
        Student s = new Student();
        s.setStudId(studId);
        User u = new User();
        u.setActive(active);
        s.setUser(u);
        return s;
    }

}
