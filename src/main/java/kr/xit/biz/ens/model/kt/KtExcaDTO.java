package kr.xit.biz.ens.model.kt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.kt.KtCommonDTO.KtCommonResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : KT 공인 알림문자 정산 DTO
 *               - 기관정산화면연계 토큰인증(BC-AG-SM-002)
 *                 Request : {@link KtTokenExcaRequest}
 *                 Response : {@link KtCommonResponse}
 *               - 정산연계 자료 조회 : BC-AG-EC-001
 *                 Request : {@link KtExcaRequest}
 *                 Response : {@link KtExcaResponse}
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
public class KtExcaDTO {
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
        @NotEmpty(message = "엑세스토큰은 필수 입니다(max:100)")
        private String accessToken;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 정산연계 자료 조회 : BC-AG-EC-001
     * Request : KtExcaRequest
     * Response : KtExcaResponse
     * </pre>
     */
    @Schema(name = "KtExcaRequest", description = "KT 정산연계 자료 조회 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtExcaRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 정산연월 : 필수 - YYYYMM
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "정산연월", example = " ")
        @Size(min = 6, max = 6, message = "정산연월은 필수 입니다(YYYYMM)")
        private String yyyymm;
    }

    @Schema(name = "KtExcaResponse", description = "KT 정산연계 자료 조회 요청 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtExcaResponse extends KtCommonResponse {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtExcaResData> rsps;
    }

    @Schema(name = "KtExcaResData", description = "KT 정산연계 자료 조회 요청 결과 rsps DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtExcaResData {
        /**
         * <pre>
         * 정산연월 : 필수 - YYYYMM
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "정산연월")
        @Size(min = 6, max = 6)
        private String yyyymm;

        /**
         * <pre>
         * 서비스코드 : 필수 - 5
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드")
        @Size(min = 1, max = 5)
        private String serviceCd;

        /**
         * <pre>
         * 문서 코드: 필수 - 5
         * 각 기관의 문서코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서코드")
        @Size(min = 5, max = 5)
        private String msgCd;

        /**
         * <pre>
         * 통신사구분코드: 필수 - 2
         * 01:KT, 02:SKT, 03:LGT
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "통신사구분코드")
        @Size(min = 2, max = 2)
        private String mobileGbn;

        /**
         * <pre>
         * 발송요청건수: 필수 - 9
         * 정산년월의 기관, 문서별 발송요청 건수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송요청건수")
        @Digits(integer = 9, fraction = 0)
        private Integer sndnReqCnt;

        /**
         * <pre>
         * 미동의발송건수: 필수 - 9
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "미동의발송건수")
        @Digits(integer = 9, fraction = 0)
        private Integer notAppBalsongCnt;

        /**
         * <pre>
         * 동의발송건수: 필수 - 9
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "동의발송건수")
        @Digits(integer = 9, fraction = 0)
        private Integer preAppBalsongCnt;

        /**
         * <pre>
         * 미동의열람건수: 필수 - 9
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "미동의열람건수")
        @Digits(integer = 9, fraction = 0)
        private Integer notAppSusinCnt;

        /**
         * <pre>
         * 미동의열람건수: 필수 - 9
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "동의열람건수")
        @Digits(integer = 9, fraction = 0)
        private Integer preAppSusinCnt;
    }
    //-------------------------------------------------------------------
}
