package ua.edu.ratos.dao.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.question.QuestionMCQ;

import javax.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class QuestionRepositoryTestIT {

    @Autowired
    private QuestionRepository questionRepository;

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_test_data_types.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countByThemeIdTest() {
        assertThat("Count of Themes is not 5",
                questionRepository.countByThemeId(1L), equalTo(5));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_test_data_types.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countAllTypesByThemeIdTest() {
        assertThat("Set of TypeAndCount is not of size = 3",
                questionRepository.countAllTypesByThemeId(1L), hasSize(3));
    }

    //@Ignore
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_test_data_types.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForTypeLevelMapByThemeIdTest() {
        assertThat("Set of Question is not of size = 5",
                questionRepository.findAllForTypeLevelMapByThemeId(1L), hasSize(5));
    }

    //---------------------------------------------------Student session------------------------------------------------
    // simple
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_session_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForSimpleSessionByThemeTypeAndLevelTest() {
        assertThat("Set of Question is not of size = 3",
                questionRepository.findAllForSimpleSessionByThemeTypeAndLevel(1L, 1L, (byte) 1), hasSize(3));
    }

    // cached
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllMCQForCachedSessionWithEverythingByThemeAndLevelTest() {
        assertThat("Set of QuestionMCQ is not of size = 3",
                questionRepository.findAllMCQForCachedSessionWithEverythingByThemeAndLevel(1L,  (byte) 1), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbsq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllFBSQForCachedSessionWithEverythingByThemeAndLevelTest() {
        assertThat("Set of QuestionFBSQ is not of size = 3",
                questionRepository.findAllFBSQForCachedSessionWithEverythingByThemeAndLevel(1L,  (byte) 1), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbmq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllFBMQForCachedSessionWithEverythingByThemeAndLevelTest() {
        assertThat("Set of QuestionFBMQ is not of size = 3",
                questionRepository.findAllFBMQForCachedSessionWithEverythingByThemeAndLevel(1L,  (byte) 2), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllMQForCachedSessionWithEverythingByThemeAndLevelTest() {
        assertThat("Set of QuestionMQ is not of size = 2",
                questionRepository.findAllMQForCachedSessionWithEverythingByThemeAndLevel(1L,  (byte) 1), hasSize(2));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_sq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllSQForCachedSessionWithEverythingByThemeAndLevelTest() {
        assertThat("Set of QuestionMQ is not of size = 1",
                questionRepository.findAllSQForCachedSessionWithEverythingByThemeAndLevel(1L,  (byte) 2), hasSize(1));
    }

    //-----------------------------------------------------Instructor---------------------------------------------------
    // one
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneMCQForEditByIdTest() {
        Optional<QuestionMCQ> optional = questionRepository.findOneMCQForEditById(1L);
        assertTrue("QuestionMCQ was not found for questionId = 1L", optional.isPresent());
        assertThat("Wrong answers size", optional.get().getAnswers(), hasSize(4));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbsq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneFBSQForEditByIdTest() {
        assertTrue("QuestionFBSQ was not found for questionId = 1L",
                questionRepository.findOneFBSQForEditById(1L).isPresent());


    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbmq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneFBMQForEditByIdTest() {
        assertTrue("QuestionFBMQ was not found for questionId = 1L",
                questionRepository.findOneFBMQForEditById(1L).isPresent());

    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneMQForEditByIdTest() {
        assertTrue("QuestionMQ was not found for questionId = 1L",
                questionRepository.findOneMQForEditById(1L).isPresent());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_sq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneSQForEditByIdTest() {
        assertTrue("QuestionSQ was not found for questionId = 1L",
                questionRepository.findOneSQForEditById(1L).isPresent());
    }

    // table
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllMCQForEditByThemeIdTest() {
        assertThat("Page of QuestionMCQ is not of size = 3",
                questionRepository.findAllMCQForEditByThemeId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }


    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbsq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllFBSQForEditByThemeIdTest() {
        assertThat("Page of QuestionFBSQ is not of size = 3",
                questionRepository.findAllFBSQForEditByThemeId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbmq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllFBMQForEditByThemeIdTest() {
        assertThat("Page of QuestionFBMQ is not of size = 3",
                questionRepository.findAllFBMQForEditByThemeId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllMQForEditByThemeIdTest(){
        assertThat("Page of QuestionMQ is not of size = 3",
                questionRepository.findAllMQForEditByThemeId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_sq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllSQForEditByThemeIdTest() {
        assertThat("Page of QuestionSQ is not of size = 3",
                questionRepository.findAllSQForEditByThemeId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }



    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllMCQForEditByThemeIdAndQuestionLettersContainsTest() {
        assertThat("Slice of QuestionMCQ is not of size = 2",
                questionRepository.findAllMCQForSearchByDepartmentIdAndTitleContains(1L, "MCQ", PageRequest.of(0, 30)).getContent(), hasSize(2));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbsq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllFBSQForEditByThemeIdAndQuestionLettersContainsTest() {
        assertThat("Slice of QuestionFBSQ is not of size = 3",
                questionRepository.findAllFBSQForSearchByDepartmentIdAndTitleContains(1L, "FBSQ", PageRequest.of(0, 30)).getContent(), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbmq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllFBMQForEditByThemeIdAndQuestionLettersContainsTest() {
        assertThat("Slice of QuestionFBMQ is not of size = 1",
                questionRepository.findAllFBMQForSearchByDepartmentIdAndTitleContains(1L, "FBMQ", PageRequest.of(0, 30)).getContent(), hasSize(1));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllMQForEditByThemeIdAndQuestionLettersContainsTest(){
        assertThat("Slice of QuestionMQ is not of size = 2",
                questionRepository.findAllMQForSearchByDepartmentIdAndTitleContains(1L, "MQ", PageRequest.of(0, 30)).getContent(), hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_sq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllSQForEditByThemeIdAndQuestionLettersContainsTest() {
        assertThat("Slice of QuestionSQ is not of size = 2",
                questionRepository.findAllSQForSearchByDepartmentIdAndTitleContains(1L, "SQ", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }


    //---------------------------------------------REPORT on content----------------------------------------------------
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countQuestionsByDepOfDepId() {
        Tuple questionsByDep = questionRepository.countQuestionsByDepOfDepId(1L);
        assertThat("Org. name is not as expected", questionsByDep.get("org"), CoreMatchers.equalTo("University"));
        assertThat("Fac. name is not as expected", questionsByDep.get("fac"), CoreMatchers.equalTo("Faculty"));
        assertThat("Dep. name is not as expected", questionsByDep.get("dep"), CoreMatchers.equalTo("Department"));
        assertThat("Count of themes is not as expected", questionsByDep.get("count"), CoreMatchers.equalTo(5L));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countQuestionsByDepOfFacId() {
        Set<Tuple> questionsByDeps = questionRepository.countQuestionsByDepOfFacId(1L);
        assertThat("Count tuple of themes by dep is not of right size", questionsByDeps, hasSize(1));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countQuestionsByDepOfFacIdNegative() {
        Set<Tuple> questionsByDeps = questionRepository.countQuestionsByDepOfFacId(2L);
        assertThat("Count tuple of themes by dep is not empty", questionsByDeps, empty());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countQuestionsByDepOfOrgId() {
        Set<Tuple> questionsByDeps = questionRepository.countQuestionsByDepOfOrgId(1L);
        assertThat("Count tuple of themes by dep is not of right size", questionsByDeps, hasSize(1));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countQuestionsByDepOfOrgIdNegative() {
        Set<Tuple> questionsByDeps = questionRepository.countQuestionsByDepOfOrgId(2L);
        assertThat("Count tuple of themes by dep is not empty", questionsByDeps, empty());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countQuestionsByDepOfRatos() {
        Set<Tuple> questionsByDeps = questionRepository.countQuestionsByDepOfRatos();
        assertThat("Count tuple of themes by dep is not of right size", questionsByDeps, hasSize(1));
    }
}
