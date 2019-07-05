package ua.edu.ratos.service.dto.out;

import lombok.*;
import lombok.experimental.Accessors;
import ua.edu.ratos.service.parsers.QuestionsParsingIssue;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsParsingResultOutDto {
    private String charset;
    private int questions;
    private int issues;
    private int majorIssues;
    private int mediumIssues;
    private int minorIssues;
    private List<QuestionsParsingIssue> allIssues = new ArrayList<>();
    // Specifies if the totalByType were saved to DB
    private boolean saved;
}