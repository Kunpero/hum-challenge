package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OptionDto {
    private int index;

    private String description;

    private boolean isSelected;
}
