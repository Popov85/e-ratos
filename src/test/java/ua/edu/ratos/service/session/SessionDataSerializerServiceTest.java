package ua.edu.ratos.service.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos._helper.QuestionGeneratorHelper;
import ua.edu.ratos.service.domain.*;
import ua.edu.ratos.service.domain.question.*;
import ua.edu.ratos.service.domain.response.*;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class SessionDataSerializerServiceTest extends QuestionGeneratorHelper {

    public static final String JSON_INIT_NO_BATCH = "classpath:json/session_data_init_no_batch.json";

    public static final String JSON_INIT_BATCH = "classpath:json/session_data_init_batch.json";

    public static final String JSON_INTERMEDIATE = "classpath:json/session_data_intermediate.json";

    public static final String JSON_TERMINAL = "classpath:json/session_data_terminal.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private SchemeDomain schemeDomain;

    @BeforeAll
    public static void setUp() {
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME);
        objectMapper.registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class, localDateTimeDeserializer));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @BeforeEach
    public void init() {
        // Some tests modify scheme's state(!)
        schemeDomain = new SchemeDomain();
        schemeDomain.setSchemeId(1L);
        schemeDomain.setName("SchemeDomain#1");
        schemeDomain.setModeDomain(new ModeDomain().setModeId(1L).setName("ModeDomain#1"));
        schemeDomain.setSettingsDomain(
                new SettingsDomain()
                        .setSetId(1L)
                        .setName("SettingsDomain#1")
                        .setDaysKeepResultDetails((short) 1)
                        .setLevel2Coefficient(1)
                        .setLevel3Coefficient(1)
                        .setQuestionsPerSheet((short) 5)
                        .setSecondsPerQuestion(-1)
                        .setStrictControlTimePerQuestion(false));
        schemeDomain.setStrategyDomain(new StrategyDomain().setStrId(1L).setName("StrategyDomain#1"));
        schemeDomain.setGradingDomain(new GradingDomain().setGradingId(1L).setName("GradingDomain #1"));
        schemeDomain.setOptionsDomain(new OptionsDomain().setOptId(1L).setName("OptionsDomain #1"));
    }

    @Test
    public void serializeInitStateNoCurrentBatchTest() throws Exception {
        // Given: SessionData object being preserved by a user at some point of its life-cycle (initial state, no batch)
        // We use custom class to transform it into JSON format
        // Check if this JSON string is equal to the expected one, located in a pre-defined file
        SessionData sessionData = SessionData.createNoLMS(1L, schemeDomain, List.of(
                createMCQ(1L, "QuestionMultiple Choice #1"),
                createFBSQ(2L, "Question Fill Blank Single #2"),
                createFBMQ(3L, "Question Fill Blank Multiple #3", false),
                createMQ(4L, "Matcher Question #4", false),
                createSQ(5L, "Sequence Question #5")));
        sessionData.setSessionTimeout(LocalDateTime.MAX);
        sessionData.setCurrentBatchTimeout(LocalDateTime.MAX);
        sessionData.setCurrentBatchIssued(LocalDateTime.parse("2018-03-22 11:30:40",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sessionData.setCurrentIndex(0);
        sessionData.setSuspended(true);
        // Actual test begins
        // Read expected file into string
        File json = ResourceUtils.getFile(JSON_INIT_NO_BATCH);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());
        // Serialize with our service bean into JSON string
        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        String actual = serializer.serialize(sessionData);
        // Compare two strings
        assertEquals("Actual JSON string is different from what is expected", expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void deserializeInitStateNoCurrentBatchTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_INIT_NO_BATCH);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());
        //Actual test begins
        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        SessionData sessionData = serializer.deserialize(expected);
        assertThat("Deserialized SessionData object is not as expected", sessionData, allOf(
                hasProperty("userId", equalTo(1L)),
                hasProperty("schemeDomain", is(notNullValue())),
                hasProperty("sequence", hasSize(5)),
                hasProperty("questionsMap", allOf(
                        hasKey(1L), hasKey(2L), hasKey(3L), hasKey(4L), hasKey(5L))),
                hasProperty("lmsId", equalTo(Optional.empty())),
                hasProperty("sessionTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentBatchTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentIndex", equalTo(0)),
                hasProperty("currentBatch", equalTo(Optional.empty())),
                hasProperty("currentBatchIssued", equalTo(LocalDateTime.parse("2018-03-22T11:30:40"))),
                hasProperty("progressData", allOf(
                        hasProperty("score", equalTo(0.0)),
                        hasProperty("progress", equalTo(0.0)),
                        hasProperty("timeSpent", equalTo(0L)),
                        hasProperty("batchesEvaluated", empty()))),
                hasProperty("metaData", is(notNullValue())),
                hasProperty("suspended", equalTo(true))
        ));
    }

    @Test
    public void serializeInitStateWith5DifferentQuestionsNoLmsTest() throws Exception {
        // Given: SessionData object being preserved by a user at initial state, first and single batch of 5 questions
        // We use custom class to transform it into JSON format
        // Check if this JSON string is equal to the expected one, located in a pre-defined file
        List<QuestionDomain> sequence = List.of(
                createMCQ(1L, "QuestionMultiple Choice #1"),
                createFBSQ(2L, "Question Fill Blank Single #2"),
                createFBMQ(3L, "Question Fill Blank Multiple #3", false),
                createMQ(4L, "Matcher Question #4", false),
                createSQ(5L, "Sequence Question #5"));

        SessionData sessionData = SessionData.createNoLMS(1L, schemeDomain, sequence);
        BatchOutDto batchOutDto = BatchOutDto.createRegular(
                sequence.stream().map(QuestionDomain::toDto).collect(Collectors.toList()), true);
        batchOutDto.setBatchExpiresInSec(null);
        batchOutDto.setSessionExpiresInSec(500L);
        batchOutDto.setQuestionsLeft(0);
        batchOutDto.setBatchesLeft(0);
        batchOutDto.setProgress("0");
        batchOutDto.setCurrentScore("0");
        batchOutDto.setEffectiveScore("0");
        batchOutDto.setMotivationalMessage(null);
        sessionData.setCurrentBatch(batchOutDto);
        sessionData.setSessionTimeout(LocalDateTime.MAX);
        sessionData.setCurrentBatchTimeout(LocalDateTime.MAX);
        sessionData.setCurrentBatchIssued(LocalDateTime.parse("2018-03-22 11:30:40",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sessionData.setCurrentIndex(5);
        sessionData.setSuspended(true);

        // Actual test begins
        File json = ResourceUtils.getFile(JSON_INIT_BATCH);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());

        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        String actual = serializer.serialize(sessionData);
        assertEquals("Actual JSON string is different from what is expected", expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void deserializeInitStateWith5DifferentQuestionsNoLmsTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_INIT_BATCH);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());
        // Actual test begins
        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        SessionData sessionData = serializer.deserialize(expected);
        assertThat("Deserialized SessionData object is not as expected", sessionData, allOf(
                hasProperty("userId", equalTo(1L)),
                hasProperty("schemeDomain", is(notNullValue())),
                hasProperty("sequence", hasSize(5)),
                hasProperty("questionsMap", allOf(
                        hasKey(1L), hasKey(2L), hasKey(3L), hasKey(4L), hasKey(5L))),
                hasProperty("lmsId", equalTo(Optional.empty())),
                hasProperty("sessionTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentBatchTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentIndex", equalTo(5)),
                hasProperty("currentBatchOrNull", allOf(
                        hasProperty("questions", hasSize(5)),
                        hasProperty("lastBatch", equalTo(true)),
                        hasProperty("batchMap", is(notNullValue())),
                        hasProperty("questionsLeft", equalTo(0)),
                        hasProperty("batchesLeft", equalTo(0)),
                        hasProperty("sessionExpiresInSec", equalTo(500L)),
                        hasProperty("currentScore", equalTo("0")),
                        hasProperty("effectiveScore", equalTo("0")),
                        hasProperty("progress", equalTo("0")))),
                hasProperty("currentBatchIssued", equalTo(LocalDateTime.parse("2018-03-22T11:30:40"))),
                hasProperty("progressData", allOf(
                        hasProperty("score", equalTo(0.0)),
                        hasProperty("actualScore", equalTo(0.0)),
                        hasProperty("progress", equalTo(0.0)),
                        hasProperty("timeSpent", equalTo(0L)),
                        hasProperty("batchesEvaluated", empty()))),
                hasProperty("metaData", is(notNullValue())),
                hasProperty("suspended", equalTo(true))
        ));
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void serializeInterStateWith1QuestionPerBatchWithLmsTest() throws Exception {
        // Given: typical use case (1 question ber batch, limited in time, preserved in the middle of the test)
        QuestionMCQDomain mcq = createMCQ(1L, "Question Multiple Choice #1");
        QuestionFBSQDomain fbsq = createFBSQ(2L, "Question Fill Blank Single #2");
        QuestionFBMQDomain fbmq = createFBMQ(3L, "Question Fill Blank Multiple #3", false);
        QuestionMQDomain mq = createMQ(4L, "Matcher Question #4", false);
        QuestionSQDomain sq = createSQ(5L, "Sequence Question #5");
        List<QuestionDomain> sequence = List.of(mcq, fbsq, fbmq, mq, sq);
        // Adjust value for this particular test
        schemeDomain.getSettingsDomain().setQuestionsPerSheet((short) 1);

        SessionData sessionData = SessionData.createFromLMS(1L, 1L, schemeDomain, sequence);

        // Last but one question in the current batch
        BatchOutDto batchOutDto = BatchOutDto.createRegular(List.of(mq.toDto()), false);
        batchOutDto.setBatchExpiresInSec(60L);
        batchOutDto.setSessionExpiresInSec(300L);
        batchOutDto.setQuestionsLeft(1);
        batchOutDto.setBatchesLeft(1);
        batchOutDto.setProgress("80");
        batchOutDto.setCurrentScore("50");
        batchOutDto.setEffectiveScore("60");
        batchOutDto.setMotivationalMessage("Well-done!");

        sessionData.setCurrentBatch(batchOutDto);
        sessionData.setSessionTimeout(LocalDateTime.MAX);
        sessionData.setCurrentBatchTimeout(LocalDateTime.MAX);
        sessionData.setCurrentBatchIssued(LocalDateTime.parse("2018-03-22 11:30:40",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sessionData.setCurrentIndex(4);
        sessionData.setSuspended(true);

        ProgressData progressData = new ProgressData();
        progressData.setProgress(0.8);
        progressData.setTimeSpent(200);
        progressData.setActualScore(60);
        progressData.setScore(5);

        ResponseEvaluated r0 = new ResponseEvaluated(1L, new ResponseMCQ(
                1L, Set.of(1L)), 100, false);

        BatchEvaluated b0 = new BatchEvaluated(List.of(r0), 30, false);

        ResponseEvaluated r1 = new ResponseEvaluated(2L,
                new ResponseFBSQ(2L, "wrong phraseDomain"), 0, false);

        BatchEvaluated b1 = new BatchEvaluated(List.of(r1), 50, false);

        ResponseEvaluated r2 = new ResponseEvaluated(3L, new ResponseFBMQ(3L, Set.of(
                new ResponseFBMQ.Pair(1L, "(correct) Target phraseDomain for FBMQ answer #1"),
                new ResponseFBMQ.Pair(2L, "(correct) Target phraseDomain for FBMQ answer #2"),
                new ResponseFBMQ.Pair(3L, "(correct) Target phraseDomain for FBMQ answer #3"),
                new ResponseFBMQ.Pair(4L, "(correct) Target phraseDomain for FBMQ answer #4")
        )), 100, false);

        BatchEvaluated b2 = new BatchEvaluated(List.of(r2), 25, false);

        progressData.setBatchesEvaluated(List.of(b0, b1, b2));

        sessionData.setProgressData(progressData);

        Map<Long, MetaData> metaDataMap = new HashMap<>();
        MetaData metaL1 = new MetaData();
        metaL1.setIncorrect((short) 0).setSkipped((short) 0).setHelp((short) 0);
        metaDataMap.put(1L, metaL1);

        MetaData metaL2 = new MetaData();
        metaL2.setIncorrect((short) 1).setSkipped((short) 0).setHelp((short) 1);
        metaDataMap.put(2L, metaL2);

        MetaData metaL3 = new MetaData();
        metaL3.setIncorrect((short) 0).setSkipped((short) 1).setHelp((short) 1);
        metaDataMap.put(3L, metaL3);

        sessionData.setMetaData(metaDataMap);

        // Actual test begins
        File json = ResourceUtils.getFile(JSON_INTERMEDIATE);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());

        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        String actual = serializer.serialize(sessionData);
        assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void deserializeInterStateWith1QuestionPerBatchWithLmsTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_INTERMEDIATE);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());
        // Actual test begins
        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        SessionData sessionData = serializer.deserialize(expected);

        assertThat("Deserialized SessionData object is not as expected", sessionData, allOf(
                hasProperty("userId", equalTo(1L)),
                hasProperty("schemeDomain", is(notNullValue())),
                hasProperty("sequence", hasSize(5)),
                hasProperty("questionsMap", allOf(
                        hasKey(1L), hasKey(2L), hasKey(3L), hasKey(4L), hasKey(5L))),
                hasProperty("lmsId", equalTo(Optional.of(1L))),
                hasProperty("sessionTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentBatchTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentIndex", equalTo(4)),
                hasProperty("currentBatchOrNull", allOf(
                        hasProperty("questions", hasSize(1)),
                        hasProperty("lastBatch", equalTo(false)),
                        hasProperty("batchMap", is(notNullValue())),
                        hasProperty("questionsLeft", equalTo(1)),
                        hasProperty("batchesLeft", equalTo(1)),
                        hasProperty("sessionExpiresInSec", equalTo(300L)),
                        hasProperty("batchExpiresInSec", equalTo(60L)),
                        hasProperty("currentScore", equalTo("50")),
                        hasProperty("effectiveScore", equalTo("60")),
                        hasProperty("progress", equalTo("80")),
                        hasProperty("motivationalMessage", equalTo("Well-done!"))
                )),
                hasProperty("currentBatchIssued", equalTo(LocalDateTime.parse("2018-03-22T11:30:40"))),
                hasProperty("progressData", allOf(
                        hasProperty("score", equalTo(5.0)),
                        hasProperty("actualScore", equalTo(60.0)),
                        hasProperty("progress", equalTo(0.8)),
                        hasProperty("timeSpent", equalTo(200L)),
                        hasProperty("batchesEvaluated", hasSize(3))
                )),
                hasProperty("metaData", is(notNullValue())), // Actually a map with 3 keys (cannot check due to the hamcrest limitations)
                hasProperty("suspended", equalTo(true))
        ));
    }


    @Test
    public void serializeTerminalStateWith1QuestionPerBatchWithLmsTest() throws Exception {
        // Given: typical use case (1 question ber batch, limited in time, preserved at the end of the session to keep for some time before auto removal)
        QuestionMCQDomain mcq = createMCQ(1L, "Question Multiple Choice #1");
        QuestionFBSQDomain fbsq = createFBSQ(2L, "Question Fill Blank Single #2");
        QuestionFBMQDomain fbmq = createFBMQ(3L, "Question Fill Blank Multiple #3", false);
        QuestionMQDomain mq = createMQ(4L, "Matcher Question #4", false);
        QuestionSQDomain sq = createSQ(5L, "Sequence Question #5");
        List<QuestionDomain> sequence = List.of(mcq, fbsq, fbmq, mq, sq);
        // Adjust value for this particular test
        schemeDomain.getSettingsDomain().setQuestionsPerSheet((short) 1);

        SessionData sessionData = SessionData.createFromLMS(1L, 1L, schemeDomain, sequence);
        sessionData.setSessionTimeout(LocalDateTime.MAX);
        // No current batches at the end
        sessionData.setCurrentBatch(null);
        sessionData.setCurrentBatchTimeout(null);
        sessionData.setCurrentBatchIssued(null);
        sessionData.setCurrentIndex(5);
        sessionData.setSuspended(false);

        ProgressData progressData = new ProgressData();
        progressData.setProgress(1.0);
        progressData.setTimeSpent(499);
        progressData.setActualScore(90);
        progressData.setScore(9);

        ResponseEvaluated r0 = new ResponseEvaluated(1L,
                new ResponseMCQ(1L, Set.of(1L)), 100, false);
        BatchEvaluated b0 = new BatchEvaluated(List.of(r0), 30, false);

        ResponseEvaluated r1 = new ResponseEvaluated(2L,
                new ResponseFBSQ(2L, "wrong phraseDomain"), 0, false);
        BatchEvaluated b1 = new BatchEvaluated(List.of(r1), 50, false);

        ResponseEvaluated r2 = new ResponseEvaluated(3L, new ResponseFBMQ(3L, Set.of(
                new ResponseFBMQ.Pair(1L, "(correct) Target phraseDomain for FBMQ answer #1"),
                new ResponseFBMQ.Pair(2L, "(correct) Target phraseDomain for FBMQ answer #2"),
                new ResponseFBMQ.Pair(3L, "(correct) Target phraseDomain for FBMQ answer #3"),
                new ResponseFBMQ.Pair(4L, "(correct) Target phraseDomain for FBMQ answer #4")
        )), 100, false);
        BatchEvaluated b2 = new BatchEvaluated(List.of(r2), 25, false);

        ResponseEvaluated r3 = new ResponseEvaluated(4L, new ResponseMQ(4L, Set.of(
                new ResponseMQ.Triple(1L, 21L, 22L),
                new ResponseMQ.Triple(2L, 23L, 24L),
                new ResponseMQ.Triple(3L, 25L, 26L),
                new ResponseMQ.Triple(4L, 27L, 28L)
        )), 100, false);
        BatchEvaluated b3 = new BatchEvaluated(List.of(r3), 30, false);

        ResponseEvaluated r4 = new ResponseEvaluated(5L,
                new ResponseSQ(5L, List.of(29L, 30L, 31L, 32L, 33L)), 100, false);
        BatchEvaluated b4 = new BatchEvaluated(List.of(r4), 15, false);

        progressData.setBatchesEvaluated(List.of(b0, b1, b2, b3, b4));

        sessionData.setProgressData(progressData);

        Map<Long, MetaData> metaDataMap = new HashMap<>();
        MetaData metaL1 = new MetaData();
        metaL1.setIncorrect((short) 0).setSkipped((short) 0).setHelp((short) 0);
        metaDataMap.put(1L, metaL1);

        MetaData metaL2 = new MetaData();
        metaL2.setIncorrect((short) 1).setSkipped((short) 0).setHelp((short) 1);
        metaDataMap.put(2L, metaL2);

        MetaData metaL3 = new MetaData();
        metaL3.setIncorrect((short) 0).setSkipped((short) 0).setHelp((short) 0);
        metaDataMap.put(3L, metaL3);

        MetaData metaL4 = new MetaData();
        metaL4.setIncorrect((short) 0).setSkipped((short) 0).setHelp((short) 1);
        metaDataMap.put(4L, metaL4);

        MetaData metaL5 = new MetaData();
        metaL5.setIncorrect((short) 0).setSkipped((short) 0).setHelp((short) 1);
        metaDataMap.put(5L, metaL5);

        sessionData.setMetaData(metaDataMap);

        // Actual test begins
        File json = ResourceUtils.getFile(JSON_TERMINAL);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());
        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        String actual = serializer.serialize(sessionData);
        assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }


    @Test
    public void deserializeTerminalStateWith1QuestionPerBatchWithLmsTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_TERMINAL);
        byte[] encoded = Files.readAllBytes(json.toPath());
        String expected = new String(encoded, Charset.defaultCharset());
        SessionDataSerializerService serializer = new SessionDataSerializerService(objectMapper);
        SessionData sessionData = serializer.deserialize(expected);
        assertThat("Deserialized SessionData object is not as expected", sessionData, allOf(
                hasProperty("userId", equalTo(1L)),
                hasProperty("schemeDomain", is(notNullValue())),
                hasProperty("sequence", hasSize(5)),
                hasProperty("questionsMap", allOf(
                        hasKey(1L), hasKey(2L), hasKey(3L), hasKey(4L), hasKey(5L))),
                hasProperty("lmsId", equalTo(Optional.of(1L))),
                hasProperty("sessionTimeout", equalTo(LocalDateTime.MAX)),
                hasProperty("currentBatchTimeout", is(nullValue())),
                hasProperty("currentIndex", equalTo(5)),
                hasProperty("currentBatchOrNull", is(nullValue())),
                hasProperty("currentBatchIssued", is(nullValue())),
                hasProperty("progressData", allOf(
                        hasProperty("score", equalTo(9.0)),
                        hasProperty("actualScore", equalTo(90.0)),
                        hasProperty("progress", equalTo(1.0)),
                        hasProperty("timeSpent", equalTo(499L)),
                        hasProperty("batchesEvaluated", hasSize(5))
                )),
                hasProperty("metaData", is(notNullValue())), // Actually a map with 5 keys (cannot check due to the hamcrest limitations)
                hasProperty("suspended", equalTo(false))
        ));
    }

}
