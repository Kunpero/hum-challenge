package rs.kunpero.humchallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class UpdateQuestionApiResponse {
    private List<ApiQuestion> questions;
}
