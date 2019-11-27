package rs.kunpero.humchallenge.integration.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import rs.kunpero.humchallenge.service.dto.QuestionDto;

import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateQuestionResponseDto {
    List<QuestionDto> questions;
}
