package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class QuestionDto {
    private int index;

    private String uuid;

    private String description;

    private List<OptionDto> options = new ArrayList<>();
}
