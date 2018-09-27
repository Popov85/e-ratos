package ua.edu.ratos.service.sequence;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import ua.edu.ratos.domain.entity.SchemeTheme;
import ua.edu.ratos.domain.entity.SchemeThemeSettings;
import ua.edu.ratos.domain.entity.question.*;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.sequence.SequenceMapper;
import ua.edu.ratos.service.utils.CollectionShuffler;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;;
import static org.mockito.Mockito.*;

/**
 * @link https://www.slideshare.net/nunafig/mockito-12079903
 * @link https://www.mkyong.com/unittest/junit-how-to-test-a-map/
 */
@RunWith(MockitoJUnitRunner.class)
public class SequenceMapperTest {

    @Mock
    private QuestionService questionService;

    @Spy
    private CollectionShuffler collectionShuffler;

    @Mock
    private SchemeTheme schemeTheme;

    @InjectMocks
    private SequenceMapper sm = new SequenceMapper();

    private Set<SchemeThemeSettings> settings;

    private Set<QuestionMultipleChoice> questionsType1;

    private Set<QuestionFillBlankSingle> questionsType2;

    private Set<QuestionSequence> questionsType5;

    private List<QuestionType> types;

    @Before
    public void init() {
        QuestionType t1 = createType(1L);
        QuestionType t2 = createType(2L);
        QuestionType t5 = createType(5L);
        types=Arrays.asList(t1, t2, t5);

        SchemeThemeSettings set1 = createSettings(1L, types.get(0), (short)3, (short)2, (short)2);
        SchemeThemeSettings set2 = createSettings(1L, types.get(1), (short)1, (short)1, (short)1);
        SchemeThemeSettings set3 = createSettings(1L, types.get(2), (short)3, (short)0, (short)0);
        settings = new HashSet<>();
        settings.addAll(Arrays.asList(set1, set2, set3));

        final QuestionMultipleChoice q11 = createQuestionType1(1L, (byte) 1, "MCQ11");
        final QuestionMultipleChoice q12 = createQuestionType1(2L, (byte) 1, "MCQ12");
        final QuestionMultipleChoice q13 = createQuestionType1(3L, (byte) 1, "MCQ13");

        final QuestionMultipleChoice q14 = createQuestionType1(4L, (byte) 2, "MCQ14");
        final QuestionMultipleChoice q15 = createQuestionType1(5L, (byte) 2, "MCQ15");
        final QuestionMultipleChoice q16 = createQuestionType1(6L, (byte) 2, "MCQ16");

        final QuestionMultipleChoice q17 = createQuestionType1(7L, (byte) 3, "MCQ17");
        final QuestionMultipleChoice q18 = createQuestionType1(8L, (byte) 3, "MCQ18");
        final QuestionMultipleChoice q19 = createQuestionType1(9L, (byte) 3, "MCQ19");
        questionsType1 = new HashSet<>();
        questionsType1.addAll(Arrays.asList(q11, q12, q13, q14, q15, q16, q17, q18, q19));

        final QuestionFillBlankSingle q21 = createQuestionType2(10L, (byte) 1, "FBSQ21");
        final QuestionFillBlankSingle q22 = createQuestionType2(11L, (byte) 1, "FBSQ22");
        final QuestionFillBlankSingle q23 = createQuestionType2(12L, (byte) 1, "FBSQ23");

        final QuestionFillBlankSingle q24 = createQuestionType2(13L, (byte) 2, "FBSQ24");
        final QuestionFillBlankSingle q25 = createQuestionType2(14L, (byte) 2, "FBSQ25");
        final QuestionFillBlankSingle q26 = createQuestionType2(15L, (byte) 2, "FBSQ26");

        final QuestionFillBlankSingle q27 = createQuestionType2(16L, (byte) 3, "FBSQ27");
        final QuestionFillBlankSingle q28 = createQuestionType2(17L, (byte) 3, "FBSQ28");
        final QuestionFillBlankSingle q29 = createQuestionType2(18L, (byte) 3, "FBSQ29");
        questionsType2 = new HashSet<>();
        questionsType2.addAll(Arrays.asList(q21, q22, q23, q24, q25, q26, q27, q28, q29));

        final QuestionSequence q51 = createQuestionType5(19L, (byte) 1, "SQ31");
        final QuestionSequence q52 = createQuestionType5(20L, (byte) 1, "SQ32");
        final QuestionSequence q53 = createQuestionType5(21L, (byte) 1, "SQ33");

        final QuestionSequence q54 = createQuestionType5(23L, (byte) 2, "SQ34");
        final QuestionSequence q55 = createQuestionType5(24L, (byte) 2, "SQ35");
        final QuestionSequence q56 = createQuestionType5(25L, (byte) 2, "SQ36");

        final QuestionSequence q57 = createQuestionType5(26L, (byte) 3, "SQ37");
        final QuestionSequence q58 = createQuestionType5(27L, (byte) 3, "SQ38");
        final QuestionSequence q59 = createQuestionType5(28L, (byte) 3, "SQ39");
        questionsType5 = new HashSet<>();
        questionsType5.addAll(Arrays.asList(q51, q52, q53, q54, q55, q56, q57, q58, q59));
    }


    @Test
    public void getMapTest() {
        Mockito.<Set<? extends Question>>when(questionService.findAllByThemeIdAndTypeId(1L, 1L)).thenReturn(questionsType1);
        Mockito.<Set<? extends Question>>when(questionService.findAllByThemeIdAndTypeId(1L, 2L)).thenReturn(questionsType2);
        Mockito.<Set<? extends Question>>when(questionService.findAllByThemeIdAndTypeId(1L, 5L)).thenReturn(questionsType5);

        final Map<Long, Map<Byte, List<Question>>> map = sm.getMap(1L, settings);

        verify(questionService, times(1)).findAllByThemeIdAndTypeId(1L, 1L);
        verify(questionService, times(1)).findAllByThemeIdAndTypeId(1L, 2L);
        verify(questionService, times(1)).findAllByThemeIdAndTypeId(1L, 5L);

        assertThat(map.size(), CoreMatchers.is(3));

        assertThat(map, IsMapContaining.hasKey(1L));
        assertThat(map, IsMapContaining.hasKey(2L));
        assertThat(map, IsMapContaining.hasKey(5L));

        final Map<Byte, List<Question>> type1Map = map.get(1L);

        assertThat(type1Map.size(), CoreMatchers.is(3));
        assertThat(type1Map, IsMapContaining.hasKey((byte)1));
        assertThat(type1Map, IsMapContaining.hasKey((byte)2));
        assertThat(type1Map, IsMapContaining.hasKey((byte)3));
        assertEquals(3, type1Map.get((byte)1).size());
        assertEquals(2, type1Map.get((byte)2).size());
        assertEquals(2, type1Map.get((byte)3).size());

        final Map<Byte, List<Question>> type2Map = map.get(2L);

        assertThat(type2Map.size(), CoreMatchers.is(3));
        assertThat(type2Map, IsMapContaining.hasKey((byte)1));
        assertThat(type2Map, IsMapContaining.hasKey((byte)2));
        assertThat(type2Map, IsMapContaining.hasKey((byte)3));
        assertEquals(1, type2Map.get((byte)1).size());
        assertEquals(1, type2Map.get((byte)2).size());
        assertEquals(1, type2Map.get((byte)3).size());

        final Map<Byte, List<Question>> type5Map = map.get(5L);

        assertThat(type5Map.size(), CoreMatchers.is(1));
        assertThat(type5Map, IsMapContaining.hasKey((byte)1));
        assertEquals(3, type5Map.get((byte)1).size());
    }

    @Test
    public void getListTest() {
        Mockito.<Set<? extends Question>>when(questionService.findAllByThemeIdAndTypeId(1L, 1L)).thenReturn(questionsType1);
        Mockito.<Set<? extends Question>>when(questionService.findAllByThemeIdAndTypeId(1L, 2L)).thenReturn(questionsType2);
        Mockito.<Set<? extends Question>>when(questionService.findAllByThemeIdAndTypeId(1L, 5L)).thenReturn(questionsType5);

        final List<Question> list = sm.getList(1L, settings);

        verify(questionService, times(1)).findAllByThemeIdAndTypeId(1L, 1L);
        verify(questionService, times(1)).findAllByThemeIdAndTypeId(1L, 2L);
        verify(questionService, times(1)).findAllByThemeIdAndTypeId(1L, 5L);

        assertEquals(13, list.size());

    }


    private QuestionType createType(Long typeId) {
        QuestionType t1 = new QuestionType();
        t1.setTypeId(typeId);
        return t1;
    }

    private SchemeThemeSettings createSettings(Long id, QuestionType type, short l1, short l2, short l3) {
        SchemeThemeSettings set = new SchemeThemeSettings();
        set.setSchemeThemeSettingsId(id);
        set.setSchemeTheme(schemeTheme);
        set.setType(type);
        set.setLevel1(l1);
        set.setLevel2(l2);
        set.setLevel3(l3);
        return set;
    }

    private QuestionMultipleChoice createQuestionType1(Long id, byte level, String question) {
        QuestionMultipleChoice q = new QuestionMultipleChoice();
        q.setQuestionId(id);
        q.setType(types.get(0));
        q.setLevel(level);
        q.setQuestion(question);
        return q;
    }

    private QuestionFillBlankSingle createQuestionType2(Long id, byte level, String question) {
        QuestionFillBlankSingle q = new QuestionFillBlankSingle();
        q.setQuestionId(id);
        q.setType(types.get(1));
        q.setLevel(level);
        q.setQuestion(question);
        return q;
    }

    private QuestionSequence createQuestionType5(Long id, byte level, String question) {
        QuestionSequence q = new QuestionSequence();
        q.setQuestionId(id);
        q.setType(types.get(2));
        q.setLevel(level);
        q.setQuestion(question);
        return q;
    }
}