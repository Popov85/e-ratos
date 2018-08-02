package ua.edu.ratos.domain.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ua.edu.ratos.domain.model.ResultMock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ResultTest {

    private List<ResultMock> result = Arrays.asList(
            new ResultMock(17963L, "Clara", 0),
            new ResultMock(17963L, "Clara", 35),
            new ResultMock(17963L, "Clara", 60),
            new ResultMock(17963L, "Clara", 78),
            new ResultMock(17963L, "Clara", 90),
            new ResultMock(17963L, "Clara", 120),
            new ResultMock(17963L, "Clara", -1)
    );

    @Test
    public void prepare() {
        List<Integer> marks = result.stream()
                .map(Result->Result.getResult())
                .collect(Collectors.toList());
        assertThat(marks, containsInAnyOrder(0, 35, 60, 78, 90, 120, -1));
    }

    @Test
    public void calculateMarkShouldGiveUnsatisfactoryMarkTest() {
        String actualResult = result.get(0).calculateMark();
        System.out.println(result.get(0));
        //assertEquals(actualResult, "Unsatisfactory");
        assertThat(actualResult, is("Unsatisfactory"));
        actualResult = result.get(1).calculateMark();
        assertEquals(actualResult, "Unsatisfactory");
    }


    @Test
    public void calculateMarkShouldGiveSatisfactoryMarkTest() {
        String actualResult = result.get(2).calculateMark();
        assertEquals(actualResult, "Satisfactory");
    }

    @Test
    public void calculateMarkShouldGiveGoodMarkTest() {
        String actualResult = result.get(3).calculateMark();
        assertEquals(actualResult, "Good");
    }

    @Test
    public void calculateMarkShouldGiveExcellentMarkTest() {
        String actualResult = result.get(4).calculateMark();
        assertEquals(actualResult, "Excellent");
    }

    @Test(expected = RuntimeException.class)
    public void calculateMarkTooBigNumberTest() {
        String actualResult = result.get(5).calculateMark();
    }


    @Test(expected = RuntimeException.class)
    public void calculateMarkTooSmallNumberTest() {
        String actualResult = result.get(6).calculateMark();
    }
}