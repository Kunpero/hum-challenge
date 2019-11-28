package rs.kunpero.humchallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateQuestionApiRequest {
    private String user;

    private int questionIndex;

    private int optionIndex;
}
