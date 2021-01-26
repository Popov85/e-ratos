package ua.edu.ratos.service.transformer.entity_to_dto;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.game.Game;
import ua.edu.ratos.dao.entity.game.Gamer;
import ua.edu.ratos.dao.entity.game.Week;
import ua.edu.ratos.service.dto.out.StudMinOutDto;
import ua.edu.ratos.service.dto.out.game.GamerOutDto;
import ua.edu.ratos.service.session.GameLabelResolver;
import ua.edu.ratos.service.transformer.*;

import java.util.Optional;

@Deprecated
@Component
public class GamerDtoTransformer {

    private UserMinMapper userMinMapper;

    private ClassMinMapper classMinMapper;

    private FacultyMinMapper facultyMinMapper;

    private OrganisationMinMapper organisationMinMapper;

    private GameLabelResolver gameLabelResolver;

    private TotalAchievementsMapper totalAchievementsMapper;

    private WeeklyAchievementsMapper weeklyAchievementsMapper;

    @Autowired
    public void setClassMinDtoTransformer(ClassMinMapper classMinMapper) {
        this.classMinMapper = classMinMapper;
    }

    @Autowired
    public void setFacultyMinDtoTransformer(FacultyMinMapper facultyMinMapper) {
        this.facultyMinMapper = facultyMinMapper;
    }

    @Autowired
    public void setUserMinDtoTransformer(UserMinMapper userMinMapper) {
        this.userMinMapper = userMinMapper;
    }

    @Autowired
    public void setOrganisationMinDtoTransformer(OrganisationMinMapper organisationMinMapper) {
        this.organisationMinMapper = organisationMinMapper;
    }

    @Autowired
    public void setGameLabelResolver(GameLabelResolver gameLabelResolver) {
        this.gameLabelResolver = gameLabelResolver;
    }

    @Autowired
    public void setTotalAchievementsDtoTransformer(TotalAchievementsMapper totalAchievementsMapper) {
        this.totalAchievementsMapper = totalAchievementsMapper;
    }

    @Autowired
    public void setWeeklyAchievementsDtoTransformer(WeeklyAchievementsMapper weeklyAchievementsMapper) {
        this.weeklyAchievementsMapper = weeklyAchievementsMapper;
    }

    public GamerOutDto toDto(@NonNull final Gamer entity) {
        Optional<Game> game = entity.getGame();
        Optional<Week> week = entity.getWeek();
        GamerOutDto dto = new GamerOutDto().setStudent(getStud(entity));
        if (game.isPresent()) {
            dto.setLabel(gameLabelResolver.getLabel(game.get().getTotalWins()));
            dto.setTotal(totalAchievementsMapper.toDto(game.get()));
        }
        if (week.isPresent()) dto.setWeekly(weeklyAchievementsMapper.toDto(week.get()));
        return dto;
    }

    private StudMinOutDto getStud(@NonNull final Gamer entity) {
        return new StudMinOutDto()
                .setStudId(entity.getStudId())
                .setUser(userMinMapper.toDto(entity.getUser()))
                .setStudentClass(classMinMapper.toDto(entity.getStudentClass()))
                .setFaculty(facultyMinMapper.toDto(entity.getFaculty()))
                .setOrganisation(organisationMinMapper.toDto(entity.getOrganisation()))
                .setEntranceYear(entity.getEntranceYear());
    }
}
