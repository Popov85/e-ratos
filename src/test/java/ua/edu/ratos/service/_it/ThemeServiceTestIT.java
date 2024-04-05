package ua.edu.ratos.service._it;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.service.ThemeService;
import ua.edu.ratos.service.dto.out.LevelsOutDto;
import ua.edu.ratos.service.dto.out.ThemeMapOutDto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ThemeServiceTestIT {

    @Autowired
    private ThemeService themeService;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_map_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getQuestionTypeLevelMapByThemeIdTest() {
        ThemeMapOutDto themeMapOutDto = themeService.getQuestionTypeLevelMapByThemeId(1L);
        assertThat("Found ThemeMapOutDto object is not as expected", themeMapOutDto, allOf(
                hasProperty("themeId", equalTo(1L)),
                hasProperty("totalByTheme", equalTo(21)),
                hasProperty("typeLevelMap", allOf(
                        hasEntry(1L, new LevelsOutDto("MCQ", 2, 3, 3, 8)),
                        hasEntry(2L, new LevelsOutDto("FBSQ", 1, 1, 1, 3)),
                        hasEntry(3L, new LevelsOutDto("FBMQ", 3, 0, 0, 3)),
                        hasEntry(4L, new LevelsOutDto("MQ", 3, 1, 0, 4)),
                        hasEntry(5L, new LevelsOutDto("SQ", 2, 0, 1, 3))
                ))
        ));
    }
}
