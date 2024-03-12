package kr.xit.core.spring.auth.jwt;

import egovframework.com.cmm.model.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.xit.core.consts.Constants.JwtToken;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.model.TokenDTO;
import kr.xit.core.spring.config.properties.JwtProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * description : JWT 토큰 생성, 검증
 * packageName : kr.xit.core.spring.auth
 * fileName    : JwtTokenProvider
 * author      : julim
 * date        : 2023-11-29
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-11-29    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean {
    public static final String REFRESH_HEADER = "Refresh";

    @Getter
    private Key key;
    @Getter
    private final transient JwtProperties jwtProp;

    /**
     * Bean 등록후 Key SecretKey HS256 decode
     */
    @Override
    public void afterPropertiesSet() {
        String base64EncodedSecretKey = encodeBase64SecretKey(jwtProp.getSecretKey());
        this.key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDTO generateTokenDto(final Map<String, Object> claims, final String subject) {
        Instant now = new Date().toInstant();

        Date accessTokenExpiresIn = getAccessTokenExpiration(now);
        Date refreshTokenExpiresIn = getRefreshTokenExpiration(now);

        // 반드시 setClaims 먼저 해야 한다
        String accessToken = Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date.from(now))
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        String refreshToken = Jwts.builder()
            .setSubject(subject)
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        return TokenDTO.builder()
                .id(subject)
                .grantType(JwtToken.GRANT_TYPE.getCode())
                .authorizationType(JwtToken.HEADER_NAME.getCode())
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명 입니다");
            //throw BizRuntimeException.of("fail.jwt.invalid");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰 입니다.");
            //throw BizRuntimeException.of("fail.jwt.expired");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
            //throw BizRuntimeException.of("fail.jwt.unsupported");
        } catch (IllegalArgumentException e) {
            log.info("정상적인 JWT 토큰이 아닙니다.");
            //throw BizRuntimeException.of("fail.jwt.illegalArgument");
        }
        return false;
    }

    // Token 복호화 및 예외 발생(토큰 만료, 시그니처 오류)시 Claims 객체가 안만들어짐.
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void accessTokenSetHeader(String accessToken, HttpServletResponse response) {
        String headerValue = JwtToken.GRANT_TYPE + accessToken;
        response.setHeader(JwtToken.HEADER_NAME.getCode(), headerValue);
    }

    public void refresshTokenSetHeader(String refreshToken, HttpServletResponse response) {
        response.setHeader("Refresh", refreshToken);
    }

    // Request Header에 Access Token 정보를 추출하는 메서드
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtToken.HEADER_NAME.getCode());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtToken.GRANT_TYPE.getCode())) {
            return bearerToken.substring(JwtToken.GRANT_TYPE.getCode().length() + 1);
        }
        return null;
    }

    // Request Header에 Refresh Token 정보를 추출하는 메서드
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id").toString();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getUserSeFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userSe").toString();
    }
    public String getInfoFromToken(String type, String token) {
        Claims claims = parseClaims(token);
        return claims.get(type).toString();
    }

    public Boolean validateToken(String token, LoginVO loginVO) {
        final String username = getUsernameFromToken(token);
        if(!username.equals(loginVO.getUserSe()+loginVO.getId())){
            throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
        }
        return isTokenExpired(token);
    }

    public Date getAccessTokenExpiration(final Instant now){
        return getTokenExpiration(jwtProp.getTokenExpiry(), now);
    }

    public Date getRefreshTokenExpiration(final Instant now){
        return getTokenExpiration(jwtProp.getRefreshTokenExpiry(), now);
    }

    private Date getTokenExpiration(final int expirationDay, final Instant now) {
        return Date.from(now.plus(expirationDay, ChronoUnit.DAYS));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if(expiration.before(new Date()))  throw BizRuntimeException.create(ErrorCode.EXPIRED_TOKEN);
        return true;
    }
}
