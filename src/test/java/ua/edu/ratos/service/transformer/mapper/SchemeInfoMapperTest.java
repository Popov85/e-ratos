package ua.edu.ratos.service.transformer.mapper;

import org.junit.jupiter.api.Test;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.SchemeTheme;
import ua.edu.ratos.dao.entity.SchemeThemeSettings;
import ua.edu.ratos.service.dto.out.SchemeInfoOutDto;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SchemeInfoMapperTest {

    private static class SchemeInfoMapperImpl extends SchemeInfoMapper {

        @Override
        public SchemeInfoOutDto toDto(Scheme entity) {
            return new SchemeInfoOutDto();
        }
    }

    @Test
    public void getQuestionsCount() {
        SchemeInfoMapperImpl mockImpl = new SchemeInfoMapperImpl();

        SchemeTheme st1 = new SchemeTheme();
        SchemeThemeSettings sts1 = new SchemeThemeSettings();
        sts1.setLevel1((short) 15);
        sts1.setLevel2((short) 10);
        sts1.setLevel3((short) 5);
        st1.setSettings(Set.of(sts1));

        SchemeTheme st2 = new SchemeTheme();
        SchemeThemeSettings sts2 = new SchemeThemeSettings();
        sts2.setLevel1((short) 10);
        sts2.setLevel2((short) 5);
        sts2.setLevel3((short) 0);
        st2.setSettings(Set.of(sts2));

        int actualResult = mockImpl.getQuestionsCount(List.of(st1, st2));
        assertThat("Result of sum is not as expected", actualResult, is(45));
    }
}