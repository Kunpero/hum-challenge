package rs.kunpero.humchallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class QueryQuestionApiResponse {
    // TODO: 2019-11-29 has
    private boolean hasNext;

    private List<ApiQuestion> questions;
}
