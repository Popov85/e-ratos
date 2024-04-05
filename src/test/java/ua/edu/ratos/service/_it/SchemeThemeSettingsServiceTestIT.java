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
import ua.edu.ratos.dao.entity.SchemeThemeSettings;
import ua.edu.ratos.service.SchemeThemeSettingsService;
import ua.edu.ratos.service.dto.in.SchemeThemeSettingsInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SchemeThemeSettingsServiceTestIT {

    public static final String JSON_NEW = "classpath:json/scheme_theme_settings_in_dto_new.json";
    public static final String JSON_UPD = "classpath:json/scheme_theme_settings_in_dto_upd.json";

    public static final String FIND = "select s from SchemeThemeSettings s where s.schemeThemeSettingsId=:schemeThemeSettingsId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SchemeThemeSettingsService schemeThemeSettingsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_theme_test_data.sql", "/scripts/scheme_theme_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        SchemeThemeSettingsInDto dto = objectMapper.readValue(json, SchemeThemeSettingsInDto.class);
        schemeThemeSettingsService.save(dto);
        final SchemeThemeSettings schemeThemeSettings = (SchemeThemeSettings) em.createQuery(FIND).setParameter("schemeThemeSettingsId", 2L).getSingleResult();
        assertThat("Saved SchemeThemeSettings object is not as expected", schemeThemeSettings, allOf(
                hasProperty("schemeThemeSettingsId", equalTo(2L)),
                hasProperty("schemeTheme", hasProperty("schemeThemeId", equalTo(1L))),
                hasProperty("type", hasProperty("typeId", equalTo(1L))),
                hasProperty("level1", equalTo((short) 20)),
                hasProperty("level2", equalTo((short) 10)),
                hasProperty("level3", equalTo((short) 5))
        ));
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_theme_test_data.sql", "/scripts/scheme_theme_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        SchemeThemeSettingsInDto dto = objectMapper.readValue(json, SchemeThemeSettingsInDto.class);
        schemeThemeSettingsService.update(1L, dto);
        final SchemeThemeSettings schemeThemeSettings = (SchemeThemeSettings) em.createQuery(FIND).setParameter("schemeThemeSettingsId", 1L).getSingleResult();
        assertThat("Updated SchemeThemeSettings object is not as expected", schemeThemeSettings, allOf(
                hasProperty("schemeThemeSettingsId", equalTo(1L)),
                hasProperty("schemeTheme", hasProperty("schemeThemeId", equalTo(1L))),
                hasProperty("type", hasProperty("typeId", equalTo(1L))),
                hasProperty("level1", equalTo((short) 5)),
                hasProperty("level2", equalTo((short) 5)),
                hasProperty("level3", equalTo((short) 5))
        ));
    }
}
