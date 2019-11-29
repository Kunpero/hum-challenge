package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rs.kunpero.humchallenge.service.dto.QuestionDto;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class UpdateQuestionResponseDto {
    List<QuestionDto> questions;
}
