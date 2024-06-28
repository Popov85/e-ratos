package ua.edu.ratos.service.parsers;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class QuestionsFileParserTXTTest {

    private static final String TYPICAL_CASE = "classpath:files/txt/typical_case.txt";

    private static final String TEST_CASE = "classpath:files/txt/test_case.txt";

    private static final String WIN1251_CASE = "classpath:files/txt/win1251_case.txt";

    private static final String ISSUES_CASE = "classpath:files/txt/with_issues_case.txt";

    private static final String EMPTY_CASE = "classpath:files/txt/empty_case.txt";

    private static final String HEADER_CASE = "classpath:files/txt/header_case.txt";

    private static final String BOM_CASE = "classpath:files/txt/bom_case.txt";


    @Test
    public void parseFileOfTXTFormatShouldYieldCorrectResultOf10QuestionsTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
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
    public void parseFileOfTXTFormatShouldYieldCorrectResultOf3QuestionsTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
        File file = ResourceUtils.getFile(TEST_CASE);
        QuestionsParsingResult result = parser.parseFile(file, "UTF-8");
        assertThat("Result of parsing object is not as expected", result, allOf(
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("header", not(is(""))),
                hasProperty("questions", hasSize(equalTo(3))),
                hasProperty("issues", hasSize(equalTo(0))),
                hasProperty("invalid", equalTo(0))
        ));
    }

    @Test
    public void parseFileOfTXTFormatInWin1251ShouldYieldCorrectResultOf10QuestionsTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
        File file = ResourceUtils.getFile(WIN1251_CASE);
        QuestionsParsingResult result = parser.parseFile(file, "windows-1251");
        assertThat("Result of parsing object is not as expected", result, allOf(
                hasProperty("charset", equalTo("windows-1251")),
                hasProperty("header", is("")),
                hasProperty("questions", hasSize(equalTo(10))),
                hasProperty("issues", hasSize(equalTo(0))),
                hasProperty("invalid", equalTo(0))
        ));
    }

    @Test
    public void parseFileOfTXTFormatWithIssuesTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
        File file = ResourceUtils.getFile(ISSUES_CASE);
        QuestionsParsingResult result = parser.parseFile(file, "UTF-8");
        assertThat("Result of parsing object is not as expected", result, allOf(
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("header", not(is(""))),
                hasProperty("questions", hasSize(equalTo(3))),
                hasProperty("issues", hasSize(equalTo(5))),
                hasProperty("invalid", equalTo(3))
        ));
    }

    @Test
    public void parseFileOfTXTFormatWithBOMIssuesTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
        File file = ResourceUtils.getFile(BOM_CASE);
        QuestionsParsingResult result = parser.parseFile(file, "UTF-8");
        //System.out.println("Result = "+ result);
        assertThat("Result of parsing object is not as expected", result, allOf(
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("header", is("")),
                hasProperty("questions", hasSize(equalTo(1))),
                hasProperty("issues", hasSize(equalTo(0))),
                hasProperty("invalid", equalTo(0))
        ));
    }


    @Test
    public void parseFileOfTXTFormatEmptyShouldYieldExceptionTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
        File file = ResourceUtils.getFile(EMPTY_CASE);
        assertThrows(RuntimeException.class, () -> {
            parser.parseFile(file, "UTF-8");
        });
    }

    @Test
    public void parseFileOfTXTFormatOnlyHeaderShouldYieldExceptionTest() throws Exception {
        QuestionsFileParser parser = new QuestionsFileParserTXT();
        File file = ResourceUtils.getFile(HEADER_CASE);
        assertThrows(RuntimeException.class, () ->
                parser.parseFile(file, "UTF-8")
        );
    }
}

