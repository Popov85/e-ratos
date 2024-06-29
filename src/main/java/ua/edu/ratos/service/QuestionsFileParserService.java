package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.ratos.dao.entity.QuestionType;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.Theme;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.dao.entity.question.QuestionMCQ;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.out.QuestionsParsingResultOutDto;
import ua.edu.ratos.service.parsers.ParserFactory;
import ua.edu.ratos.service.parsers.QuestionsFileParser;
import ua.edu.ratos.service.parsers.QuestionsParsingIssue;
import ua.edu.ratos.service.parsers.QuestionsParsingResult;
import ua.edu.ratos.service.transformer.QuestionsParsingResultTransformer;
import ua.edu.ratos.service.utils.CharsetDetector;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class QuestionsFileParserService {

    //Currently, only MCQ (Type ID = 1) are supported to be saved via file
    private static final long DEFAULT_QUESTION_TYPE_ID = 1L;

    @PersistenceContext
    private final EntityManager em;

    private final QuestionService questionService;

    private final QuestionsParsingResultTransformer questionsParsingResultTransformer;

    private final CharsetDetector charsetDetector;

    private final ParserFactory parserFactory;

    private final SecurityUtils securityUtils;

    /**
     * Parses multipart file and saves all the questions to DB
     * @param multipartFile file with questions
     * @param themeId theme all the questions belong to
     * @param confirmed is the operation confirmed by the user?
     * @return result on parsing and saving
     * @link <a href="https://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/input/BOMInputStream.html">BOM</>
     */
    @Transactional
    public QuestionsParsingResultOutDto parseAndSave(@NonNull final MultipartFile multipartFile,
                                                     @NonNull final Long themeId,
                                                     boolean confirmed) {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        QuestionsFileParser parser = parserFactory.getParser(extension);
        // Detect and remove BOM symbol from is2
        try (InputStream is = multipartFile.getInputStream();
             InputStream is2 = BOMInputStream.builder().setInputStream(multipartFile.getInputStream()).get()) {
            final String encoding = charsetDetector.detectEncoding(is);
            final QuestionsParsingResult parsingResult = parser.parseStream(is2, encoding);
            int quantity = parsingResult.getQuestions().size();
            if (confirmed) {
                save(parsingResult.getQuestions(), themeId);
                log.debug("Saved questions into the DB after confirmation, {}", quantity);
                return questionsParsingResultTransformer.toDto(parsingResult, true);
            }
            if (parsingResult.issuesOf(QuestionsParsingIssue.Severity.MAJOR)==0) {
                save(parsingResult.getQuestions(), themeId);
                log.debug("Saved questions into the DB with no major issues, {}", quantity);
                return questionsParsingResultTransformer.toDto(parsingResult, true);
            } else {
                return questionsParsingResultTransformer.toDto(parsingResult, false);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform parsing and saving!", e);
        }
    }

    private void save(@NonNull final List<QuestionMCQ> parsedQuestions, @NonNull final Long themeId) {
        // First, Enrich question with Theme and Type, secondly for each non-null Help, enrich it with Staff
        QuestionType type = em.getReference(QuestionType.class, DEFAULT_QUESTION_TYPE_ID);
        Theme theme = em.getReference(Theme.class, themeId);
        Staff staff = em.getReference(Staff.class, securityUtils.getAuthStaffId());
        final List<Question> questions = new ArrayList<>();
        parsedQuestions.forEach(q->{
            q.setTheme(theme);
            q.setType(type);
            if (q.getHelp().isPresent()) q.getHelp().get().setStaff(staff);
            questions.add(q);
        });
        questionService.saveAll(questions);
    }
}
