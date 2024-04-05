package ua.edu.ratos.service.session;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.service.domain.SettingsDomain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EvaluatorPostProcessorTest {

    @Mock
    private SettingsDomain settingsDomain;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppProperties appProperties;

    @InjectMocks
    private EvaluatorPostProcessor evaluatorPostProcessor;

    @Test
    public void applyBountyTo2dLevelQuestionTest() {
        double actual = evaluatorPostProcessor.applyBounty(70.0, (byte) 2, 1.1f, 1.2f);
        assertEquals(77, actual, 0.01, "Wrong bounty score");
    }


    @Test
    public void applyPenaltyOf50PercentToFullyCorrectScoreTest() {
        when(appProperties.getSession().getTimeoutPenalty()).thenReturn(50d);
        double actual = evaluatorPostProcessor.applyPenalty(100d);
        assertEquals( 50, actual, 0.01, "Wrong penalised score");
    }

    @Test
    public void getBountyFor3dLevelQuestionTest() {
        when(settingsDomain.getLevel3Coefficient()).thenReturn(1.2f);
        Double actual = evaluatorPostProcessor.getBounty((byte) 3, settingsDomain);
        assertEquals(20, actual, 0.01, "Wrong bounty value, %");

    }

    @Test
    public void getPenalty() {
        when(appProperties.getSession().getTimeoutPenalty()).thenReturn(50d);
        Double actual = evaluatorPostProcessor.getPenalty();
        assertEquals( 50, actual, 0.01, "Wrong penalty value, %");
    }
}