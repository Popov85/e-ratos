package ua.edu.ratos.service._it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.SchemeTheme;
import ua.edu.ratos.service.SchemeThemeService;
import ua.edu.ratos.service.dto.in.SchemeThemeInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchemeThemeServiceTestIT {

    public static final String JSON_NEW = "classpath:json/scheme_theme_in_dto_new.json";
    public static final String FIND = "select s from SchemeTheme s join fetch s.settings where s.schemeThemeId=:schemeThemeId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SchemeThemeService schemeThemeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test(timeout = 10000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_theme_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        SchemeThemeInDto dto = objectMapper.readValue(json, SchemeThemeInDto.class);
        schemeThemeService.save(1L, dto);
        final SchemeTheme schemeTheme = (SchemeTheme) em.createQuery(FIND).setParameter("schemeThemeId", 1L).getSingleResult();
        assertThat("Saved SchemeTheme object is not as expected", schemeTheme, allOf(
                hasProperty("schemeThemeId", equalTo(1L)),
                hasProperty("scheme", hasProperty("schemeId", equalTo(1L))),
                hasProperty("theme", hasProperty("themeId", equalTo(1L))),
                hasProperty("settings", hasSize(3))
        ));
    }

    @Test(timeout = 10000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_theme_settings_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void removeSettingsTest() {
        // Given 4 settings objects associated with SchemeTheme, let's remove one (4th)
        schemeThemeService.removeSettings(1L, 4L);
        final SchemeTheme schemeTheme = (SchemeTheme) em.createQuery(FIND).setParameter("schemeThemeId", 1L).getSingleResult();
        assertThat("Updated SchemeTheme object is not as expected", schemeTheme, allOf(
                hasProperty("schemeThemeId", equalTo(1L)),
                hasProperty("scheme", hasProperty("schemeId", equalTo(1L))),
                hasProperty("theme", hasProperty("themeId", equalTo(1L))),
                hasProperty("settings", hasSize(3))
        ));
    }

    @Test(timeout = 10000, expected = RuntimeException.class)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_theme_settings_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void removeSettingsLastExceptionTest() {
        // Given 4 settings objects associated with SchemeTheme, let's remove all one by one
        schemeThemeService.removeSettings(1L, 1L);
        schemeThemeService.removeSettings(1L, 2L);
        schemeThemeService.removeSettings(1L, 3L);
        // expect exception here
        schemeThemeService.removeSettings(1L, 4L);
    }


}
