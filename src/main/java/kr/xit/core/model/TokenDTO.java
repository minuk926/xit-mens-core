package kr.xit.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    private final String id;
    private final String grantType;
    private final String authorizationType;
    private final String accessToken;
    private final String refreshToken;
    private final Long accessTokenExpiresIn;
    private final Long refreshTokenExpiresIn;
}
