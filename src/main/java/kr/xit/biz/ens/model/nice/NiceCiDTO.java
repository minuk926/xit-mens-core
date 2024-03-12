package kr.xit.biz.ens.model.nice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import kr.xit.core.model.AuditFields;
import kr.xit.core.model.IApiResponse;
import kr.xit.core.support.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : Nice CI DTO
 *
 * packageName : kr.xit.biz.ens.model.nice
 * fileName    : NiceCiDTO
 * author      : limju
 * date        : 2023-09-06
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-06    limju       최초 생성
 *
 * </pre>
 */
public class NiceCiDTO {

    @Schema(name = "NiceCiRequest", description = "Nice CI 공통 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NiceCiRequest {
        /**
         * 시군구 코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "시군구코드", example = "88328")
        @NotEmpty(message = "시군구 코드는 필수 입니다")
        private String signguCode;

        /**
         * 과태료 코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "과태료코드", example = "11")
        @NotEmpty(message = "과태료 코드는 필수 입니다")
        private String ffnlgCode = "11";

        /**
         * 주민Id
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "주민번호", example = " ")
        @Size(max = 13, message = "주민번호는 13자리 입니다.")
        private String juminId;
    }

    //--------------------------------------------------------------------------------
    // 기관용 Token
    //--------------------------------------------------------------------------------
    /**
     * <pre>
     * 기관용 Token(50년 유효) 발급 요청
     * Json data : SNAKE_CASE (grant_type <-> grantType)
     * </pre>
     */
    @Schema(name = "NiceTokenRequest", description = "Nice 기관용 Token(50년 유효) 발급 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class NiceTokenRequest {
        /**
         * default 로 고정
         */
        @Default
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "scope", example = "default")
        @NotEmpty(message = "scope는 필수입니다")
        private String scope = "default";

        /**
         * clinet_credentials 로 고정
         */
        @Default
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "grant_type", example = "client_credentials")
        @NotEmpty(message = "grant_type은 필수입니다")
        private String grantType = "client_credentials";
    }

    /**
     * <pre>
     * 기관용 Token(50년 유효) 발급 요청 응답
     * url : /digital/niceid/oauth/oauth/token
     * content-type : application/json
     * </pre>
     */
    @Schema(name = "NiceTokenResponse", description = "Nice 기관용 Token(50년 유효) 발급 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NiceTokenResponse implements IApiResponse {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private ResponseDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private TokenResDataBody dataBody;
    }

    /**
     * <pre>
     * 기관용 Token 폐기 응답
     * </pre>
     */
    @Schema(name = "TokenRevokeResponse", description = "기관용 Token 폐기 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TokenRevokeResponse implements IApiResponse {

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private ResponseDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private TokenRevokeResDataBody dataBody;
    }

    /**
     * <pre>
     * 기관용 토큰 발급 결과 dataBody
     * Json data : SNAKE_CASE (access_token <-> accessToken)
     * </pre>
     */
    @Schema(name = "TokenResDataBody", description = "기관용 토큰 발급 결과 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TokenResDataBody {
        /**
         * <pre>
         * 사용자 엑세스 토큰 값 : token 발급시 필수
         * 모든 API 요청시 헤더에 access_token을 포함하여 전송
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "사용자 엑세스 토큰 값", example = " ")
        private String accessToken;

        /**
         * token_type : token 발급시 필수
         * bearer로 고정
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "token_type", example = "bearer")
        private String tokenType;

        /**
         * access 토큰 만료 시간(초) : token 발급시 필수
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "access 토큰 만료 시간(초)", example = "1.57698305E9")
        private Integer expiresIn;

        /**
         * <pre>
         * 요청한 scope 값 : token 발급시 필수
         * 기본 : default
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "요청한 scope 값", example = "default")
        private String scope;
    }

    /**
     * <pre>
     * 기관용 토큰 폐기 결과 dataBody
     * </pre>
     */
    @Schema(name = "TokenRevokeResDataBody", description = "기관용 토큰 폐기 결과 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class TokenRevokeResDataBody implements IApiResponse{
        /**
         * <pre>
         * 토큰폐기결과
         * true or false
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "토큰폐기결과", example = "true")
        private boolean result;
    }
    //--------------------------------------------------------------------------------
    // 기관용 Token
    //--------------------------------------------------------------------------------


    //--------------------------------------------------------------------------------
    // 공개키 : publickey
    //--------------------------------------------------------------------------------
    /**
     * <pre>
     * 공개키(publickey) 요청
     * </pre>
     */
    @Schema(name = "PublickeyRequest", description = "공개키(publickey) 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PublickeyRequest {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private RequestDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private PublickeyReqDataBody dataBody;
    }

    /**
     * <pre>
     * 공개키(publickey) 발급 요청 결과
     * </pre>
     */
    @Schema(name = "PublickeyResponse", description = "공개키(publickey) 발급 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PublickeyResponse implements IApiResponse{
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private ResponseDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private PublickeyResDataBody dataBody;
    }

    /**
     * <pre>
     * 공개키(publickey) 요청 dataBody DTO
     * Json data : SNAKE_CASE (req_dtim <-> reqDtim)
     * </pre>
     */
    @Schema(name = "PublickeyReqDataBody", description = "공개키(publickey) 요청 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PublickeyReqDataBody {
        /**
         * 공개키 요청일시 (YYYYMMDDHH24MISS)
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "공개키 요청일시", example = "20230906121259")
        @Size(min = 14, max = 14, message = "요청일시(req_dtim)는 필수 입니다(14자리)")
        private String reqDtim;
    }

    /**
     * <pre>
     * 공개키(publickey) 요청 결과 dataBody DTO
     * Json data : SNAKE_CASE (rsp_cd <-> rspCd)
     *
     * rsp_cd가 P000일 때
     * result_cd(상세결과코드) : 4자리
     * - 0000: 공개키 발급
     * - 0001: 필수입력값 오류
     * - 0003: 공개키 발급 대상 회원사 아님
     * - 0099: 기타오류
     * </pre>
     */
    @Schema(name = "PublickeyResDataBody", description = "공개키(publickey) 요청 결과 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = true)
    public static class PublickeyResDataBody extends CommonResponseDataBody {
        /**
         * <pre>
         * 사이트코드 : 16
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "사이트 코드", example = " ")
        private String siteCode;

        /**
         * <pre>
         * 공개키 버저 : 50
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "공개키 버전", example = " ")
        private String keyVersion;

        /**
         * <pre>
         * 공개키 : 최대 1000
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "공개키", example = " ")
        private String publicKey;

        /**
         * <pre>
         * 공개키 만료일시 (YYYYMMDDHH24MISS) : 14자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "공개키 만료일시", example = " ")
        private String validDtim;
    }
    //--------------------------------------------------------------------------------
    // 공개키 : publickey
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // 대칭키 : symmetrickey
    //--------------------------------------------------------------------------------
    /**
     * <pre>
     * 대칭키(symmetrickey) 등록 요청
     * </pre>
     */
    @Schema(name = "SymmetrickeyRequest", description = "대칭키(symmetrickey) 등록 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SymmetrickeyRequest {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private RequestDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private SymmetrickeyReqDataBody dataBody;
    }

    /**
     * <pre>
     * 대칭키(symmetrickey) 등록 요청 결과
     * </pre>
     */
    @Schema(name = "SymmetrickeyResponse", description = "대칭키(symmetrickey) 등록 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SymmetrickeyResponse implements IApiResponse {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private ResponseDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private SymmetrickeyResDataBody dataBody;
    }

    /**
     * <pre>
     * 대칭키(symmetrickey) 등록 요청 dataBody DTO
     * Json data : SNAKE_CASE (req_dtim <-> reqDtim)
     * </pre>
     */
    @Schema(name = "SymmetrickeyReqDataBody", description = "대칭키(symmetrickey) 등록 요청 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SymmetrickeyReqDataBody {
        /**
         * </pre>
         * 공개키버전
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "공개키버전", example = "20210121ca8c1612-2c2d-IPaa-aad1-xxxxxxxxxxxx")
        @NotEmpty(message = "공개키버전은 필수 입니다(max:50)")
        @Size(max = 50)
        private String pubkeyVersion;

        /**
         * <pre>
         * json암호화값(회원사에서 생성한 대칭키를 공개키로 암호화한 값)
         * 암호화알고리즘 : RSA/ECB/PKCS1Padding, X509EncodedKeySpec 사용, Base64 Encoding
         * site_code - 16 : 공개키요청시 수신한 사이트 코드
         * request_no - 30 : 이용기관에서 임의 생성한 값
         * key - 32 : 회원사에서 사용할 암호화 KEY 세팅
         *            32byte AES256 bits, 16byte AES128 bits
         *            NICE에 KEY등록 후 최대 6개월 내 갱신 필요
         * iv - 16 : Inital Vector값
         * hmac_key - 32 : 화원사에서 사용할 HMAC KEY
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "json암호화값(회원사에서 생성한 대칭키를 공개키로 암호화한 값)", example = "SDFWASDFASDFSDFASDFASDFASD=")
        @NotEmpty(message = "대칭키(암호화)는 필수 입니다")
        @Size(max = 512)
        private String symkeyRegInfo;
    }

    /**
     * <pre>
     * 대칭키(symmetrickey) 등록 요청 결과 dataBody DTO
     * Json data : SNAKE_CASE (rsp_cd <-> rspCd)
     *
     * rsp_cd가 P000일 때
     * result_cd(상세결과코드) : 4자리
     * - 0000: 대칭키 발급
     * - 0001: 공개키 기간 만료
     * - 0002: 공개키를 찾을 수 없음
     * - 0003: 공개키를 발급한 회원사 아님
     * - 0004: 복호화 오류
     * - 0005: 필수입력값 오류 (key_version, key_info 필수값 확인)
     * - 0006: 대칭키 등록 가능 회원사 아님
     * - 0007: key 중복 오류 (현재 및 직전에 사용한 Key 사용 불가)
     * - 0008: 요청사이트코드와 공개키발급 사이트코드 다름
     * - 0099: 기타오류
     * </pre>
     */
    @Schema(name = "SymmetrickeyResDataBody", description = "대칭키(symmetrickey) 등록 결과 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = true)
    public static class SymmetrickeyResDataBody extends CommonResponseDataBody {
        /**
         * <pre>
         * JSON값
         * - 회원사에 생성되어있는 대칭키 버전별 유효기간
         * - result_cd 0000, 0007일 경우 나감
         * cur_symkey_version - 50 : 현재 등록요청한 대칭키 버전
         * cur_valid_dtim - 14 : 현재 등록된 대칭키 만료일시 (YYYYMMDDHH24MISS)
         * bef_symkey_version - 50 : 이전 등록된 대칭키 버전
         * bef_valid_dtim - 14 : 이전 등록된 대칭키 만료일시 (YYYYMMDDHH24MISS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "사이트 코드", example = " ")
        private String symkeyStatInfo;
    }

    @Schema(name = "SymkeyRegInfo", description = "대칭키(symmetrickey) 등록 요청시 symkey_reg_info JSON 속성")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SymkeyRegInfo {
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "사이트 코드", description = "공개키요청시 수신한 사이트코드", example = "AAA==")
        @NotEmpty(message = "사이트 코드는 필수 입니다(max:16)")
        @Size(max = 16)
        private String siteCode;

        @Schema(requiredMode = RequiredMode.REQUIRED, title = "요청고유번호", description = "이용기관에서 생성한 임의의 값", example = " ")
        @NotEmpty(message = "요청고유번호는 필수 입니다(max:30)")
        @Size(max = 30)
        private String requestNo;

        @Schema(requiredMode = RequiredMode.REQUIRED, title = "암호화키", description = "사용할 암호화키", example = " ")
        @NotEmpty(message = "사이트 코드는 필수 입니다(max:32)")
        @Size(max = 32)
        private String key;

        @Schema(requiredMode = RequiredMode.REQUIRED, title = "iv", description = "inital Vector", example = " ")
        @NotEmpty(message = "iv는 필수 입니다(max:16)")
        @Size(max = 16)
        private String iv;

        @Schema(requiredMode = RequiredMode.REQUIRED, title = "hmac_key", description = "사용할 HMAC KEY", example = " ")
        @NotEmpty(message = "iv는 필수 입니다(max:32)")
        @Size(max = 32)
        private String hmacKey;
    }

    @Schema(name = "SymkeyStatInfo", description = "대칭키(symmetrickey) 등록 결과 symkey_stat_info JSON 속성")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SymkeyStatInfo {
        @Schema(requiredMode = RequiredMode.AUTO, title = "cur_symkey_version", description = "현재 등록 요청한 대칭키 버전", example = " ")
        @Size(max = 50)
        private String curSymkeyVersion;

        @Schema(requiredMode = RequiredMode.AUTO, title = "cur_valid_dtim", description = "현재 등록된 대칭키 만료일시 (YYYYMMDDHH24MISS)", example = " ")
        @Size(max = 14)
        private String curValidDtim;

        @Schema(requiredMode = RequiredMode.AUTO, title = "bef_symkey_version", description = "이전 등록된 대칭키 버전", example = " ")
        @Size(max = 50)
        private String befSymkeyVersion;

        @Schema(requiredMode = RequiredMode.AUTO, title = "bef_valid_dtim", description = "이전 등록된 대칭키 만료일시 (YYYYMMDDHH24MISS)", example = " ")
        @Size(max = 14)
        private String befValidDtim;
    }
    //--------------------------------------------------------------------------------
    // 대칭키 : symmetrickey
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    // 아이핀 CI 요청
    //--------------------------------------------------------------------------------
    @Schema(name = "IpinCiRequest", description = "IPIN CI 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IpinCiRequest {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private RequestDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private IpinCiReqDataBody dataBody;
    }

    @Schema(name = "IpinCiResponse", description = "IPIN CI 요청 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IpinCiResponse implements IApiResponse {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private ResponseDataHeader dataHeader;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private IpinCiResDataBody dataBody;
    }

    /**
     * <pre>
     * 대칭키(symmetrickey) 등록 요청 dataBody DTO
     * Json data : SNAKE_CASE (req_dtim <-> reqDtim)
     * </pre>
     */
    @Schema(name = "IpinCiReqDataBody", description = "IPIN CI 요청 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IpinCiReqDataBody {
        /**
         * </pre>
         * 대칭키 버전 : 대칭키 수신시 받은 버전 set
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "대칭키버전", example = "20210121ca8c1612-2c2d-IPaa-aad1-xxxxxxxxxxxx")
        @NotEmpty(message = "대칭키 버전은 필수 입니다")
        @Size(max = 50)
        private String symkeyVersion;

        /**
         * <pre>
         * JSON암호화값
         * - 요청정보를 회원사에서 생성한 대칭키로 암호화한 값
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "json암호화값(회원사에서 생성한 대칭키를 공개키로 암호화한 값)", example = "SDFWASDFASDFSDFASDFASDFASD=")
        @NotEmpty(message = "encode data는 필수입니다(JSON암호화)")
        @Size(max = 1024)
        private String encData;

        /**
         * <pre>
         * 무결성체크를 위해 enc_data를 HMAC처리 후, Base64 인코딩한 값
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "integrity_value", example = "SDFWASDFASDFSDFASDFASDFASD=")
        @NotEmpty(message = "encode data는 필수입니다(JSON암호화)")
        @Size(max = 44)
        private String integrityValue;
    }

    @Schema(name = "IpinCiResDataBody", description = "IPIN CI 요청 결과 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IpinCiResDataBody extends CommonResponseDataBody {
        /**
         * <pre>
         * JSON암호화값(P000일때 나감)
         * - 응답정보를 회원사에서 요청시 전달한 대칭키로 암호화한 값
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "JSON암호화값(P000일때 나감)", example = "SDFWASDFASDFSDFASDFASDFASD=")
        @Size(max = 1024)
        private String encData;

        /**
         * <pre>
         * 무결성체크를 위해 enc_data를 HMAC처리 후, Base64 인코딩한 값
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "integrity_value", example = "SDFWASDFASDFSDFASDFASDFASD=")
        @Size(max = 44)
        private String integrityValue;

        public IpinCiResEncData toEncData(){
            return JsonUtils.toObject(this.encData, IpinCiResEncData.class);
        }
    }

    @Schema(name = "IpinCiReqEncData", description = "IPIN CI 요청 enc_data JSON 속성")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IpinCiReqEncData {

        /**
         * 공개키요청시 수신한 사이트코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "사이트 코드", description = "공개키 요청시 수신한 사이트코드", example = " ")
        @NotEmpty(message = "사이트 코드는 필수 입니다(max:16)")
        @Size(max = 16)
        private String siteCode;

        /**
         * 정보요청유형 : 1 - CI제공
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "정보요청유형", description = "정보요청유형", example = "1")
        @NotEmpty(message = "정보요청유형은 필수 입니다(1:CI요청)")
        private final String infoReqType = "1";

        /**
         * 주민번호 13자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "주민번호", description = "주민번호13자리", example = " ")
        @NotEmpty(message = "주민번호(13자리)는 필수 입니다")
        private String juminId;

        /**
         * 이용기관에서 서비스에 대한 요청거래를 확인하기 위한 고유값
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "요청번호", description = "이용기관에서 서비스에 대한 요청거래를 확인하기 위한 고유값", example = " ")
        @NotEmpty(message = "요청번호는 필수 입니다(30자리)")
        private String reqNo;

        /**
         * 거래요청시간 : YYYYMMDDHH24MISS
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "거래요청시간", description = "거래요청시간(YYYYMMDDHH24MISS)", example = " ")
        @NotEmpty(message = "거래요청시간(14자리)은 필수 입니다")
        private String reqDtim;

        /**
         * 서비스 이용 사용자 IP
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "서비스 이용 사용자 IP", description = "서비스 이용 사용자 IP", example = " ")
        @Size(max = 15, message = "서비스 이용 사용자 IP는 최대 15자리 입니다.")
        private String clientIp;
    }

    @Schema(name = "IpinCiResEncData", description = "IPIN CI 요청 결과 enc_data JSON 속성")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    public static class IpinCiResEncData {
        /**
         * <pre>
         * 연계정보1
         * - Connection Info로 다른 웹사이트간 고객확인용으로 사용
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "연계정보1", description = "연계정보1", example = " ")
        @Size(max = 88)
        private String ci1;

        /**
         * <pre>
         * 연계정보2
         * - 연계정보1의 Key 유출에 대비한 예비값
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "연계정보2", description = "연계정보2", example = " ")
        @Size(max = 88)
        private String ci2;

        /**
         * <pre>
         * 갱신횟수
         * - 연계정보 Key 유출될 경우 갱신 횟수 (초기값 1세팅)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "갱신횟수", description = "갱신횟수", example = " ")
        @Size(max = 1)
        @JsonProperty("updt_cnt")
        private String updtCnt;

        /**
         * <pre>
         * 거래고유번호
         * - result_cd가 0000일 경우 NICE에서 제공하는 거래일련번호
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "거래고유번호", description = "거래고유번호", example = " ")
        @Size(max = 30)
        @JsonProperty("tx_unique_no")
        private String txUniqueNo;
    }
    //--------------------------------------------------------------------------------
    // 아이핀 CI 요청
    //--------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------
    /**
     * <pre>
     * Request dataHeader
     * Json data : UPPER_SNAKE_CASE (GW_RSLT_CD <-> gwRsltCd)
     * </pre>
     */
    @Schema(name = "RequestDataHeader", description = "Nice CI request dataHeader(공통)")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
    public static class RequestDataHeader {

        /**
         * <pre>
         * TRAN_ID : 요청한값 그대로 return
         * 고유번호 : 최대 24
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "TRAN_ID", example = "20230906120000")
        @Size(max = 24, message = "TRAN_ID는 24자를 넘을 수 없습니다.")
        private String tranId;

        /**
         * CNTY_CD : 요청한값 그대로 return
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "CNTY_CD", example = "ko")
        @Size(max = 2, message = "CNTY_CD는 2자를 넘을 수 없습니다.")
        private String cntyCd;
    }


    /**
     * <pre>
     * Response dataHeader
     * Json data : UPPER_SNAKE_CASE (GW_RSLT_CD <-> gwRsltCd)
     * </pre>
     */
    @Schema(name = "ResponseDataHeader", description = "Nice CI Response dataHeader(공통)")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
    public static class ResponseDataHeader {
        /**
         * <pre>
         * 결과코드 : 4자리
         * 정상 : 1200, 그외 오류
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "결과코드", example = "1200")
        @Size(min = 1, max = 8)
        private String gwRsltCd;

        /**
         * 결과메세지 : 200
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "결과메세지")
        @Size(min = 1, max = 200)
        private String gwRsltMsg;

        /**
         * <pre>
         * TRAN_ID : 요청한값 그대로 return
         * 고유번호 : 최대 24
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "TRAN_ID")
        private String tranId;

        /**
         * <pre>
         * CNTY_CD : 요청한값 그대로 return
         * 2자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "CNTY_Cd")
        private String cntyCd;
    }

    @Schema(name = "CommonResponseDataBody", description = "공통 요청 결과 dataBody DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CommonResponseDataBody {
        /**
         * dataBody 정상처리여부 (P000 성공, 이외 모두 오류) : 8자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "정상처리 여부")
        private String rspCd;

        /**
         * <pre>
         * rsp_cd가 "EAPI"로 시작될 경우 오류 메시지 세팅
         * 최대 200
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "상세 메세지")
        private String resMsg;

        /**
         * <pre>
         * rsp_cd가 P000일 때 상세결과코드 : 4자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "상세 결과코드")
        private String resultCd;
    }

    /**
     * Nice 계정 인증 정보 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class NiceCiInfo extends AuditFields implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 시군구 코드
         */
        private String signguCode;
        /**
         * 과태료 코드
         */
        private String ffnlgCode;
        /**
         * 전자문서중계구분코드
         */
        private String clientId;
        /**
         * App Security code
         */
        private String clientSecret;
        /**
         * 상품코드
         */
        private String productId;
        /**
         * access token
         */
        private String accessToken;
        /**
         * access token 만료일시(초)
         */
        private Integer expiresIn;
        /**
         * token type-bearer
         */
        private String tokenType;
        /**
         * token scope-default
         */
        private String scope;
        /**
         * 공개키 site code
         */
        private String siteCode;
        /**
         * 공개키 버전
         */
        private String keyVersion;
        /**
         * 공개키
         */
        private String publicKey;
        /**
         * 공개키 만료일시
         */
        private String validDtim;
        /**
         * 현재 대칭키 버전
         */
        private String curSymkeyVersion;
        /**
         * 현재 대칭키 만료일시
         */
        private String curSymkeyValidDtim;
        /**
         * 현재 대칭키 key
         */
        private String curSymkeyKey;
        /**
         * 현재 대칭키 iv
         */
        private String curSymkeyIv;
        /**
         * 현재 대칭키 hmac_key
         */
        private String curSymkeyHmacKey;
        /**
         * 이전 대칭키 버전
         */
        private String befSymkeyVersion;
        /**
         * 이전 대칭키 만료일시
         */
        private String befSymkeyValidDtim;
        /**
         * 이전 대칭키 key
         */
        private String befSymkeyKey;
        /**
         * 이전 대칭키 iv
         */
        private String befSymkeyIv;
        /**
         * 이전 대칭키 hmac_key
         */
        private String befSymkeyHmacKey;
    }
}
