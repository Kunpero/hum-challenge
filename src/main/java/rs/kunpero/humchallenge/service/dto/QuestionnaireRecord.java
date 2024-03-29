package rs.kunpero.humchallenge.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class QuestionnaireRecord {
    private boolean hasNext = true;

    private List<QuestionDto> questions = new ArrayList<>();

    public boolean hasNext() {
        return hasNext;
    }

    public Optional<QuestionDto> getQuestion(int questionIndex) {
        return this.questions.stream()
                .filter(q -> q.getIndex() == questionIndex)
                .findFirst();
    }
}
