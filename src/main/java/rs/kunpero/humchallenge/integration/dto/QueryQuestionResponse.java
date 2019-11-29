package rs.kunpero.humchallenge.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
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
