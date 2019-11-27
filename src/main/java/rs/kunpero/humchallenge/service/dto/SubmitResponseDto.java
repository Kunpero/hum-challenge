package rs.kunpero.humchallenge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubmitResponseDto {
    private boolean isSuccessful;

    private String resultDescription;
}
