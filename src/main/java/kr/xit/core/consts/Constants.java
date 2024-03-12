package kr.xit.core.consts;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;

/**
 * <pre>
 * description : 상수 정의
 * packageName : kr.xit.core.const
 * fileName    : Constants
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
public class Constants {
    public static final String API_URL_PATTERNS = "/*";
    public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    public static final String DEFAULT_VIEW = "mappingJackson2JsonView";

    public static final String PRIMARY_DATA_SOURCE = "primaryDataSource";
    public static final String SECONDARY_DATA_SOURCE = "secondaryDataSource";
    public static final String PRIMARY_SQL_SESSION = "primarySqlSession";
    public static final String SECONDARY_SQL_SESSION = "secondarySqlSession";

    /**
     * 인증정보 저장(세션)
     */
    public enum AuthSaveSession {
        LOGIN_VO("LoginVO")
        ;

        private final String code;

        AuthSaveSession(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }

    /**
     * 인증정보 저장 방식
     *
     */
    public enum AuthSaveType {
        SECURITY("security"),	// SessionCreationPolicy.STATELESS인 경우는 SecurityContext 사용불가
        SESSION("session"),   // TOKEN도 사용 가능은 하지만...
        HEADER("header");     // TOKEN

        private final String code;

        AuthSaveType(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }

    public enum JwtToken {
        // 토큰헤더명,
        HEADER_NAME("Authorization"),
        GRANT_TYPE("Bearer"),
        ACCESS_TOKEN_NAME("accessToken"),
        REFRESH_TOKEN_NAME("refreshToken"),
        AUTHORITIES_KEY("role"),
        TOKEN_USER_NAME("userName"),
        TOKEN_USER_MAIL("userEmail"),
        TOKEN_USER_ID("userId");

        private final String code;

        JwtToken(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }

    /**
     * JWT Token 통신 방식 지정
     * COOKIE : accessToken - header, refreshToken - cookie
     * HEADER : accessToken - header, refreshToken - DTO
     * DTO : accessToken - header, refreshToken - DTO
     */
    public enum JwtTokenParamType {
        COOKIE,
        HEADER,
        DTO
    }

    /**
     * 카카오페이 전자문서 요청 헤더 필드명
     */
    public enum HeaderName {
        TOKEN(HttpHeaders.AUTHORIZATION),	 //Token
        UUID("Contract-Uuid"),   //Contract-Uuid
        ;     // TOKEN

        private final String code;

        HeaderName(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
