package ua.edu.ratos.service.session;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.dao.entity.Result;
import ua.edu.ratos.dao.entity.Student;
import ua.edu.ratos.dao.entity.game.Bonus;
import ua.edu.ratos.dao.entity.game.Game;
import ua.edu.ratos.dao.entity.game.Week;
import ua.edu.ratos.dao.entity.game.Wins;
import ua.edu.ratos.dao.repository.ResultRepository;
import ua.edu.ratos.dao.repository.game.*;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.dto.out.game.AllTimesGamerOutDto;
import ua.edu.ratos.service.dto.out.game.GamerOutDto;
import ua.edu.ratos.service.dto.out.game.WeeklyGamerOutDto;
import ua.edu.ratos.service.dto.out.game.WinnerOutDto;
import ua.edu.ratos.service.transformer.mapper.GamerMapper;
import ua.edu.ratos.service.transformer.mapper.TotalTopMapper;
import ua.edu.ratos.service.transformer.mapper.WeeklyGamerMapper;
import ua.edu.ratos.service.transformer.mapper.WinnerMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GameService {

    @PersistenceContext
    private final EntityManager em;

    private final AppProperties appProperties;

    private final ResultRepository resultRepository;

    private final GameRepository gameRepository;

    private final WeekRepository weekRepository;

    private final BonusRepository bonusRepository;

    private final WinsRepository winsRepository;

    private final GamerRepository gamerRepository;

    private final GamerMapper gamerMapper;

    private final WinnerMapper winnerMapper;

    private final WeeklyGamerMapper weeklyGamerMapper;

    private final TotalTopMapper totalTopMapper;

    private final SecurityUtils securityUtils;


    /**
     * Allows to quickly decide if a user has gained any gamification points for this session.
     * It involves minimum or no DB operations. This information needs to be included into result DTO,
     * without waiting for long asynchronous DB operations completion.
     * @param sessionData
     * @param percent
     * @return gamification points if any
     */
    @Transactional
    public Optional<Integer> getPoints(@NonNull final SessionData sessionData, double percent) {
        Long userId = sessionData.getUserId();
        Long schemeId = sessionData.getSchemeDomain().getSchemeId();
        if (!isGameOn()) return Optional.empty();
        if (sessionData.isGameableSession() && isEnoughPercents(percent) && isFromFirstAttempt(userId, schemeId)) {
            return Optional.of(getPoints(percent));
        }
        return Optional.ofNullable(0);
    }

    /**
     * If game mode is turned off - the method throws RuntimeException.
     * If for gameable session you score less percents then low threshold - you lose strike.
     * if for gameable session you score enough percents and did it from the first attempt
     * you are granted a corresponding amount of scores depending on gaming settings.
     * Only Week table is updated if you failed to score any points, timeSpent field.
     * Both Week and Game tables are updated in case you are granted any points.
     * A new entry is added to Bonus table if you are gained strike and granted a bonus.
     * @param sessionData session data
     * @param percent the scored percent
     * @param timeSpent time spend for this learning session
     */
    @Transactional
    public void doGameProcessing(@NonNull final SessionData sessionData, double percent, long timeSpent) {
        if (!isGameOn()) throw new RuntimeException("Failed to process gamification points, gaming mode is off");
        Long userId = sessionData.getUserId();
        // Lose strike for any gameable session if failed to score enough percents
        if (sessionData.isGameableSession() && !isEnoughPercents(percent)) {
            resetWeeklyStrike(userId);
            createOrUpdateWeeklyTimeSpent(userId, timeSpent);
            return;
        }
        Long schemeId = sessionData.getSchemeDomain().getSchemeId();
        // from first attempt
        if (sessionData.isGameableSession() && isEnoughPercents(percent) && isFromFirstAttempt(userId, schemeId)) {
            Integer points = getPoints(percent);
            Optional<Week> week = weekRepository.findById(userId);
            if (!week.isPresent()) {
                Week w = new Week();
                w.setWeekPoints(points);
                w.setWeekStrike(1);
                w.setWeekTimeSpent(timeSpent);
                w.setStud(em.getReference(Student.class, userId));
                weekRepository.save(w);
                createOrUpdateGamePoints(userId, points);
            } else {
                // update week
                Week w = week.get();
                int weekPoints = w.getWeekPoints();
                int weekStrike = w.getWeekStrike();
                int weekBonuses = w.getWeekBonuses();
                long weekTimeSpent = w.getWeekTimeSpent();
                // check for weekly strike
                AppProperties.Game props = appProperties.getGame();
                if (weekStrike >= props.getBonusStrike()-1) {
                    int bonus = props.getBonusSize();
                    // update/create game bonus, add bonus entry
                    createOrUpdateGameBonuses(userId, bonus);
                    saveBonus(userId, bonus);
                    // reset counter
                    w.setWeekBonuses(weekBonuses + bonus);
                    w.setWeekStrike(0);
                } else {
                    w.setWeekStrike(++weekStrike);
                }
                w.setWeekPoints(weekPoints+points);
                w.setWeekTimeSpent(weekTimeSpent+timeSpent);
                createOrUpdateGamePoints(userId, points);
            }
        }
    }

    public boolean isGameOn() {
        AppProperties.Game props = appProperties.getGame();
        if (props.isGameOn()) return true;
        log.debug("Gamification mode is turned off");
        return false;
    }

    private boolean isEnoughPercents(double percent) {
        AppProperties.Game props = appProperties.getGame();
        if (percent >= props.getLowBoundaryFrom()) return true;
        log.debug("Not enough percents = {}", percent);
        return false;
    }

    private boolean isFromFirstAttempt(@NonNull final Long userId, @NonNull final Long schemeId) {
        // if user has already taken this scheme previously - no points can be granted!
        Slice<Result> results = resultRepository.findFirstByUserIdAndSchemeId(userId, schemeId, PageRequest.of(0, 1));
        if (!results.hasContent()) return true;
        log.debug("Not from the first attempt, userId = {}, schemeId = {}", userId, schemeId);
        return false;
    }

    public int getPoints(double percent) {
        AppProperties.Game props = appProperties.getGame();
        if (percent >= props.getLowBoundaryFrom() && percent < props.getLowBoundaryTo())
            return props.getLowBoundaryPoints();
        if (percent >= props.getMiddleBoundaryFrom() && percent < props.getMiddleBoundaryTo())
            return props.getMiddleBoundaryPoints();
        if (percent >= props.getHighBoundaryFrom() && percent <= props.getHighBoundaryTo())
            return props.getHighBoundaryPoints();
        log.error("Bad percent, cannot decide gamification points for percent = {}. Fallback to 0 points", percent);
        return 0;
    }

    private void resetWeeklyStrike(@NonNull final Long userId) {
        Optional<Week> week = weekRepository.findById(userId);
        if (week.isPresent()) {
            Week w = week.get();
            w.setWeekStrike(0);
        }
    }

    private void createOrUpdateWeeklyTimeSpent(@NonNull final Long userId, long timeSpent) {
        Optional<Week> week = weekRepository.findById(userId);
        if (!week.isPresent()) {
            Week w = new Week();
            w.setWeekTimeSpent(timeSpent);
            w.setStud(em.getReference(Student.class, userId));
            weekRepository.save(w);
        } else {
            Week w = week.get();
            long weekTimeSpent = w.getWeekTimeSpent();
            w.setWeekTimeSpent(weekTimeSpent + timeSpent);
        }
    }

    private void createOrUpdateGamePoints(@NonNull final Long userId, @NonNull final Integer points) {
        Optional<Game> game = gameRepository.findById(userId);
        if (!game.isPresent()) {
            Game g = new Game();
            g.setTotalPoints(points);
            g.setStud(em.getReference(Student.class, userId));
            gameRepository.save(g);
        } else { // just update value
            Game g = game.get();
            int totalPoints = g.getTotalPoints();
            g.setTotalPoints(totalPoints+points);
        }
    }

    private void createOrUpdateGameBonuses(@NonNull final Long userId, @NonNull final Integer bonus) {
        Optional<Game> game = gameRepository.findById(userId);
        if (!game.isPresent()) {
            Game g = new Game();
            g.setTotalBonuses(bonus);
            g.setStud(em.getReference(Student.class, userId));
            gameRepository.save(g);
        } else { // just update value
            Game g = game.get();
            int totalBonuses = g.getTotalBonuses();
            g.setTotalBonuses(totalBonuses+bonus);
        }
    }

    private void saveBonus(@NonNull final Long userId, @NonNull final Integer bonus) {
        Bonus b = new Bonus();
        b.setBonus(bonus);
        b.setStudent(em.getReference(Student.class, userId));
        b.setWhenGranted(LocalDateTime.now());
        bonusRepository.save(b);
    }

    //-----------------------------------------------Quartz regular weekly job------------------------------------------

    /**
     * Launched automatically by quartz scheduler, mainly at the end of a week.
     * Winners of the week for an organisation are considered those n% of students who participated in the competition and:
     * gained the most points, then gained the most bonuses, and then spent the least time.
     */
    @Transactional
    public void calculateAndSaveWeeklyWinners() {
        AppProperties.Game props = appProperties.getGame();
        int topWeeklyPercentage = props.getTopWeekly();
        // Get all entries from week table;
        List<Week> all = weekRepository.findAll();
        if (all.isEmpty()) {
            log.debug("No gamers found! Hence, no winners of the week!");
            return;
        }
        // For each organisation calculate top-n% of students and obtain a list of winners
        Map<Long, List<Week>> allWeeklyByOrg = all.stream()
                .collect(Collectors.groupingBy(w -> w.getStud().getOrganisation().getOrgId()));
        for (List<Week> orgList : allWeeklyByOrg.values()) {
            if (orgList.isEmpty()) break;
            orgList.sort(Comparator.comparing(Week::getWeekPoints, Comparator.reverseOrder())
                    .thenComparing(Week::getWeekBonuses, Comparator.reverseOrder())
                    .thenComparing(Week::getWeekTimeSpent, Comparator.naturalOrder()));
            int size = orgList.size();
            // get n% of the sorted list
            int topNSize = size * topWeeklyPercentage / 100;
            List<Week> winners = orgList.subList(0, topNSize);
            // borderline case, if percentage of winners is too small,
            // the first person in the sorted list is the winner
            if (winners.isEmpty()) winners = Arrays.asList(orgList.get(0));
            // create Wins, and update totalWins in Game
            saveWeeklyWinners(winners);
        }
        // reset week table
        weekRepository.emptyWeek();
    }

    private void saveWeeklyWinners(@NonNull final List<Week> winners) {
        for (Week winner : winners) {
            Wins wins = new Wins();
            wins.setStudent(em.getReference(Student.class, winner.getWeekId()));
            wins.setWonPoints(winner.getWeekPoints());
            wins.setWonBonuses(winner.getWeekBonuses());
            wins.setWonTimeSpent(winner.getWeekTimeSpent());
            wins.setWonDate(LocalDate.now());
            winsRepository.save(wins);
            // winners for sure have entries in Game table!
            gameRepository.incrementWins(winner.getWeekId());
        }
    }

    //--------------------------------------------------SELECT----------------------------------------------------------

    @Transactional(readOnly = true)
    public GamerOutDto findMyGamingRating() {
        Long studId = securityUtils.getAuthUserId();
        return gamerMapper.toDto(gamerRepository.findById(studId).get());
    }

    //-------------------------------------------For TOP weekly rating table--------------------------------------------

    @Transactional(readOnly = true)
    public Page<WeeklyGamerOutDto> findWeeklyGamers(@NonNull final Pageable pageable) {
        return weekRepository.findWeeklyGamers(pageable).map(weeklyGamerMapper::toDto);
    }

    //------------------------------------------For TOP rating students table-------------------------------------------

    @Transactional(readOnly = true)
    public Page<AllTimesGamerOutDto> findBestGamers(@NonNull final Pageable pageable) {
        return gameRepository.findBestGamers(pageable).map(totalTopMapper::toDto);
    }

    //-------------------------------------------For last week winners table--------------------------------------------
    // sorted by points, bonuses and time spent
    @Transactional(readOnly = true)
    public Page<WinnerOutDto> findLastWeekWinners(@NonNull final Pageable pageable) {
        return winsRepository.findAllWinnersSince(LocalDate.now().minusDays(7), pageable).map(winnerMapper::toDto);
    }

}
