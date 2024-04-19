package ua.edu.ratos.service._it;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.grading.SchemeFourPoint;
import ua.edu.ratos.service.SchemeService;
import ua.edu.ratos.service.dto.in.SchemeInDto;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestContainerConfig.class)
public class SchemeServiceTestIT extends BaseIT {

    private static final String JSON_NEW = "classpath:json/scheme_in_dto_new.json";
    private static final String FIND_WITH_COLLECTIONS = "select s from Scheme s left join fetch s.groups left join fetch s.themes t left join fetch t.settings where s.schemeId=:schemeId";
    private static final String FIND_GRADING_FOUR = "select s from SchemeFourPoint s join fetch s.fourPointGrading where s.schemeId=:schemeId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        SchemeInDto dto = objectMapper.readValue(json, SchemeInDto.class);
        // Actual test begins
        schemeService.save(dto);
        final Scheme scheme = (Scheme) em.createQuery(FIND_WITH_COLLECTIONS).setParameter("schemeId", 1L).getSingleResult();
        assertThat("Saved Scheme object is not as expected", scheme, allOf(
                hasProperty("schemeId", equalTo(1L)),
                hasProperty("name", equalTo("Scheme #1")),
                hasProperty("groups", hasSize(3)),
                hasProperty("themes", hasSize(3))
        ));
        // Verify insertion into scheme_four_point table
        final SchemeFourPoint foundFour = (SchemeFourPoint) em.createQuery(FIND_GRADING_FOUR).setParameter("schemeId", 1L).getSingleResult();
        assertNotNull(foundFour, "SchemeFourPoint is not found");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateNameTest() {
        // Update only name
        schemeService.updateName(1L, "Updated name");
        final Scheme scheme = (Scheme) em.createQuery(FIND_WITH_COLLECTIONS).setParameter("schemeId", 1L).getSingleResult();
        assertThat("Updated Scheme object is not as expected", scheme, allOf(
                hasProperty("schemeId", equalTo(1L)),
                hasProperty("name", equalTo("Updated name"))
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateIsActiveTest() {
        // Deactivate
        schemeService.updateIsActive(1L, false);
        final Scheme scheme = (Scheme) em.createQuery(FIND_WITH_COLLECTIONS).setParameter("schemeId", 1L).getSingleResult();
        assertThat("Updated Scheme object is not as expected", scheme, allOf(
                hasProperty("schemeId", equalTo(1L)),
                hasProperty("name", equalTo("Scheme #1")),
                hasProperty("active", equalTo(false))
        ));
    }

}
