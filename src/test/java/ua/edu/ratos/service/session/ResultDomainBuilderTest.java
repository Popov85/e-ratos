package ua.edu.ratos.service.session;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.question.QuestionMCQDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.domain.response.ResponseMCQ;
import ua.edu.ratos.service.domain.ResultPerTheme;
import ua.edu.ratos.service.domain.*;
import ua.edu.ratos.service.session.grade.GradedResult;
import java.util.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResultDomainBuilderTest {

    private final static List<Integer> SCORES = Arrays.asList(100, 100, 0, 50, 100, 50, 100, 0, 0, 0);

    @Mock
    private SessionData sessionData;

    @Mock
    private GradingService gradingService;

    @Mock
    private ProgressDataService progressDataService;

    @InjectMocks
    private ResultBuilder resultBuilder;

    private ProgressData progressData;

    private SchemeDomain schemeDomain = getSchemeDomain();

    private List<QuestionDomain> questionDomains;

    private List<BatchEvaluated> batchesEvaluated;

    private List<ResponseEvaluated> responsesEvaluated;

    private Map<Long, QuestionDomain> questionsMap;


    @Before
    public void init() {
        questionDomains = new ArrayList<>();
        questionsMap = new HashMap<>();
        responsesEvaluated = new ArrayList<>();
        batchesEvaluated = new ArrayList<>();

        ThemeDomain t1 = getTheme(1L, "T1");
        ThemeDomain t2 = getTheme(2L, "T2");
        ThemeDomain t3 = getTheme(3L, "T3");
        ThemeDomain t4 = getTheme(4L, "T4");

        // ThemeDomain 1
        initBatchEvaluated(1L, "Q1", t1, SCORES.get(0));
        initBatchEvaluated(2L, "Q2", t1, SCORES.get(1));
        initBatchEvaluated(3L, "Q3", t1, SCORES.get(2));
        // ThemeDomain 2
        initBatchEvaluated(4L, "Q4", t2, SCORES.get(3));
        initBatchEvaluated(5L, "Q5", t2, SCORES.get(4));
        initBatchEvaluated(6L, "Q6", t2, SCORES.get(5));
        // ThemeDomain 3
        initBatchEvaluated(7L, "Q7", t3, SCORES.get(6));
        initBatchEvaluated(8L, "Q8", t3, SCORES.get(7));
        // ThemeDomain 4
        initBatchEvaluated(9L, "Q9", t4, SCORES.get(8));
        initBatchEvaluated(10L, "Q10", t4, SCORES.get(9));

        progressData = new ProgressData();
        progressData.setScore(5d);
        progressData.setBatchesEvaluated(batchesEvaluated);
    }


    @Test
    public void buildTest() {
        when(sessionData.getProgressData()).thenReturn(progressData);
        when(sessionData.getSchemeDomain()).thenReturn(schemeDomain);
        when(sessionData.getQuestionDomains()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);
        when(progressDataService.toResponseEvaluated(sessionData)).thenReturn(responsesEvaluated);
        when(gradingService.grade(2/3d*100, schemeDomain)).thenReturn(new GradedResult(true, 3));
        when(gradingService.grade(50.0, schemeDomain)).thenReturn(new GradedResult(true, 3));
        when(gradingService.grade(0.0, schemeDomain)).thenReturn(new GradedResult(false, 2));


        final ResultDomain resultDomain = resultBuilder.build(sessionData);
        final List<ResultPerTheme> themeResults = resultDomain.getThemeResults();

        final ResultPerTheme resultPerTheme1 = themeResults.get(0);
        final double percentT1 = resultPerTheme1.getPercent();
        Assert.assertEquals(66.6, percentT1, 0.1);
        final int quantityT1 = resultPerTheme1.getQuantity();
        Assert.assertEquals(3, quantityT1);
        final double grade1 = resultPerTheme1.getGrade();
        Assert.assertEquals(3, grade1, 0.1);

        final ResultPerTheme resultPerTheme2 = themeResults.get(1);
        final double percentT2 = resultPerTheme2.getPercent();
        Assert.assertEquals(66.6, percentT2, 0.1);
        final int quantityT2 = resultPerTheme2.getQuantity();
        Assert.assertEquals(3, quantityT2);
        final double grade2 = resultPerTheme2.getGrade();
        Assert.assertEquals(3, grade2,0.1);

        final ResultPerTheme resultPerTheme3 = themeResults.get(2);
        final double percentT3 = resultPerTheme3.getPercent();
        Assert.assertEquals(50.0, percentT3, 0.1);
        final int quantityT3 = resultPerTheme3.getQuantity();
        Assert.assertEquals(2, quantityT3);
        final double grade3 = resultPerTheme3.getGrade();
        Assert.assertEquals(3, grade3, 0.1);

        final ResultPerTheme resultPerTheme4 = themeResults.get(3);
        final double percentT4 = resultPerTheme4.getPercent();
        Assert.assertEquals(0.0, percentT4, 0.1);
        final int quantityT4 = resultPerTheme4.getQuantity();
        Assert.assertEquals(2, quantityT4);
        final double grade4 = resultPerTheme4.getGrade();
        Assert.assertEquals(2, grade4, 0.1);
    }





    private void initBatchEvaluated(Long questionId, String question, ThemeDomain t, int score) {
        final QuestionDomain q = getQuestion(questionId, question, t);
        final Response r = getResponse(questionId, 1L);
        ResponseEvaluated re = new ResponseEvaluated(questionId, r, score);
        BatchEvaluated b = getBatchEvaluated(questionId, re);
        questionDomains.add(q);
        questionsMap.put(questionId, q);
        responsesEvaluated.add(re);
        batchesEvaluated.add(b);
    }

    private BatchEvaluated getBatchEvaluated(Long questionId, ResponseEvaluated responseEvaluated) {
        final HashMap<Long, ResponseEvaluated> responsesEvaluated = new HashMap<>();
        responsesEvaluated.put(questionId, responseEvaluated);
        return new BatchEvaluated(responsesEvaluated, new ArrayList<>(),  100);
    }

    private QuestionDomain getQuestion(Long questionId, String question, ThemeDomain themeDomain) {
        QuestionDomain q = new QuestionMCQDomain();
        q.setQuestionId(questionId);
        q.setQuestion(question);
        q.setThemeDomain(themeDomain);
        return q;
    }

    private Response getResponse(Long questionId, Long answerId) {
        Response r = new ResponseMCQ(questionId, new HashSet<>(Arrays.asList(answerId)));
        return r;
    }

    private ThemeDomain getTheme(Long themeId, String theme) {
        ThemeDomain t = new ThemeDomain();
        t.setThemeId(themeId);
        t.setName(theme);
        return t;
    }

    private SchemeDomain getSchemeDomain() {
        SchemeDomain schemeDomain = new SchemeDomain();
        return schemeDomain;
    }
}