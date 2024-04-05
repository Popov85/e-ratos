package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PhraseRepositoryTestIT {

    @Autowired
    private PhraseRepository phraseRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/phrase_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForEditTest() {
        assertTrue(phraseRepository.findOneForEdit(1L).isPresent(), "Phrase was not found for phraseId = 1L");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/phrase_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByStaffIdTest() {
        assertThat("Page of Phrase is not of size = 4",
                phraseRepository.findAllByStaffId(1L, PageRequest.of(0, 50)).getContent(), hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/phrase_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByStaffIdAndFirstLettersTest() {
        assertThat("Page of Phrase is not of size = 2",
                phraseRepository.findAllByStaffIdAndPhraseLettersContains(2L, "trans", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/phrase_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Page of Phrase is not of size = 7",
                phraseRepository.findAllByDepartmentId(1L, PageRequest.of(0, 50)).getContent(), hasSize(7));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/phrase_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdAndFirstLettersTest() {
        assertThat("Page of Phrase is not of size = 2",
                phraseRepository.findAllByDepartmentIdAndPhraseLettersContains(1L, "trans", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/phrase_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of Phrase is not of size = 7",
                phraseRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(7));
    }
}
