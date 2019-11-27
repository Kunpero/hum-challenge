package rs.kunpero.humchallenge.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionnaireRecord {
    private boolean hasNext;

    private List<QuestionDto> questions = new ArrayList<>();
}
