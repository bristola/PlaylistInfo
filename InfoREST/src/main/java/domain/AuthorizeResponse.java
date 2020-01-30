package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter @Setter @AllArgsConstructor
public class AuthorizeResponse {
    private String accessToken;
    private String refreshToken;
}
