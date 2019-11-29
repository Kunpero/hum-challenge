package rs.kunpero.humchallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ApiOption {
    private int index;

    private String description;

    private boolean isSelected;
}
