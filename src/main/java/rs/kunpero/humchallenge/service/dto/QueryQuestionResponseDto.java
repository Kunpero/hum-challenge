package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QueryQuestionResponseDto {
    private boolean hasNext;

    private QuestionDto question;
}
