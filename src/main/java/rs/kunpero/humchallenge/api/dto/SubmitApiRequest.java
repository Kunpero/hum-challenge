package rs.kunpero.humchallenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SubmitApiRequest {
    private String user;
}
