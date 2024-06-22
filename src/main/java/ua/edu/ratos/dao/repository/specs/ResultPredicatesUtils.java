package ua.edu.ratos.dao.repository.specs;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import ua.edu.ratos.dao.entity.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@UtilityClass
public class ResultPredicatesUtils {

    @Deprecated
    // This is how exactly react-datepicker component sends dates to server
    private static DateTimeFormatter SIMPLE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // This format is exactly how react-bootstrap-tables2 component sends date from its Date filter in remote mode!
    private static DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


    public static Specification<ResultOfStudent> hasSpecs(@NonNull final Map<String, SpecsFilter> criteria) {
        return (Specification<ResultOfStudent>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, SpecsFilter> entry : criteria.entrySet()) {
                String key = entry.getKey();
                SpecsFilter value = entry.getValue();
                Optional<Predicate> predicate = getSpec(key, value, root, builder);
                if (predicate.isPresent()) predicates.add(predicate.get());
            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    static Optional<Predicate> getSpec(String key, SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        switch (key) {
            case "course": // Alias used to build a report
            case "scheme.course":  return getSpecCourse(value, root, builder);
            case "scheme":  return getSpecScheme(value, root, builder);
            case "student.user.name":  return getSpecName(value, root, builder);
            case "student.user.surname":  return getSpecSurname(value, root, builder);
            case "student.user.email":  return getSpecEmail(value, root, builder);
            case "student.faculty":  return getSpecFaculty(value, root, builder);
            case "student.studentClass":  return getSpecClass(value, root, builder);
            case "student.entranceYear":  return getSpecYear(value, root, builder);
            case "sessionEnded":  return getSpecEnded(value, root, builder); // Used in table filter
            case "sessionEndedFrom":  return getSpecEndedFrom(value, root, builder); // Used to build a report
            case "sessionEndedTo":  return getSpecEndedTo(value, root, builder); // Used to build a report
            case "sessionLasted":  return getSpecLasted(value, root, builder);
            case "grade": return getSpecGrade(value, root, builder);
            case "percent": return getSpecPercent(value, root, builder);
            case "passed": return getSpecPassed(value, root, builder);
            case "timeouted": return getSpecTimeouted(value, root, builder);
            case "cancelled": return getSpecCancelled(value, root, builder);
            case "lms": return getSpecLMS(value, root, builder);
            case "points": return getSpecPoints(value, root, builder);
            default: return Optional.empty();
        }
    }

    static Optional<Predicate> getSpecCourse(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. FK
        // 2. Entity = Course
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Long courseId = Long.parseLong((String)object);
            Predicate coursePredicate = builder.equal(root.get(ResultOfStudent_.scheme).get(Scheme_.course).get(Course_.courseId), courseId);
            return Optional.of(coursePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecScheme(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. FK
        // 2. Entity = Scheme
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Long schemeId = Long.parseLong((String)object);
            Predicate schemePredicate = builder.equal(root.get(ResultOfStudent_.scheme).get(Scheme_.schemeId), schemeId);
            return Optional.of(schemePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecName(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. String
        // 2. Entity = Student->User
        // 3. Comparator always StartsFrom!
        Object object = value.getFilterVal();
        if (object != null ) {
            String name = (String) object;
            String nameCriteria = name + "%";
            Predicate namePredicate = builder.like(root.get(ResultOfStudent_.student).get(Student_.user).get(User_.name), nameCriteria);
            return Optional.of(namePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecSurname(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. String
        // 2. Entity = Student->User
        // 3. Comparator always StartsFrom!
        Object object = value.getFilterVal();
        if (object != null ) {
            String surname = (String) object;
            String surnameCriteria = surname + "%";
            Predicate surnamePredicate = builder.like(root.get(ResultOfStudent_.student).get(Student_.user).get(User_.surname), surnameCriteria);
            return Optional.of(surnamePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecEmail(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. String
        // 2. Entity = Student->User
        // 3. Comparator always Contains!
        Object object = value.getFilterVal();
        if (object != null ) {
            String email = (String) object;
            String emailCriteria = "%" +email + "%";
            Predicate emailPredicate = builder.like(root.get(ResultOfStudent_.student).get(Student_.user).get(User_.email), emailCriteria);
            return Optional.of(emailPredicate);
        }
        return Optional.empty();
    }


    static Optional<Predicate> getSpecFaculty(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. FK
        // 2. Entity = Student->Faculty
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Long facId = Long.parseLong((String)object);
            Predicate facultyPredicate = builder.equal(root.get(ResultOfStudent_.student).get(Student_.faculty).get(Faculty_.facId), facId);
            return Optional.of(facultyPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecClass(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. String
        // 2. Entity = Student->Class->name
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            String clazz = String.valueOf(object);
            Predicate classPredicate = builder.equal(root.get(ResultOfStudent_.student).get(Student_.studentClass).get(Clazz_.name), clazz);
            return Optional.of(classPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecYear(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Integer
        // 2. Entity = Student
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Integer year= Integer.parseInt((String)object);
            Predicate yearPredicate = builder.equal(root.get(ResultOfStudent_.student).get(Student_.entranceYear), year);
            return Optional.of(yearPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecEnded(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Date
        // 2. Entity = Result
        // 3. Comparator: =, >, <
        Object object = value.getFilterVal();
        if (object != null ) {
            Map<String, String> map = (Map<String, String>) object;
            String string = map.get("date");
            if (string ==null || "".equals(string)) return Optional.empty();

            LocalDateTime dateTime= LocalDateTime.parse(string, DEFAULT_FORMATTER);

            final ZoneId zone = ZoneId.systemDefault();
            ZoneOffset zoneOffSet = zone.getRules().getOffset(dateTime);
            OffsetDateTime offsetDateTime = dateTime.atOffset(zoneOffSet);

            // Decide which comparator to use:
            String type = map.get("comparator");
            Predicate datePredicate;
            if ("<".equals(type)) {
                datePredicate = builder.lessThan(root.get(ResultOfStudent_.sessionEnded), offsetDateTime);
            } else if (">".equals(type)) {
                datePredicate = builder.greaterThan(root.get(ResultOfStudent_.sessionEnded), offsetDateTime);
            } else { // =
                // From 00:00 to 23:59 this day
                OffsetDateTime from = offsetDateTime;
                OffsetDateTime to = offsetDateTime.plusHours(24);
                datePredicate = builder.and(
                        builder.greaterThan(root.get(ResultOfStudent_.sessionEnded), from),
                        builder.lessThan(root.get(ResultOfStudent_.sessionEnded), to)
                );
            }
            return Optional.ofNullable(datePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecEndedFrom(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Date
        // 2. Entity = Result
        Object object = value.getFilterVal();
        log.debug("Filter val = {}", object);
        if (object != null ) {
            Map<String, String> map = (Map<String, String>) object;
            String string = map.get("date");
            if (string ==null || "".equals(string)) return Optional.empty();

            LocalDate date = LocalDate.parse(string, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime from = date.atStartOfDay();

            final ZoneId zone = ZoneId.systemDefault();
            ZoneOffset zoneOffSet = zone.getRules().getOffset(from);
            OffsetDateTime offsetFrom = from.atOffset(zoneOffSet);

            Predicate datePredicate = builder.greaterThan(root.get(ResultOfStudent_.sessionEnded), offsetFrom);
            return Optional.ofNullable(datePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecEndedTo(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Date
        // 2. Entity = Result
        Object object = value.getFilterVal();
        if (object != null ) {
            Map<String, String> map = (Map<String, String>) object;
            String string = map.get("date");
            if (string ==null || "".equals(string)) return Optional.empty();
            LocalDate date = LocalDate.parse(string, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime to = date.atStartOfDay();

            final ZoneId zone = ZoneId.systemDefault();
            ZoneOffset zoneOffSet = zone.getRules().getOffset(to);
            OffsetDateTime offsetTo = to.atOffset(zoneOffSet);

            Predicate datePredicate = builder.lessThan(root.get(ResultOfStudent_.sessionEnded), offsetTo);
            return Optional.ofNullable(datePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecLasted(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Number
        // 2. Entity = Result
        // 3. Comparator: =, >, <
        Object object = value.getFilterVal();
        if (object != null ) {
            Map<String, String> map = (Map<String, String>) object;
            String string = map.get("number");
            if (string ==null || "".equals(string)) return Optional.empty();
            Long number = Long.parseLong(string);
            // Decide which comparator to use:
            String type = map.get("comparator");
            Predicate lastedPredicate;
            if ("<".equals(type)) {
                lastedPredicate = builder.lessThan(root.get(ResultOfStudent_.sessionLasted), number);
            } else if (">".equals(type)) {
                lastedPredicate = builder.greaterThan(root.get(ResultOfStudent_.sessionLasted), number);
            } else { // =
                lastedPredicate = builder.equal(root.get(ResultOfStudent_.sessionLasted), number);
            }
            return Optional.ofNullable(lastedPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecGrade(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Number->Double
        // 2. Entity = Result
        // 3. Comparator: =, >, <
        Object object = value.getFilterVal();
        if (object != null ) {
            Map<String, String> map = (Map<String, String>) object;
            String string = map.get("number");
            if (string ==null || "".equals(string)) return Optional.empty();
            Double number = Double.parseDouble(string);
            // Decide which comparator to use:
            String type = map.get("comparator");
            Predicate gradePredicate;
            if ("<".equals(type)) {
                gradePredicate = builder.lessThan(root.get(ResultOfStudent_.grade), number);
            } else if (">".equals(type)) {
                gradePredicate = builder.greaterThan(root.get(ResultOfStudent_.grade), number);
            } else { // =
                gradePredicate = builder.equal(root.get(ResultOfStudent_.grade), number);
            }
            return Optional.ofNullable(gradePredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecPercent(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Number->Double
        // 2. Entity = Result
        // 3. Comparator: =, >, <
        Object object = value.getFilterVal();
        if (object != null ) {
            Map<String, String> map = (Map<String, String>) object;
            String string = map.get("number");
            if (string ==null || "".equals(string)) return Optional.empty();
            Double number = Double.parseDouble(string);
            // Decide which comparator to use:
            String type = map.get("comparator");
            Predicate percentPredicate;
            if ("<".equals(type)) {
                percentPredicate = builder.lessThan(root.get(ResultOfStudent_.percent), number);
            } else if (">".equals(type)) {
                percentPredicate = builder.greaterThan(root.get(ResultOfStudent_.percent), number);
            } else { // =
                percentPredicate = builder.equal(root.get(ResultOfStudent_.percent), number);
            }
            return Optional.ofNullable(percentPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecPassed(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Boolean
        // 2. Entity = Result
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Predicate passedPredicate = builder.equal(root.get(ResultOfStudent_.passed), "true".equals(object));
            return Optional.of(passedPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecTimeouted(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Boolean
        // 2. Entity = Result
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Predicate timeoutedPredicate = builder.equal(root.get(ResultOfStudent_.timeOuted), "true".equals(object));
            return Optional.of(timeoutedPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecCancelled(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Boolean
        // 2. Entity = Result
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Predicate cancelledPredicate = builder.equal(root.get(ResultOfStudent_.cancelled), "true".equals(object));
            return Optional.of(cancelledPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecPoints(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Boolean
        // 2. Entity = Result
        // 3. Comparator always =
        Object object = value.getFilterVal();
        if (object != null ) {
            Predicate pointsPredicate = builder.equal(root.get(ResultOfStudent_.points), "true".equals(object));
            return Optional.of(pointsPredicate);
        }
        return Optional.empty();
    }

    static Optional<Predicate> getSpecLMS(SpecsFilter value, Root<ResultOfStudent> root, CriteriaBuilder builder) {
        // 1. Boolean
        // 2. Entity = Result
        // 3. Comparator: special case!!!
        Object object = value.getFilterVal();
        if (object != null ) {
            Boolean lms = Boolean.valueOf((String) object);
            Predicate lmsPredicate;
            if (lms) {
                lmsPredicate = builder.isTrue(root.join(ResultOfStudent_.lms, JoinType.LEFT).isNotNull());
            } else {
                lmsPredicate = builder.isTrue(root.join(ResultOfStudent_.lms, JoinType.LEFT).isNull());
            }
            return Optional.of(lmsPredicate);
        }
        return Optional.empty();
    }
}
