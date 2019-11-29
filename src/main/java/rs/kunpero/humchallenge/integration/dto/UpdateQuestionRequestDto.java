package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateQuestionRequestDto {
    private String user;

    private int questionIndex;

    private int optionIndex;
}
