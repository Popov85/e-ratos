package ua.edu.ratos.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.DayOfWeek;
import java.util.Map;

@Getter
@Setter
@ToString
@Component
@Validated
@ConfigurationProperties("ratos")
public class AppProperties {

    private Init init;

    private LTI lti;

    private Session session;

    private Result result;

    private Game game;

    @Getter
    @Setter
    @ToString
    public static class Init {

        /**
         * Clear db and re-init with pre-defined values
         */
        private boolean reInit;

        /**
         * Locale for init data, supported {en, fr, ru}
         */
        private Language locale;

        public enum Language {
            EN, FR, RU
        }

    }

    @Getter
    @Setter
    @ToString
    public static class LTI {
        /**
         * As per LTI v1.1.1, this is the base URL which TC (LMS) should request with POST to perform launch request
         * Change this value for the actual domain name after production deployment
         */
        private String launchUrl;
        private String launchPath;

        /**
         * Whether we should replace https (requested) to http (actual) protocols?
         */
        private boolean launchUrlFix;
    }

    @Getter
    @Setter
    @ToString
    public static class Session {
        /**
         * Whether we should take into account student groups when deciding if to allow access to a given schema?
         */
        private boolean includeGroups;
        /**
         * Whether we should shuffle answers each time? If true, randomizes answers of questions where possible.
         */
        private boolean shuffleEnabled;

        /**
         * Specified the algorithm to randomly pick up questions from DB
         */
        private Algorithm randomAlgorithm;
        /**
         * How many sessions can be preserved by a single user within this installation of RATOS
         */
        private int preservedLimit;

        /**
         * How many starred questions can be kept by a single user within this installation of RATOS
         */
        private int starredLimit;

        /**
         * If cancelled by user, should we save these results to DB?
         */
        private boolean saveCancelledResults;

        /**
         * At the session end, should we save to DB time-outed results?
         */
        private boolean saveTimeoutedResults;

        /**
         * In case of long inactivity with lost forever session, should we save to DB time-outed results?
         */
        private boolean saveAbandonedResults;

        public enum Algorithm {
            JPA, DB, DECIDE
        }
    }

    /**
     * All about resultDetails
     */
    @Getter
    @Setter
    @ToString
    public static class Result {
        /**
         * Whether the regular cleaning or result_details table is enabled in this installation of RATOS
         */
        private boolean cleanOn;

        private Period period;

        @Min(0)
        @Max(24)
        private int cleanHour;

        @Min(0)
        @Max(59)
        private int cleanMinute;

        public enum Period {
            DAILY, WEEKLY, MONTHLY
        }
    }


    @Getter
    @Setter
    @ToString
    public static class Game {
        /**
         * Whether game mode is enabled in this installation of RATOS
         */
        private boolean gameOn;

        /**
         * Boundaries for granting points, exceptionally for control type of sessions:
         * (a user has to pass a test from the first attempt, a scheme must not allow right answers,
         * skips, pyramid, and is time-limited);
         */
        @Min(1)
        private double lowBoundaryFrom;
        private double middleBoundaryFrom;
        private double highBoundaryFrom;

        private double lowBoundaryTo;
        private double middleBoundaryTo;
        @Max(100)
        private double highBoundaryTo;

        private int lowBoundaryPoints;
        private int middleBoundaryPoints;
        private int highBoundaryPoints;

        /**
         * How many strikes in a row (during a week) a user should get to be granted a bonus
         */
        private int bonusStrike;

        /**
         * What is the bonus size (global value)?
         */
        private int bonusSize;

        /**
         * Each week, there is the user rating based on scored points during this week.
         * Top n-% from this rating list are considered to be winners;
         * This parameter represents exactly the desired percentage of users from the top of the list.
         */
        @Min(1)
        @Max(50)
        private int topWeekly;

        /**
         * Day of week when it is considered the end of the week to reset weekly results.
         */
        private DayOfWeek resetWeeklyDay;

        @Min(0)
        @Max(24)
        private int resetWeeklyHour;

        @Min(0)
        @Max(59)
        private int resetWeeklyMinute;

        /**
         * How a user is titled depending on the number of weeks he won in the competition;
         * Key - number of weeks;
         * Value - corresponding title
         */
        private Map<Integer, String> userLabel;

        /**
         * If session was cancelled by user, should we anyways process gamification points?
         * Not only grant points, but also update fields like time-spend and lose strike etc.
         */
        private boolean processCancelledResults;

        /**
         * The same like previous but for time-outed sessions.
         */
        private boolean processTimeoutedResults;
    }
}
