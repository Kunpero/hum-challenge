package rs.kunpero.humchallenge.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubmitApiResponse {
    private boolean isSuccessful;

    private String resultDescription;
}
