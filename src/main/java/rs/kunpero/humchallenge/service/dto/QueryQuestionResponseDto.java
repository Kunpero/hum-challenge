package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QueryQuestionResponseDto {

    private boolean hasNext;

    public boolean hasNext() {
        return hasNext;
    }

    private List<QuestionDto> questions;
}
