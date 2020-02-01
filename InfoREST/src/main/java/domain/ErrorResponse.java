package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter @Setter @AllArgsConstructor
public class ErrorResponse {
    private String error;
    private Integer status;
}
