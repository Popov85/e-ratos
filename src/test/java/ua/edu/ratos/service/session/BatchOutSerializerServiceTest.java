package ua.edu.ratos.service.session;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos._helper.QuestionGeneratorHelper;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

@JsonTest
@ExtendWith(SpringExtension.class)
public class BatchOutSerializerServiceTest {

    public static final String JSON = "/json/batch_out_dto_init.json";

    @Autowired
    private JacksonTester<BatchOutDto> json;

    QuestionGeneratorHelper qGH = new QuestionGeneratorHelper();

    @Test
    public void serializeTest() throws Exception {
        BatchOutDto batchOutDto = createBatch(createSequence());
        // actual test begins
        org.assertj.core.api.Assertions.assertThat(this.json.write(batchOutDto)).isEqualToJson(JSON);
    }

    @Test
    public void deserializeTest() throws Exception {
        ObjectContent<BatchOutDto> read = this.json.read(JSON);
        // actual test begins
        BatchOutDto batchOutDto = read.getObject();
        org.hamcrest.MatcherAssert.assertThat("Deserialized object is not equal", batchOutDto, allOf(
                hasProperty("lastBatch", equalTo(false)),
                hasProperty("questionsLeft", equalTo(20)),
                hasProperty("batchesLeft", equalTo(5)),
                hasProperty("sessionExpiresInSec", equalTo(500L)),
                hasProperty("batchExpiresInSec", equalTo(50L)),
                hasProperty("currentScore", equalTo("24.5")),
                hasProperty("effectiveScore", equalTo("100.0")),
                hasProperty("progress", equalTo("50.0")),
                hasProperty("motivationalMessage", equalTo("Well-done!")),
                hasProperty("questions", IsCollectionWithSize.hasSize(5))
        ));
    }


    private BatchOutDto createBatch(List<QuestionSessionOutDto> sequence) {
        BatchOutDto batchOutDto = BatchOutDto.createRegular(sequence, false);
        batchOutDto.setQuestionsLeft(20);
        batchOutDto.setBatchesLeft(5);
        batchOutDto.setSessionExpiresInSec(500L);
        batchOutDto.setBatchExpiresInSec(50L);
        batchOutDto.setCurrentScore("24.5");
        batchOutDto.setEffectiveScore("100.0");
        batchOutDto.setProgress("50.0");
        batchOutDto.setMotivationalMessage("Well-done!");
        return batchOutDto;
    }

    private List<QuestionSessionOutDto> createSequence() {
        return Arrays.asList(
                qGH.createMCQ(1L, "QuestionMultiple Choice #1").toDto(),
                qGH.createFBSQ(2L, "Question Fill Blank Single #2").toDto(),
                qGH.createFBMQ(3L, "Question Fill Blank Multiple #3", false).toDto(),
                qGH.createMQ(4L, "Matcher Question #4", false).toDto(),
                qGH.createSQ(5L, "Sequence Question #5").toDto());
    }

}
