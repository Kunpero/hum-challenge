package rs.kunpero.humchallenge.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiOption {
    private int index;

    private String description;

    private boolean isSelected;
}
