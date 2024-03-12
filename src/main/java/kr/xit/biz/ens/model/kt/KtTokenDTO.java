package kr.xit.biz.ens.model.kt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.kt.KtCommonDTO.KtCommonResponse;
import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : KT 공인 알림문자 토큰발행 DTO
 *               - 토큰발행
 *                 Request: {@link KtTokenRequest}
 *                 Response: {@link KtTokenResponse}
 *               - Token 인증 확인 조회(BC-AG-SN-008)
 *                 Request: {@link KtTokenConfirmRequest}
 *                 Response: {@link KtTokenConfirmResponse}
 *               - 열람확인결과전송(BC-AG-SN-009)
 *                 Request : {@link KtTokenReadRequest}
 *                 Response : {@link KtCommonResponse}
 *               - 기관정산화면연계 토큰인증(BC-AG-SM-002)
 *                 Request : {@link KtTokenExcaRequest}
 *                 Response : {@link KtCommonResponse}
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : KtTokenDTO
 * author      : limju
 * date        : 2023-09-22
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-22    limju       최초 생성
 *
 * </pre>
 */
public class KtTokenDTO {
    /**
     * <pre>
     * Token 발행
     * Request : KtTokenRequest
     * Response : KtTokenResponse
     * </pre>
     */
    @Schema(name = "KtTokenRequest", description = "KT MMS 토큰발행 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtTokenRequest {

        /**
         * 권한부여방식 : 'clinet_credentials' 로 고정
         */
        @Default
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "권한부여방식", example = "clinet_credentials")
        @NotEmpty(message = "권한부여방식은 필수 입니다")
        @Size(max = 100)
        private String grantType = "client_credentials";

        /**
         * 과태료 코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "클라이언트 ID", example = " ")
        @NotEmpty(message = "클라이언트 ID는 필수 입니다")
        @Size(max = 500)
        private String clientId;

        /**
         * 클라이언트 secret
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "클라이언트 secret", example = " ")
        @NotEmpty(message = "클라이언트 secret는 필수 입니다")
        @Size(max = 500)
        private String clientSecret;

        /**
         * <pre>
         * 접근토큰 권한범위 : ag.api|pf.api
         * </pre>
         */
        @Default
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "권한범위", example = "ag.api")
        @NotEmpty(message = "권한범위는 필수 입니다")
        @Length(max = 100)
        private String scope = "ag.api";
    }

    @Schema(name = "KtTokenResponse", description = "KT MMS 토큰발행 요청 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtTokenResponse implements IApiResponse {
        //-------------------------------------------------------------------
        // 토큰 발행 성공시 필수
        //-------------------------------------------------------------------
        /**
         * 접근토큰 : 성공시 필수
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "접근 토큰", example = " ")
        @Size(max = 1000)
        private String accessToken;

        /**
         * <pre>
         * 접근 토큰 유형 : 성공시 필수
         * bearer 고정
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "접근 토큰 유형", example = "bearer")
        @Size(max = 100)
        private final String tokenType = "bearer";

        /**
         * 접근 토큰 유효 기간 : 성공시 필수
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "접근 토큰 유효 기간", example = " ")
        @Size(max = 100)
        private String expiresIn;

        /**
         * <pre>
         * 접근토큰 권한범위 : 성공시 필수
         * 또는, 실패시 scope에러인 경우 발생
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "권한범위|scope에러인 경우만 발생", example = "ag.api")
        @Size(max = 100)
        private String scope;

        /**
         * 접근토큰 식별자 : 성공시 필수
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "접근토큰 식별자", example = "ag.api")
        @Size(max = 100)
        private String jti;
        //-------------------------------------------------------------------

        //-------------------------------------------------------------------
        // 토큰 발행 실패시 필수
        //-------------------------------------------------------------------
        /**
         * <pre>
         * 접근 토큰 유형 : 성공시 필수
         * bearer 고정
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "에러코드", example = " ")
        @Size(max = 100)
        private String error;

        /**
         * 접근 토큰 유효 기간 : 성공시 필수
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "에러메세지", example = " ")
        @Size(max = 100)
        private String errorDescription;
    }
    //----------------------------------------------------------------------------------------

    /**
     * <pre>
     * Token 인증 확인 조회 : BC-AG-SN-008
     * Request : KtTokenConfirmRequest
     * Response : KtTokenConfirmResponse
     * </pre>
     */
    @Schema(name = "KtTokenConfirmRequest", description = "KT 토큰 인증 확인 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtTokenConfirmRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 서비스코드 : 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = " ")
        @NotEmpty(message = "서비스코드는 필수 입니다(max:20)")
        private String serviceCd;

        /**
         * <pre>
         * 서비스 코드 인증키 : 필수 - 16 자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스 코드 인증키", example = " ")
        @Size(min = 16, max = 16, message = "서비스 코드 인증키는 필수 입니다(16자리)")
        private String serviceKey;

        /**
         * <pre>
         * 토큰 : 필수 - max 150
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "토큰", example = " ")
        @NotEmpty(message = "토큰은 필수 입니다")
        private String accessToken;
    }

    @Schema(name = "KtTokenConfirmResponse", description = "KT 토큰 인증 확인 요청 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtTokenConfirmResponse extends KtCommonResponse {
        /**
         * <pre>
         * 관리키 : 고객 메시지 건별 Unique한 키 - max 50
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키")
        private String srcKey;
    }

    /**
     * <pre>
     * 열람확인결과전송 : BC-AG-SN-009
     * Request : KtTokenReadRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtTokenReadRequest", description = "KT 열람확인 결과 전송 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = true)
    public static class KtTokenReadRequest extends KtTokenConfirmRequest {
        /**
         * <pre>
         * 서비스코드 : 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "열람타임스탬프", example = " ")
        @NotEmpty(message = "열람타임스탬프는 필수입니다(max:20)")
        private String mmsRdgTmst;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 기관정산화면연계 토큰인증 : BC-AG-SM-002
     * Request : KtTokenExcaRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtTokenExcaRequest", description = "KT 기관정산화면연계 토큰 인증 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtTokenExcaRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 엑세스토큰 : 필수 - 100
         * 기관정산화면 호출 시 전달 받은 토큰
         * 1회 인증시 만료처리
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "엑세스토큰", example = " ")
        @NotEmpty(message = "엑세스토큰은 필수 입니다")
        private String accessToken;
    }
    //-------------------------------------------------------------------
}
