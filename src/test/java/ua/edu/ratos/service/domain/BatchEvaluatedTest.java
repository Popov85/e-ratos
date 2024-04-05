package ua.edu.ratos.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ratos.service.domain.response.ResponseMCQ;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatchEvaluatedTest {

    @Mock
    private BatchEvaluated batchEvaluated;

    @Test
    public void getIncorrectResponsesIdsZeroTest() {
        List<ResponseEvaluated> responsesEvaluated = List.of(
                createResponseEvaluated(1L, 100),
                createResponseEvaluated(2L, 100),
                createResponseEvaluated(3L, 100),
                createResponseEvaluated(4L, 100));

        when(batchEvaluated.getResponsesEvaluated()).thenReturn(responsesEvaluated);  // Mock implementation
        when(batchEvaluated.getIncorrectResponseIds()).thenCallRealMethod();  // Real implementation

        // Actual test begins
        List<Long> actual = batchEvaluated.getIncorrectResponseIds();

        assertThat("Array is not empty", actual, is(empty()));
    }


    @Test
    public void getIncorrectResponsesIdsOneTest() {
        List<ResponseEvaluated> responsesEvaluated = List.of(
                createResponseEvaluated(1L, 100),
                createResponseEvaluated(2L, 100),
                createResponseEvaluated(3L, 0),
                createResponseEvaluated(4L, 100));

        when(batchEvaluated.getResponsesEvaluated()).thenReturn(responsesEvaluated);  // Mock implementation
        when(batchEvaluated.getIncorrectResponseIds()).thenCallRealMethod();  // Real implementation

        // Actual test begins
        List<Long> actual = batchEvaluated.getIncorrectResponseIds();

        assertThat("Wrong array of incorrect responses", List.of(3L), is(equalTo(actual)));
    }


    @Test
    public void getIncorrectResponsesIdsTwoTest() {
        List<ResponseEvaluated> responsesEvaluated = List.of(
                createResponseEvaluated(1L, 0),
                createResponseEvaluated(2L, 100),
                createResponseEvaluated(3L, 0),
                createResponseEvaluated(4L, 100));

        when(batchEvaluated.getResponsesEvaluated()).thenReturn(responsesEvaluated);  // Mock implementation
        when(batchEvaluated.getIncorrectResponseIds()).thenCallRealMethod();  // Real implementation

        // Actual test begins
        List<Long> actual = batchEvaluated.getIncorrectResponseIds();

        assertThat("Wrong array of incorrect responses", List.of(1L, 3L), is(equalTo(actual)));
    }

    @Test
    public void getIncorrectResponsesIdsAllTest() {
        List<ResponseEvaluated> responsesEvaluated = List.of(
                createResponseEvaluated(1L, 0),
                createResponseEvaluated(2L, 0),
                createResponseEvaluated(3L, 0),
                createResponseEvaluated(4L, 0));

        when(batchEvaluated.getResponsesEvaluated()).thenReturn(responsesEvaluated);  // Mock implementation
        when(batchEvaluated.getIncorrectResponseIds()).thenCallRealMethod();  // Real implementation

        // Actual test begins
        List<Long> actual = batchEvaluated.getIncorrectResponseIds();

        assertThat("Wrong array of incorrect responses", List.of(1L, 2L, 3L, 4L), is(equalTo(actual)));
    }

    private ResponseEvaluated createResponseEvaluated(long qId, double score) {
        return new ResponseEvaluated(qId, new ResponseMCQ(qId, new HashSet<>()), score, false);
    }

}
