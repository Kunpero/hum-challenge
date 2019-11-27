package rs.kunpero.humchallenge.integration.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryQuestionResponse {
    private String uuid;

    private String description;

    private List<Option> options;

    private boolean hasNext;
}
