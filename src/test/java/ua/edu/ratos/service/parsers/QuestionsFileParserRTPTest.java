package ua.edu.ratos.service.parsers;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

public class QuestionsFileParserRTPTest {

    private static final String TYPICAL_CASE = "classpath:files/rtp/typical_case.rtp";

    private static final String TEST_CASE = "classpath:files/rtp/test_case.rtp";

    @Test
    public void parseFileOfRTPFormatTypicalCaseShouldYieldCorrectResultOf510QuestionsTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserRTP();
        File file = ResourceUtils.getFile(TYPICAL_CASE);
        QuestionsParsingResult result = parser.parseFile(file, "UTF-8");
        assertThat("Result of parsing object is not as expected", result, allOf(
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("header", is("")),
                hasProperty("questions", hasSize(equalTo(10))),
                hasProperty("issues", hasSize(equalTo(0))),
                hasProperty("invalid", equalTo(0))
        ));
    }

    @Test
    public void parseFileOfRTPFormatTestCaseShouldYieldCorrectResultOf5QuestionsTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserRTP();
        File file = ResourceUtils.getFile(TEST_CASE);
        QuestionsParsingResult result = parser.parseFile(file, "UTF-8");
        assertThat("Result of parsing object is not as expected", result, allOf(
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("header", not(is(""))),
                hasProperty("questions", hasSize(equalTo(5))),
                hasProperty("issues", hasSize(equalTo(2))),
                hasProperty("invalid", equalTo(1))
        ));
    }
}

