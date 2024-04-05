package ua.edu.ratos.dao.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.dao.entity.grading.FreePointGrading;
import ua.edu.ratos.service.session.grade.GradedResult;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FreePointGradingTest {

    private static FreePointGrading freePointGrading;

    @BeforeEach
    public void init() {
        freePointGrading = new FreePointGrading();
        freePointGrading.setMinValue((short) 0);
        freePointGrading.setPassValue((short) 60);
        freePointGrading.setMaxValue((short) 200);
    }

    @ParameterizedTest
    @MethodSource("gradeProvider")
    public void shouldBeGradedCorrectlyTest(double input, short expected) {
        final GradedResult grade = freePointGrading.grade(input);
        assertEquals(expected, grade.getGrade(), "Graded result does not match expected");
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> gradeProvider() {
        return Stream.of(
                Arguments.of(0, (short) 0),
                Arguments.of(50, (short) 100),
                Arguments.of(100, (short) 200),
                Arguments.of(75.5, (short) 151),
                Arguments.of(93.3333333333, (short) 187)
        );
    }
}
