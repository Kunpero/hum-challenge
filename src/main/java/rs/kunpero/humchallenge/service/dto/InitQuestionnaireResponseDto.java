package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InitQuestionnaireResponseDto {
    private String questionnaireDescription;
}
