package rs.kunpero.humchallenge.integration.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateQuestionRequestDto {
    private String user;

    private int questionIndex;

    private int optionIndex;
}
