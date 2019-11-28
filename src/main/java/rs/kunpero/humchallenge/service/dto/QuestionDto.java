package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
public class QuestionDto {
    private int index;

    private String description;

    private List<OptionDto> options = new ArrayList<>();

    public Optional<OptionDto> getOption(int optionIndex) {
        return this.options.stream()
                .filter(o -> o.getIndex() == optionIndex)
                .findFirst();
    }
}
