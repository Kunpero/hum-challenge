package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitQuestionnaireResponse {
    private boolean isSuccessful;

    private String resultDescription;
}
