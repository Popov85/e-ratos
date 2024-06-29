package ua.edu.ratos.service.parsers;

import lombok.extern.slf4j.Slf4j;
import ua.edu.ratos.dao.entity.question.QuestionMCQ;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
abstract class AbstractQuestionsFileParser implements QuestionsFileParser {

    boolean startStatus;

    String currentLine;

    int currentRow;

    List<QuestionMCQ> questions = new ArrayList<>();

    List<QuestionsParsingIssue> questionsParsingIssues = new ArrayList<>();

    String header = "";

    @Override
    public QuestionsParsingResult parseFile(File file, String charset) {
        Path path = file.toPath();
        try (BufferedReader br = Files.newBufferedReader(path, Charset.forName(charset))) {
            doParse(br);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse File", e);
        }
        return new QuestionsParsingResult(charset, header, questions, questionsParsingIssues);
    }

    @Override
    public QuestionsParsingResult parseStream(InputStream stream, String charset) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, charset))) {
            doParse(br);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse InputStream",e);
        }
        return new QuestionsParsingResult(charset, header, questions, questionsParsingIssues);
    }

    private void doParse(BufferedReader br) {
        List<String> lines = br.lines().collect(Collectors.toList());
        int sentinel = getSentinel(lines);
        this.startStatus = true;
        for (int i = sentinel; i < lines.size(); i++) {
            currentRow = i;
            currentLine = lines.get(i);
            parseLine(currentLine);
        }
    }

    /**
     * Gets currentIndex at which header ends and questions start (first line starting with '#')
     * @param lines list of lines of the source file
     * @return currentIndex in the list from which file body starts
     */
    private int getSentinel(List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();
        int sentinel = 0;
        boolean isEmpty = true;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith("#")) {
                sentinel = i;
                isEmpty = false;
                break;
            }
            stringBuilder.append(line);
        }
        if (isEmpty) throw new RuntimeException("No questions found in the file!");
        this.header = stringBuilder.toString();
        return sentinel;
    }

    /**
     * Parses the current line
     * @param line a line to be parsed for special symbols
     */
    abstract void parseLine(String line);

}

