package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SubmitQuestionnaireRequest {
    private List<Answer> answers;
}
