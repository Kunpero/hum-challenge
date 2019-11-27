package rs.kunpero.humchallenge.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubmitResponse {
    private boolean isSuccessful;

    private String resultDescription;
}
