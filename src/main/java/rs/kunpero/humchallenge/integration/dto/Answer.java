package rs.kunpero.humchallenge.integration.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Answer {
    private String questionUuid;

    private String optionUuid;
}
