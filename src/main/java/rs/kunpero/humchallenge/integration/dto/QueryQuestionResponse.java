package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryQuestionResponse {

    private Question question;

    private boolean hasNext;

    @Data
    @AllArgsConstructor
    public static class Question {
        private int index;

        private String description;

        private List<Option> options;
    }
}
