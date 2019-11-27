package rs.kunpero.humchallenge.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QueryQuestionApiResponse {

    private boolean hasNext;

    private List<ApiQuestion> questions;
}
