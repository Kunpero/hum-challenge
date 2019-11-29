package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SubmitQuestionnaireResponse {
    private boolean isSuccessful;

    private String resultDescription;
}
