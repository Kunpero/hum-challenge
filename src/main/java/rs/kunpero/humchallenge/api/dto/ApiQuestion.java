package rs.kunpero.humchallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class ApiQuestion {
    private int index;

    private String description;

    private List<ApiOption> options;
}
