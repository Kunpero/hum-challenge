package rs.kunpero.humchallenge.api.dto;

import lombok.Data;

@Data
public class UpdateQuestionApiRequest {
    private String user;

    private int questionIndex;

    private int optionIndex;
}
