package rs.kunpero.humchallenge.integration.dto;

import lombok.Data;

@Data
public class SubmitQuestionnaireResponse {
    private boolean isSuccessful;

    private String resultDescription;
}
