package kr.xit.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDTO {
    private final String id;
    private final String refreshToken;
    private final Long refreshTokenExpiresIn;
}
