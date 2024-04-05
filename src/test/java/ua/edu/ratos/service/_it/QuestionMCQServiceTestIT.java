package ua.edu.ratos.service._it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionMCQInDto;
import ua.edu.ratos.service.dto.out.answer.AnswerMCQOutDto;
import ua.edu.ratos.service.dto.out.question.QuestionMCQOutDto;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionMCQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/question_mcq_in_dto_new.json";
    public static final String JSON_UPD = "classpath:json/question_mcq_in_dto_upd.json";

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveQuestionMcqTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        QuestionMCQInDto dto = objectMapper.readValue(json, QuestionMCQInDto.class);
        // Actual test begins
        QuestionMCQOutDto questionMCQOutDto = questionService.save(dto);
        assertThat("Found Question MCQ object is not as expected", questionMCQOutDto, allOf(
                hasProperty("questionId", equalTo(1L)),
                hasProperty("question", equalTo("Question #1")),
                hasProperty("level", equalTo((byte) 1)),
                hasProperty("answers", hasSize(4)),
                hasProperty("resource", nullValue()),
                hasProperty("help", notNullValue())
        ));
    }

    @Test
    @Sql(scripts = "/scripts/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/question_mcq_test_data.sql", "/scripts/question_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateQuestionMcqTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        QuestionMCQInDto dto = objectMapper.readValue(json, QuestionMCQInDto.class);
        // Actual test begins
        QuestionMCQOutDto updatedQuestionMcqOutDto = questionService.update(dto);
        assertThat("Updated Question MCQ object is not as expected", updatedQuestionMcqOutDto, allOf(
                hasProperty("questionId", equalTo(1L)),
                hasProperty("question", equalTo("Question #1 (upd)")), // Updated
                hasProperty("level", equalTo((byte) 2)), // Updated
                hasProperty("answers", hasSize(5)), // Added 2, removed 1
                hasProperty("resource", notNullValue()), // Updated
                hasProperty("help", nullValue()) // Removed
        ));
        List<AnswerMCQOutDto> answers = updatedQuestionMcqOutDto.getAnswers();
        AnswerMCQOutDto a1 = createAnswerMCQ("answer #22", 0, false);
        AnswerMCQOutDto a2 = createAnswerMCQ("answer #33", 0, false);
        AnswerMCQOutDto a3 = createAnswerMCQ("answer #44", 0, false);
        AnswerMCQOutDto a4 = createAnswerMCQ("answer #5", 0, false);
        AnswerMCQOutDto a5 = createAnswerMCQ("answer #6", 100, true);

        assertThat("Failed to find all the answers that are expected!", answers, containsInAnyOrder(a1, a2, a3, a4, a5));
    }

    private AnswerMCQOutDto createAnswerMCQ(String name, int percent, boolean required) {
        return new AnswerMCQOutDto()
                .setAnswer(name)
                .setPercent((short) percent)
                .setRequired(required)
                .setResource(null);
    }
}

