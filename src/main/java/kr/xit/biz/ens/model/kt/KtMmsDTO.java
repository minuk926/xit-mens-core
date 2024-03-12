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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : KT 공인 알림 문자 DTO
 *               - 수신거부등록(BC-AG-SN-007)
 *                 Request : {@link KtBlacklistRequest}
 *                 Response : {@link KtCommonResponse}
 *               - 백오피스 발송통계 연계 조회(BC-AG-SN-011)
 *                 Request : {@link KtSendSttcRequest}
 *                 Response : {@link KtSendSttcResponse}
 *               - 백오피스 발송결과 연계 조회(BC-AG-SN-012)
 *                 Request : {@link KtSendSttcDtlRequest}
 *                 Response : {@link KtSendSttcDtlResponse}
 *               - Whitelist 등록(BC-AG-SN-013)
 *                 Request : {@link KtWhitelistRequest}
 *                 Response : {@link KtCommonResponse}
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : KtMmsDTO
 * author      : limju
 * date        : 2023-09-26
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-26    limju       최초 생성
 *
 * </pre>
 */
public class KtMmsDTO {
    /**
     * <pre>
     * 수신거부등록 : BC-AG-SN-007
     * Request : Blacklist
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtBlacklistRequest", description = "KT 수신 거부 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtBlacklistRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 서비스코드 : 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>
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

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtBlacklistReqData> reqs;
    }

    @Schema(name = "KtBlacklistReqData", description = "KT 수신거부 등록 요청 reqs DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtBlacklistReqData {
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
         * 신청일시 - 필수 : 14자리(YYYYMMDDHHMISS)
         * 수신거부 등록/해지 발생일자
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "신청일시", example = " ")
        @Size(min = 14, max = 14, message = "신청일시는 필수 입니다(YYYYMMDDHHMISS)")
        private String apctTm;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 백오피스 발송통계 연계 조회 : BC-AG-SN-011
     * Request : KtSendSttcRequest
     * Response : KtSendSttcResponse
     * </pre>
     */
    @Schema(name = "KtSendSttcRequest", description = "KT 백오피스 발송통계 연계 조회 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtSendSttcRequest extends KtCommonDTO.KtMnsRequest {

        /**
         * <pre>
         * 발송일자 : 필수(YYYYMMDD)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송일자", example = " ")
        @Size(min = 8, max = 8, message = "발송일자는 필수입니다(YYYYMMDD)")
        private String balsongDt;
    }

    @Schema(name = "KtSendSttcResponse", description = "KT 백오피스 발송통계 연계 조회 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtSendSttcResponse extends KtCommonResponse {

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtSendSttcResData> rsps;
    }

    @Schema(name = "KtSendSttcResData", description = "KT 백오피스 발송통계 연계 조회 결과 rsps DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtSendSttcResData {
        /**
         * <pre>
         * 관리키 : 필수 - max 22
         * 통계생성기준 키값(그룹을 구분하기 위함)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키")
        @Size(min = 1, max = 22)
        private String srcKey;

        /**
         * <pre>
         * 발송일자 : 필수(YYYYMMDD)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송일자")
        @Size(min = 8, max = 8)
        private String balsongDt;

        /**
         * <pre>
         * 서비스코드 : 필수 - 5
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드")
        @Size(min = 5, max = 5)
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
         * 요청건수 : 필수 - max 9
         * 발송일자 + 서비스코드 + 문서코드 별 합
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "요청건수")
        @Digits(integer = 9, fraction = 0)
        private Integer balsongTotCnt;

        /**
         * <pre>
         * 매핑건수 : 필수 - max 9
         * 3개 통신사 사용자인 경우의 건수
         * 각 통신사 별 발송 성공 건수
         * (통신사 사용자로 발송 실패인 경우도 집계가 됨 Ex. 다회선 사용자 등)
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "매핑건수")
        @Digits(integer = 9, fraction = 0)
        private Integer mapping_cnt;

        /**
         * <pre>
         * 동의건수 : 필수 - max 9
         * 기동의 사용자의 건수
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "동의건수")
        @Digits(integer = 9, fraction = 0)
        private Integer approveCnt;

        /**
         * <pre>
         * 발송건수 : 필수 - max 9
         * 각 통신사 별 정상 수신 건수
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송건수")
        @Digits(integer = 9, fraction = 0)
        private Integer balsongCnt;

        /**
         * <pre>
         * 열람건수 : 필수 - max 9
         * 열람한 건수
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "열람건수")
        @Digits(integer = 9, fraction = 0)
        private Integer susinCnt;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 백오피스 발송결과 연계 조회 : BC-AG-SN-012
     * Request : KtSendSttcDtlRequest
     * Response : KtSendSttcDtlResponse
     * </pre>
     */
    @Schema(name = "KtSendSttcDtlRequest", description = "KT 백오피스 발송 결과 연계 조회 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtSendSttcDtlRequest extends KtCommonDTO.KtMnsRequest {

        /**
         * <pre>
         * 관리키 : 필수 - max 22
         * 통계생성기준 키값(그룹을 구분하기 위함)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키", example = "")
        @Size(min = 1, max = 22, message = "관리키는 필수입니다(max:22)")
        private String srcKey;

        /**
         * <pre>
         * 통신사구분코드: 필수 - 2
         * 01:KT, 02:SKT, 03:LGT
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "통신사구분코드")
        @Size(min = 2, max = 2)
        private String mobileGbn;
    }

    @Schema(name = "KtSendSttcDtlResponse", description = "KT 백오피스 발송결과 연계 조회 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtSendSttcDtlResponse extends KtCommonResponse {

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtSendSttcDtlResData> rsps;
    }

    @Schema(name = "KtSendSttcDtlResData", description = "KT 백오피스 발송결과 연계 조회 결과 rsps DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtSendSttcDtlResData {
        /**
         * <pre>
         * 결과코드 : 필수 - 4
         * 발송결과 코드 값
         * 40	MMS/RCS 발송	수신성공
         * 41	MMS발송	메시지 내용 스팸
         * 42	MMS발송	착신자 스팸
         * 43	MMS발송	레포트 수신 시간 만료(메시지전송후 24시간 레포트 못받는 경우 )
         * 47	MMS발송	비가입자, 결번, 서비스정지
         * 48	MMS발송	단말기 전원 꺼짐
         * 49	MMS발송	기타
         * 4A	MMS발송	UNKNOWN/단말기형식오류(스팸설정)
         * 4D	MMS발송	전송 실패(무선망), 단말기 일시정지
         * 4J	MMS발송	비가용폰 오류
         * 4N	MMS발송	수신거부
         * 4O	MMS발송	LMS발송오류(시간초과)
         * 4P	MMS발송	LMS발송오류(기타)
         * 4T	MMS발송	가입자미존재(미동의포함)
         * 4Q	MMS발송	수신대기
         * 4R	MMS발송	다회선 제외
         * 4V	MMS발송	수신등록 휴대폰번호 미존재
         * 4W	MMS발송	기동의 사전문자 제외
         * 50	수신확인	수신성공
         * 60	열람확인	열람확인 (MMS발송결과순번=2, 열람타임스탬프가 있는 경우)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "결과코드")
        @Size(min = 1, max = 4)
        private String balsongRstCd;

        /**
         * <pre>
         * 결과메세지 : max 200
         * 발송결과 코드의 메세지
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "결과메세지")
        @Size(min = 1, max = 200)
        private String balsongRstMsg;

        /**
         * <pre>
         * 건수 : 필수 - max 9
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "건수")
        @Digits(integer = 9, fraction = 0)
        private Integer balsongCnt;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * Whitelist 등록 : BC-AG-SN-013
     * Request : KtWhitelistRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtWhitelistRequest", description = "KT Whitelist 등록 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtWhitelistRequest extends KtCommonDTO.KtMnsRequest {
        /**
         * <pre>
         * 서비스코드 : 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>
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

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtWhitelistReqData> reqs;
    }

    @Schema(name = "KtWhitelistReqData", description = "KT Whitelist 등록 요청 reqs DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtWhitelistReqData {
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
         * 모바일사업자구분 : 필수 - 2자리
         * 발송 통신사 구분(01:KT, 02:SKT, 03:LGT)
         * 통신사 구분, 개인휴대전화번호 중 하나는 필수입력
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "통신사구분코드", example = " ")
        @Size(min = 2, max = 2, message = "통신사구분코드는 필수 입니다(01|02|03)")
        private String mblBzowrDvcd;

        /**
         * <pre>
         * 개인휴대전화번호 : 11 자리
         * 통신사 구분, 개인휴대전화번호 중 하나는 필수입력
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "개인휴대전화번호", example = "01011112222")
        @Size(min = 11, max = 11, message = "개인 휴대 전화번호는 11자리 입니다")
        private String mdn;
    }
    //-------------------------------------------------------------------
}
