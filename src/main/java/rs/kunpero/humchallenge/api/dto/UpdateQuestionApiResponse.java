package rs.kunpero.humchallenge.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateQuestionApiResponse {
    private List<ApiQuestion> questions;
}
