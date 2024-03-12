package kr.xit.core.spring.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.token")
public class JwtProperties {

    private String typ;
    private String grant;
    private String alg;
    private String issuer;
    private String audience;
    private String secretKey;
    // minute
    private int tokenExpiry;
    // day
    private int refreshTokenExpiry;
    private String saveType;
}
