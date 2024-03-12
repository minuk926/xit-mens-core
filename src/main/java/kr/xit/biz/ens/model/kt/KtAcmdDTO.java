package kr.xit.biz.ens.model.kt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description : KT 유통증명서 DTO
 *               - 유통증명서 발급(BC-AG-SM-001)
 *                 Request : {@link KtAcmdCerfRequest}
 *                 Response : {@link KtAcmdCerfResponse}
 *               - 전자문서 유통정보 수치조회(BC-AG-HS-001)
 *                 Request : {@link KtAcmdInfoRequest}
 *                 Response : {@link KtAcmdInfoResponse}
 *               - 전자문서 유통정보 수치 확인서 발급(BC-AG-HS-002)
 *                 Request : {@link KtAcmdInfoCfmRequest}
 *                 Response : {@link KtAcmdInfoCfmResponse}
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
public class KtAcmdDTO {
    /**
     * <pre>
     * 유통증명서 발급 : BC-AG-SM-001
     * Request : KtAcmdCerfRequest
     * Response : KtAcmdCerfResponse
     * </pre>
     */
    @Schema(name = "KtAcmdCerfRequest", description = "KT 유통증명서 발급 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtAcmdCerfRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 발급요청구분 - 필수 : 1자리
         * 1: 요청, 2: 발급
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발급유청구분", example = " ")
        @Size(min = 1, max = 1, message = "발급요청구분은 필수 입니다(1|2)")
        private String reqDvcd;

        /**
         * <pre>
         * 서비스코드 : 필수 - 10
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = " ")
        @Size(min = 1, max = 10, message = "서비스코드는 필수 입니다(max:10)")
        private String serviceCd;

        /**
         * <pre>
         * 서비스 코드 인증키 : 필수 - 8 자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스 코드 인증키", example = " ")
        @Size(min = 1, max = 8, message = "서비스 코드 인증키는 필수 입니다(max:8)")
        private String serviceKey;

        /**
         * <pre>
         * 관리키 : 필수 - max 50
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키", example = " ")
        @Size(min = 1, max = 50, message = "관리키는 필수 입니다(max:50)")
        private String srcKey;

        /**
         * <pre>
         * 발급요청사요 : max 200
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발급요청사유", example = " ")
        @Size(max = 200, message = "발급요청사유는 200자를 넘을수 없습니다.")
        private String issReqRsn;
    }

    @Schema(name = "KtAcmdCerfResponse", description = "KT 유통증명서 발급 요청 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = true)
    public static class KtAcmdCerfResponse extends KtCommonResponse {
        /**
         * <pre>
         * 발급상태 : 성공시 필수 - 1
         * 1: 요청
         * 2: 발급완료(통신사발급완료)
         * 3: 발급완료(기관발급완료)
         * 4: 발급실패
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발급상태")
        @Size(min = 1, max = 1)
        private String issCls;

        /**
         * <pre>
         * 발급실패 메세지 : max 255
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발급실패 메시지")
        @Size(max = 255)
        private String issResultMsg;

        /**
         * <pre>
         * 유통증명서파일명 : max 100
         * iss_cls상태값이 '2'일 때 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "유통증명서파일명", example = " ")
        @Size(max = 100)
        private String fileName;

        /**
         * <pre>
         * 유통증명서 파일의 binary 값 : 가변
         * iss_cls상태값이 '2'일 때 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "유통증명서 파일의 binary 값", example = " ")
        private String fileBinary;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 전자문서 유통정보 수치조회 : BC-AG-HS-001
     * Request : KtAcmdInfoRequest
     * Response : KtAcmdInfoResponse
     * </pre>
     */
    @Schema(name = "KtAcmdInfoRequest", description = "KT 전자문서 유통정보 수치조회 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtAcmdInfoRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 서비스코드 : 필수 - 5
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = " ")
        @Size(min = 1, max = 5, message = "서비스코드는 필수 입니다(max:5)")
        private String serviceCd;

        /**
         * <pre>
         * 연월 : 필수 - 7
         * 조회대상 월 (yyyy-mm 형식)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "연월", example = " ")
        @Size(min = 7, max = 7, message = "연월은 필수 입니다(YYYY-MM)")
        private String period;
    }

    @Schema(name = "KtAcmdInfoResponse", description = "KT 전자문서 유통정보 수치조회 요청 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtAcmdInfoResponse extends KtCommonResponse {
        @Schema(requiredMode = RequiredMode.AUTO)
        @Valid
        private List<KtAcmdInfoResData> results;
    }

    @Schema(name = "KtAcmdInfoResData", description = "KT 전자문서 유통정보 수치조회 요청 결과 results DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KtAcmdInfoResData {
        /**
         * <pre>
         * 모바일사업자구분 : 필수 - 2자리
         * 발송 통신사 구분(01:KT, 02:SKT, 03:LGT)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "통신사구분코드")
        @Size(min = 2, max = 2)
        @JsonProperty("mbl_bzowr_dvcd")
        private String mblBzowrDvcd;

        /**
         * <pre>
         * 통계집계일 : 필수 - 10자리
         * YYYY-MM-DD
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "통계집계일")
        @Size(min = 10, max = 10)
        private String date;

        /**
         * 송신건수 : 필수 - 8자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "송신건수")
        @Digits(integer = 8, fraction = 0)
        private Integer sendCount;

        /**
         * 송신건수 : 필수 - 8자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "송신건수")
        @Digits(integer = 8, fraction = 0)
        private Integer recvCount;

        /**
         * 열람건수 : 필수 - 8자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "열람건수")
        @Digits(integer = 8, fraction = 0)
        private Integer readCount;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 전자문서 유통정보 수치 확인서 발급 : BC-AG-HS-002
     * Request : KtAcmdInfoCfmRequest
     * Response : KtAcmdInfoCfmResponse
     * </pre>
     */
    @Schema(name = "KtAcmdInfoCfmRequest", description = "KT 전자문서 유통정보 수치 확인서 발급 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = true)
    public static class KtAcmdInfoCfmRequest extends KtAcmdInfoRequest {
        /**
         * <pre>
         * 발급요청구분 - 필수 : 1자리
         * 1: 요청, 2: 발급
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발급유청구분", example = " ")
        @Size(min = 1, max = 1, message = "발급요청구분은 필수 입니다(1|2)")
        private String reqDvcd;
    }

    @Schema(name = "KtAcmdInfoCfmResponse", description = "KT 전자문서 유통정보 수치 확인서 발급 요청 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtAcmdInfoCfmResponse extends KtCommonResponse {
        /**
         * <pre>
         * 발급상태 : 성공시 필수 - 1
         * 1: 요청
         * 2: 발급완료
         * 4: 발급실패
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발급상태")
        @Size(min = 1, max = 1)
        private String issCls;

        /**
         * <pre>
         * 발급결과 메세지 : max 255
         * iss_cls=4 일때, 실패 메세지
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발급결과 메시지")
        @Size(max = 255)
        private String issResultMsg;

        @Schema(requiredMode = RequiredMode.AUTO)
        @Valid
        private List<KtAcmdInfoCfmResData> results;
    }

    @Schema(name = "KtAcmdInfoResData", description = "KT 전자문서 유통정보 수치조회 요청 결과 results DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KtAcmdInfoCfmResData {
        /**
         * <pre>
         * 모바일사업자구분 : 필수 - 2자리
         * 발송 통신사 구분(01:KT, 02:SKT, 03:LGT)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "모바일사업자구분")
        @Size(min = 2, max = 2)
        @JsonProperty("mbl_bzowr_dvcd")
        private String mblBzowrDvcd;

        /**
         * <pre>
         * 유통증명서파일명 : max 100
         * iss_cls상태값이 '2'일 때 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "유통증명서파일명")
        @Size(max = 100)
        private String fileName;

        /**
         * <pre>
         * 유통증명서 파일의 binary 값 : 가변
         * iss_cls상태값이 '2'일 때 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "유통증명서 파일의 binary 값")
        private String fileBinary;
    }
    //-------------------------------------------------------------------
}
