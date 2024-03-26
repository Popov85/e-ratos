package ua.edu.ratos.service._it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.service.QuestionsFileParserService;
import ua.edu.ratos.service.dto.out.QuestionsParsingResultOutDto;

import javax.persistence.EntityManager;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

/**
 * @link https://stackoverflow.com/questions/21800726/using-spring-mvc-test-to-unit-test-multipart-post-request
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionsFileParserServiceTestIT {

    private static final String TYPICAL_CASE_TXT = "classpath:files/txt/typical_case.txt";
    private static final String TYPICAL_CASE_RTP = "classpath:files/rtp/typical_case.rtp";
    private static final String WIN1251_CASE = "classpath:files/txt/win1251_case.txt";
    private static final String WITH_ISSUES_CASE = "classpath:files/txt/with_issues_case.txt";

    private static final String FIND = "select q from Question q";

    @Autowired
    private EntityManager em;

    @Autowired
    private QuestionsFileParserService questionsFileParserService;

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void parseAndSaveTXTFileNoIssuesTest() throws Exception {
        File file = ResourceUtils.getFile(TYPICAL_CASE_TXT);
        final byte[] bytes = Files.readAllBytes(file.toPath());
        MockMultipartFile mock = new MockMultipartFile("mock", "questions.txt", "text/plain", bytes);
        // Actual test begins
        final QuestionsParsingResultOutDto result = questionsFileParserService.parseAndSave(mock, 1L, false);
        assertThat("Result of saving object is not as expected", result, allOf(
                    hasProperty("questions", equalTo(10)),
                    hasProperty("issues", equalTo(0)),
                    hasProperty("charset", equalTo("UTF-8")),
                    hasProperty("majorIssues", equalTo(0)),
                    hasProperty("mediumIssues", equalTo(0)),
                    hasProperty("minorIssues", equalTo(0)),
                    hasProperty("allIssues", empty()),
                    hasProperty("saved", equalTo(true))
                ));
        assertThat("Questions quantity in DB is not 10", em.createQuery(FIND).getResultList().size(), equalTo(10));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void parseAndSaveRTPFileNoIssuesTest() throws Exception {
        File file = ResourceUtils.getFile(TYPICAL_CASE_RTP);
        final byte[] bytes = Files.readAllBytes(file.toPath());
        MockMultipartFile mock = new MockMultipartFile("mock", "questions.rtp", "text/plain", bytes);
        // Actual test begins
        final QuestionsParsingResultOutDto result = questionsFileParserService.parseAndSave(mock, 1L, true);
        assertThat("Result of saving object is not as expected", result, allOf(
                    hasProperty("questions", equalTo(10)),
                    hasProperty("issues", equalTo(0)),
                    hasProperty("charset", equalTo("UTF-8")),
                    hasProperty("majorIssues", equalTo(0)),
                    hasProperty("mediumIssues", equalTo(0)),
                    hasProperty("minorIssues", equalTo(0)),
                    hasProperty("allIssues", empty()),
                    hasProperty("saved", equalTo(true))
                ));
        assertThat("Questions quantity in DB is not 10", em.createQuery(FIND).getResultList().size(), equalTo(10));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void parseAndSaveTXT1251EncodedFileNoIssuesTest() throws Exception {
        File file = ResourceUtils.getFile(WIN1251_CASE);
        final byte[] bytes = Files.readAllBytes(file.toPath());
        MockMultipartFile mock = new MockMultipartFile("mock", "questions_windows_1251.txt", "text/plain", bytes);
        // Actual test begins
        final QuestionsParsingResultOutDto result = questionsFileParserService.parseAndSave(mock, 1L, false);
        assertThat("Result of saving object is not as expected", result, allOf(
                hasProperty("questions", equalTo(10)),
                hasProperty("issues", equalTo(0)),
                hasProperty("charset", equalTo("WINDOWS-1251")),
                hasProperty("majorIssues", equalTo(0)),
                hasProperty("mediumIssues", equalTo(0)),
                hasProperty("minorIssues", equalTo(0)),
                hasProperty("allIssues", empty()),
                hasProperty("saved", equalTo(true))
        ));
        assertThat("Questions quantity in DB is not 10", em.createQuery(FIND).getResultList().size(), equalTo(10));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void parseAndSaveTXTFileWithIssuesNotConfirmedTest() throws Exception {
        File file = ResourceUtils.getFile(WITH_ISSUES_CASE);
        final byte[] bytes = Files.readAllBytes(file.toPath());
        MockMultipartFile mock = new MockMultipartFile("mock", "questions.txt", "text/plain", bytes);
        // Actual test begins
        final QuestionsParsingResultOutDto result = questionsFileParserService.parseAndSave(mock, 1L, false);
        assertThat("Result of saving object is not as expected", result, allOf(
                hasProperty("questions", equalTo(3)),
                hasProperty("issues", equalTo(5)),
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("majorIssues", equalTo(5)),
                hasProperty("mediumIssues", equalTo(0)),
                hasProperty("minorIssues", equalTo(0)),
                hasProperty("allIssues", hasSize(5)),
                hasProperty("saved", equalTo(false))
        ));
        assertThat("Questions from non-confirmed file with issues must not be saved into DB", (List<Question>)em.createQuery(FIND).getResultList(), empty());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void parseAndSaveRTPFileWithIssuesConfirmedTest() throws Exception {
        File file = ResourceUtils.getFile(WITH_ISSUES_CASE);
        final byte[] bytes = Files.readAllBytes(file.toPath());
        MockMultipartFile mock = new MockMultipartFile("mock", "questions.txt", "text/plain", bytes);
        // Actual test begins
        final QuestionsParsingResultOutDto result = questionsFileParserService.parseAndSave(mock, 1L, true);
        assertThat("Result of saving object is not as expected", result, allOf(
                hasProperty("questions", equalTo(3)),
                hasProperty("issues", equalTo(5)),
                hasProperty("charset", equalTo("UTF-8")),
                hasProperty("majorIssues", equalTo(5)),
                hasProperty("mediumIssues", equalTo(0)),
                hasProperty("minorIssues", equalTo(0)),
                hasProperty("allIssues", hasSize(5)),
                hasProperty("saved", equalTo(true))
        ));
        assertThat("Questions quantity in DB is not 3", (List<Question>)em.createQuery(FIND).getResultList(), hasSize(3));
    }
}
