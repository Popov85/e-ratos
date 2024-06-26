package ua.edu.ratos.service.dto.out.question;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.edu.ratos.service.dto.out.answer.AnswerMCQOutDto;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(callSuper = true)
public class QuestionMCQOutDto extends QuestionOutDto {

    private boolean single;

    private List<AnswerMCQOutDto> answers = new ArrayList<>();

    public void addAnswer(AnswerMCQOutDto answer) {
        this.answers.add(answer);
    }
}
