package rs.kunpero.humchallenge.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QueryQuestionApiResponse {
    private int index;

    private String description;

    private boolean hasNext;

    private List<ApiOption> options;
}
