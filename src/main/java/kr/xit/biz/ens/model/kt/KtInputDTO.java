package kr.xit.biz.ens.model.kt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.kt.KtCommonDTO.KtCommonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * description : KT MMS DTO - KT에 제공해야 하는 API DTO
 *               - 수신거부상태 전송(BC-AG-SN-014)
 *                 Request : {@link KtRefuseRcvRequest}
 *                 Response : {@link KtCommonResponse}
 *               - 수신동의상태 전송(BC-AG-SN-015)
 *                 Request : {@link KtApproveRcvRequest}
 *                 Response : {@link KtCommonResponse}
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : KtInputDTO
 * author      : limju
 * date        : 2023-09-26
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-26    limju       최초 생성
 *
 * </pre>
 */
public class KtInputDTO {
    /**
     * <pre>
     * 수신거부상태 전송 : BC-AG-SN-014
     * Request : KtRefuseRcvRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtRefuseRcvRequest", description = "KT 수신거부상태 등록 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtRefuseRcvRequest {
        /**
         * <pre>
         * 신청일자 : 8(YYYYMMDD)
         * 수신거부 등록/해제 발생일자
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "신청일자", example = " ")
        @Size(min = 8, max = 8, message = "신청일자는 8자 입니다.")
        private String apctDt;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtRefuseRcvReqData> reqs;
    }

    @Schema(name = "KtRefuseRcvReqData", description = "KT 수신거부상태 등록 요청 reqs DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtRefuseRcvReqData {
        /**
         * <pre>
         * 서비스코드 : 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드, 전기관(ALL)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = " ")
        @Size(min = 1, max = 20, message = "서비스코드는 필수 입니다(max:20)")
        private String serviceCd;

        /**
         * <pre>
         * 개인식별코드(CI) - 필수 : 88자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "개인식별코드", example = " ")
        @Size(min = 88, max = 88, message = "개인식별코드(CI)는 필수 입니다(88자리)")
        private String ci;

        /**
         * <pre>
         * 신청구분 - 필수 : 1자리
         * 0: 해지, 1: 신청
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "신청구분", example = " ")
        @Size(min = 1, max = 1, message = "신청구분은 필수 입니다(0|1)")
        private String apctAcctCls;

        /**
         * <pre>
         * 신청일시 : 14
         * 수신거부 등록/해제 발생일자 (YYYYMMDDhhmmss)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "신청일시", example = " ")
        @Size(min = 14, max = 14, message = "신청일시는 14자 입니다.")
        private String apctTm;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 수신동의상태 전송 : BC-AG-SN-015
     * Request : KtApproveRcvRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtApproveRcvRequest", description = "KT 수신동의상태 등록 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtApproveRcvRequest {
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtApproveRcvReqData> reqs;
    }

    @Schema(name = "KtApproveRcvReqData", description = "KT 수신동의상태 등록 요청 reqs DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtApproveRcvReqData {
        /**
         * <pre>
         * 신청일자 : 14(YYYYMMDDHHMISS)
         * 수신동의 등록/해제 발생일시
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "신청일시", example = " ")
        @Size(min = 8, max = 8, message = "신청일시는 14자 입니다.")
        private String apctDt;

        /**
         * <pre>
         * 서비스코드 : 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = " ")
        @Size(min = 1, max = 20, message = "서비스코드는 필수 입니다(max:20)")
        private String serviceCd;

        /**
         * <pre>
         * 개인식별코드(CI) - 필수 : 88자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "개인식별코드", example = " ")
        @Size(min = 88, max = 88, message = "개인식별코드(CI)는 필수 입니다(88자리)")
        private String ci;

        /**
         * <pre>
         * 신청구분 - 필수 : 1자리
         * 0:해지(수신동의 신청상태 해지 처리)
         * 1:신청(수신동의 신청)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "신청구분", example = " ")
        @Size(min = 1, max = 1, message = "신청구분은 필수 입니다(0|1)")
        private String apctAcctCls;
    }
    //-------------------------------------------------------------------

}
