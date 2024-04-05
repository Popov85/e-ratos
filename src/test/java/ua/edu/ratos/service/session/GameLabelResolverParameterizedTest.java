package ua.edu.ratos.service.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ratos.config.properties.AppProperties;

import java.util.TreeMap;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class GameLabelResolverParameterizedTest {

    @InjectMocks
    private GameLabelResolver gameLabelResolver;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppProperties appProperties;

    @BeforeEach
    public void setUp() {
        TreeMap<Integer, String> labels = new TreeMap<>();
        // Initializing labels as before
        labels.put(0, "Novice");
        labels.put(1, "Beginner");
        labels.put(2, "Smart");
        labels.put(3, "Mature");
        labels.put(5, "Professional");
        labels.put(20, "Expert");
        labels.put(100, "Genius");
        given(appProperties.getGame().getUserLabel()).willReturn(labels);
        given(appProperties.getGame().getUserLabel()).willReturn(labels);
    }

    static Stream<Object[]> data() {
        return Stream.of(
                new Object[]{0, "Novice"},
                new Object[]{1, "Beginner"},
                new Object[]{2, "Smart"},
                new Object[]{3, "Mature"},
                new Object[]{4, "Mature"},
                new Object[]{5, "Professional"},
                new Object[]{6, "Professional"},
                new Object[]{10, "Professional"},
                new Object[]{19, "Professional"},
                new Object[]{20, "Expert"},
                new Object[]{21, "Expert"},
                new Object[]{99, "Expert"},
                new Object[]{100, "Genius"},
                new Object[]{500, "Genius"},
                new Object[]{1000, "Genius"}
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void getLabelTest(int totalWins, String expectedTitle) {
        String actualTitle = gameLabelResolver.getLabel(totalWins);
        assertThat("Expected winner title is not as expected when totalWins=" + totalWins, actualTitle, equalTo(expectedTitle));
    }
}
