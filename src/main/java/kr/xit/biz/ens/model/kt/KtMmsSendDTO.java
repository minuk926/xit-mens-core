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
import kr.xit.biz.ens.model.cmm.CmmEnsRequestDTO;
import kr.xit.biz.ens.model.kt.KtCommonDTO.KtCommonResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : KT 공인 알림문자 사전알림문자, 본문자수신 등록 DTO
 *               - 사전 알림문자 수신 등록(BC-AG-SN-001)
 *                 Request : {@link KtBefSendRequest}
 *                 Response : {@link KtCommonResponse}
 *               - 본문자 수신 등록(BC-AG-SN-002)
 *                 Request : {@link KtMainSendRequest}
 *                 Response : {@link KtCommonResponse}
 *               - 사전/본 문자 발송/수신 결과 전송(BC-AG-SN-010)
 *                 - Request : {@link KtMsgRsltRequest}
 *                 - Response : {@link KtCommonResponse}
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : KtMmsSendDTO
 * author      : limju
 * date        : 2023-09-22
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-22    limju       최초 생성
 *
 * </pre>
 */
public class KtMmsSendDTO {
    /**
     * <pre>
     * 사전문자 수신 등록 : BC-AG-SN-001
     * Request : KtBefSendRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtBefSendRequest", description = "사전문자수신(BC-AG-SN-001) 등록 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtBefSendRequest extends KtCommonDTO.KtMnsRequest {
        //-------------------------------------------------------------------
        // 필수
        //-------------------------------------------------------------------
        /**
         * <pre>
         * 서비스 코드: 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스 코드", example = "NPS")
        @NotEmpty(message = "서비스 코드는 필수 입니다(max:20)")
        @Size(max = 20)
        private String serviceCd;

        /**
         * <pre>
         * 서비스 코드: 필수 - 16
         * BizCenter에서 발행한 기관의 서비스 코드 인증키
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = "A3AD41349D759DD3")
        @Size(min = 16, max = 16, message = "서비스 코드 인증키는 필수 입니다")
        private String serviceKey;

        /**
         * <pre>
         * 문서 코드: 필수 - 5
         * 문서코드에 따라 발신번호 Mapping
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서코드", example = "DP112")
        @Size(min = 5, max = 5, message = "문서 코드는 필수 입니다(max:5)")
        private String msgCd;

        /**
         * <pre>
         * 문발송 메시지 타입: 필수 - 1
         * RCS/xMS (RCS :1, xMS: 2)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송 메시지 타입", example = "2")
        @Size(min = 1, max = 1, message = "발송 메시지 타입은 필수 입니다")
        private String msgType;

        /**
         * <pre>
         * 발송시작일시: 필수 - 14
         * 메시지 발송 처리 시작일시(YYYYMMDDHHMiSS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송시작일시", example = "20211229102000")
        @Size(min = 14, max = 14, message = "발송시작일시는 필수 입니다")
        private String makeDt;

        /**
         * <pre>
         * 발송마감시간: 필수 - 14
         * 메시지 발송에 대한 마감시간(YYYYMMDDHHMiSS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송마감시간", example = "20211229180000")
        @Size(min = 14, max = 14, message = "발송마감시간은 필수 입니다")
        private String sndnExTime;

        /**
         * <pre>
         * 문서종류: 필수 - 3
         * 메시지타입(과금코드)
         * 1: LMS
         * 2: MMS
         * 3: 하이브리드 LMS
         * 4: 하이브리드 MMS
         * 5: RCS
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서종류", example = "2")
        @NotEmpty(message = "문서종류는 필수 입니다(max:3)")
        @Size(max = 3)
        @JsonProperty("m_type")
        private String mType;

        /**
         * <pre>
         * 발송요청건수: 필수 - 8
         * 발송메세지의 총 건수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송요청건수", example = "1")
        @Digits(integer = 8, fraction = 0, message = "발송요청건수(max:8)")
        private Integer sndnTotCnt;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtBefSendReqData> reqs;
        //-------------------------------------------------------------------

        /**
         * <pre>
         * 대행사 코드: max 10
         * msg_type 1(RCS 메세지인 경우)인 경우 필수
         * LG U+에서 사용되며 기관에서 등록한 대행사 코드의 값 default 'lguplus'
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "대행사코드", example = " ")
        @Size(max = 10, message = "대행사 코드는 10자를 넘을 수 없습니다.")
        private String agencyId;

        /**
         * <pre>
         * 부서 코드: max 5
         * 기관에 따른 부서 코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "부서코드", example = " ")
        @Size(max = 5, message = "부서 코드는 5자를 넘을 수 없습니다.")
        private String deptCd;

        /**
         * <pre>
         * 발송 번호: max 20
         * 서비스기관 발송전화번호
         * 문서코드 등록시 발신번호 Mapping
         * Biz.center 등록시 통신사실증명원 등록(민간)
         *
         * API에 있는 경우 API 발송 번호를 우선으로 하고, 없는 경우 문서코드 등록시 입력된 발신번호를 사용한다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송번호", example = " ")
        @Size(max = 20, message = "서비스기관 발송전화번호는 20자를 넘을 수 없습니다.")
        private String sndTelNo;

        /**
         * <pre>
         * 메세지발송구분: max 1
         * 2: OPT_OUT 사전문자
         * 6. OPT_OUT 사전문자(마케팅수신동의고객만 발송)
         *
         * 값이 null인 경우 default 2(OPT_OUT 사전문자)로 P/F 전송 됨
         * 마케팅수신동의값이 'Y'인 경우 opt-type은 '6'으로 보내야 함.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "메세지발송구분", example = "2")
        @Size(min = 1, max = 1, message = "메세지발송구분은 1자리 입니다.")
        private String optType;

        /**
         * <pre>
         * RCS 브랜드홈 값 : mzx 30
         * msg_type 1(RCS 메시지)인 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "RCS 브랜드홈 값", example = " ")
        @Size(max = 30, message = "RCS 브랜드홈 값은 30자를 넘을 수 없습니다.")
        private String brandId;

        /**
         * <pre>
         * 마케팅 수신 동의 : 1
         * Y|N
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "마케팅수신동의", example = "N")
        @Size(min = 1, max = 1, message = "마게팅 수신 동의는 1(Y|N)자 입니다.")
        private String mktnRcveCsyn;

        /**
         * <pre>
         * 사전문자 본인인증 URL 포함 여부 : 1
         * 타 중계기관(포스토피아)에서 발송 요청인 경우 필수
         * Y : 본인인증 URL 포함,
         * N : 미포함 (NULL인 경우 미포함)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "사전문자 본인인증 URL 포함 여부", example = "N")
        @Size(min = 1, max = 1, message = "사전문자 본인인증 URL 포함 여부는 1(Y|N)자 입니다.")
        private String certificationYn;

        /**
         * <pre>
         * 다회선 사용자 처리구분 : 1
         * 1 : 다회선 모두 발송
         * 2 : 다회선 발송 제외(default) (NULL 일경우 동일취급)
         * 3 : 다회선 중 임의 1회선 발송
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "다회선 사용자 처리구분", example = "2")
        @Size(min = 1, max = 1, message = "다회선 사용자 처리구분은 1(1|2|3)자 입니다.")
        private String multiMblPrcType;

        /**
         * <pre>
         * 테스트 발송여부 : 1
         * Y : 테스트 발송 (KISA 연동, 통계, 이력 제외)
         * N : 실발송(NULL일경우 동일 취급)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "테스트 발송 여부", example = "N")
        @Size(min = 1, max = 1, message = "테스트 발송 여부는 1(Y|N)자 입니다.")
        private String testSndnYn;

        /**
         * <pre>
         * 수신거부해제 여부 : 1
         * Y : 해제 (수신거부 시 수신거부 해제)
         * N : 미해제(NULL일경우 동일취급)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신거부해제 여부", example = "Y")
        @Size(min = 1, max = 1, message = "수신거부해제 여부는 1(Y|N)자 입니다.")
        private String rcvRfrlYn;
    }


    @Schema(name = "KtBefSendReqData", description = "사전문자수신 등록 요청 reqs DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtBefSendReqData {
        //-------------------------------------------------------------------
        // 필수
        //-------------------------------------------------------------------
        /**
         * 관리키 : 고객메시지 건별 Unique key
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키", example = "S20211229102000001")
        @NotEmpty(message = "관리키는 필수 입니다(max:50)")
        @Size(max = 50)
        private String srcKey;

        /**
         * 리스트순번 : max 8자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "리스트순번", example = "1")
        @NotEmpty(message = "리스트 순번은 필수 입니다(max:8)")
        @Size(max = 8)
        private String srcSeq;

        /**
         * 개인식별코드 : 88 자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "개인식별코드", example = "gdlIa53FZGQz5aKa3wLk33nW57N3mDpcwHytWlWMhzxHKulk7EZs143442394326642342364238648423864237")
        @NotEmpty(message = "개인식별코드는 필수 입니다")
        @Size(min = 88, max = 88, message = "개인식별코드는 필수 입니다(88자리)")
        private String ci;
        //-------------------------------------------------------------------

        /**
         * <pre>
         * 개인휴대전화번호 : 11 자리
         * 특정 MDN으로 발송할 경우
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "개인휴대전화번호", example = "01011112222")
        @Size(min = 11, max = 11, message = "개인 휴대 전화번호는 11자리 입니다")
        private String mdn;

        /**
         * <pre>
         * 사전문자 본인인증 URL : max 1000
         * 타 중계기관(포스토피아)에서 발송 요청인 경우 필수
         * 사전문자 등록 시 {#INFO_CFRM_STR} 항목이 있는 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "사전문자 본인인증 URL", example = " ")
        @Size(max = 1000, message = "사전문자 본인인증 URL은 1000자를 넘을 수 없습니다.")
        private String url;

        /**
         * <pre>
         * 수신거부 및 수신 휴대폰 지정하기 치환문구 : max 50
         * 수신거부 및 수신 휴대폰 지정하기 예약어로써 API에 추가 될 {#RCVE_RF_STR} 항목에 있는 문구를 치환하여 사용 한다.
         * 항목이 NULL인 경우 default로 아래 문구를 사용 한다.
         * "○ 수신거부 및 수신 휴대폰 지정하기 : "
         * RCS 버튼명 사용이 가능(한글은 최대 17자 권고)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신거부 및 수신 휴대폰 지정하기 치환문구", example = " ○ 수신거부 및 수신 휴대폰 지정하기 :")
        @Size(max = 50, message = "수신거부 및 수신 휴대폰 지정하기 치환문구는 50자를 넘을 수 없습니다.")
        private String rcveRfStr;

        /**
         * <pre>
         * 안내문 확인하기 치환문구 : max 50
         * 포스토피아 발송건(url 의 값이 있는 경우)에 대해서
         * 안내문 확인하기 예약어로써 API에 추가 될 {#INFO_CFRM_STR} 항목에 있는 문구를 치환 하여 사용 한다.
         * 항목이 NULL인 경우 default로 아래 문구를 사용 한다.
         * "○ 안내문 확인하기 : "
         * RCS 버튼명 사용이 가능(한글은 최대 17자 권고)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "안내문 확인하기 치환문구", example = " ")
        @Size(max = 50, message = "안내문 확인하기 치환문구는 50자를 넘을 수 없습니다.")
        private String infoCfrmStr;
    }
    //----------------------------------------------------------------------------------------

    /**
     * <pre>
     * 본문자 수신 등록 : BC-AG-SN-002
     * Request : KtMainSendRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtMainSendRequest", description = "본문자수신(BC-AG-SN-002) 등록 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtMainSendRequest extends KtCommonDTO.KtMnsRequest {
        //-------------------------------------------------------------------
        // 필수
        //-------------------------------------------------------------------
        /**
         * <pre>
         * 서비스 코드: 필수 - 20
         * BizCenter에서 발행한 기관의 서비스 코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스 코드", example = "SISUL")
        @NotEmpty(message = "서비스 코드는 필수 입니다(max:20)")
        @Size(max = 20)
        private String serviceCd;

        /**
         * <pre>
         * 서비스 코드: 필수 - 16
         * BizCenter에서 발행한 기관의 서비스 코드 인증키
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스코드", example = "8TASJCZEPDUIFSJI")
        @Size(min = 16, max = 16, message = "서비스 코드 인증키는 필수 입니다")
        private String serviceKey;

        /**
         * <pre>
         * 문서 코드: 필수 - 5
         * 문서코드에 따라 발신번호 Mapping
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서코드", example = "DP112")
        @Size(min = 1, max = 5, message = "문서 코드는 필수 입니다(max:5)")
        private String msgCd;

        /**
         * <pre>
         * 문발송 메시지 타입: 필수 - 1
         * RCS/xMS (RCS :1, xMS: 2)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송 메시지 타입", example = "2")
        @Size(min = 1, max = 1, message = "발송 메시지 타입은 필수 입니다(1|2)")
        private final String msgType = "2";

        /**
         * <pre>
         * 발송시작일시: 필수 - 14
         * 메시지 발송 처리 시작일시(YYYYMMDDHHMiSS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송시작일시", example = "20231019090000")
        @Size(min = 14, max = 14, message = "발송시작일시는 필수 입니다(YYYYMMDDHHMiSS)")
        private String makeDt;

        /**
         * <pre>
         * 발송마감시간: 필수 - 14
         * 메시지 발송에 대한 마감시간(YYYYMMDDHHMiSS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송마감시간", example = "20231130090000")
        @Size(min = 14, max = 14, message = "발송마감시간은 필수 입니다(YYYYMMDDHHMiSS)")
        private String sndnExTime;

        /**
         * <pre>
         * 열람마감시간: 필수 - 14
         * 문서열람에 대한 마감시간(YYYYMMDDHHMiSS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "열람마감시간", example = "20231130090000")
        @Size(min = 14, max = 14, message = "열람마감시간은 필수 입니다(YYYYMMDDHHMiSS)")
        private String exTime;

        /**
         * <pre>
         * 문서종류: 필수 - 3
         * 메시지타입(과금코드)
         * 3: LMS
         * 4: MMS
         * 6: RCS
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서종류", example = "2")
        @NotEmpty(message = "문서종류는 필수 입니다(max:3)")
        private final String m_type = "4";

        /**
         * <pre>
         * 발송요청건수: 필수 - 8
         * 발송메세지의 총 건수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송요청건수", example = "1")
        @Digits(integer = 8, fraction = 0, message = "발송요청건수(max:8)")
        private Integer sndnTotCnt;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtMainSendReqData> reqs;
        //-------------------------------------------------------------------

        /**
         * <pre>
         * 대행사 코드: max 10
         * msg_type 1(RCS 메세지인 경우)인 경우 필수
         * LG U+에서 사용되며 기관에서 등록한 대행사 코드의 값 default 'lguplus'
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "대행사코드", example = " ")
        @Size(max = 10, message = "대행사 코드는 10자를 넘을 수 없습니다.")
        private String agencyId;

        /**
         * <pre>
         * 부서 코드: max 5
         * 기관에 따른 부서 코드
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "부서코드", example = " ")
        @Size(max = 5, message = "부서 코드는 5자를 넘을 수 없습니다.")
        private String deptCd;

        /**
         * <pre>
         * 발송 번호: max 20
         * 문서코드 등록시 발신번호 Mapping
         * Biz.center 등록시 통신사실증명원 등록(민간)
         *
         * API에 있는 경우 API 발송 번호를 우선으로 하고, 없는 경우 문서코드 등록시 입력된 발신번호를 사용한다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송번호", example = " ")
        @Size(max = 20, message = "서비스기관 발송번호는 20자를 넘을 수 없습니다.")
        private String sndTelNo;

        /**
         * <pre>
         * 메세지발송구분: max 1
         * 3: 하이브리드
         *
         * 값이 null인 경우 default 1(OPT_OUT 본문)로 P/F 전송 됨
         * 마케팅수신동의 고객인 경우 5 또는 7 코드를 전송해야 함.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "메세지발송구분", example = "1")
        @Size(min = 1, max = 1, message = "메세지발송구분은 1자리 입니다.")
        private final String optType = "1";

        /**
         * <pre>
         * RCS 브랜드홈 값 : max 30
         * msg_type 1(RCS 메시지)인 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "RCS 브랜드홈 값", example = " ")
        @Size(max = 30, message = "RCS 브랜드홈 값은 30자를 넘을 수 없습니다.")
        private String brandId;

        /**
         * <pre>
         * 토큰확인대체여부 : Y|N
         * Y 인경우 본인인증시 열람확인처리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "토큰확인대체여부", example = " ")
        @Size(max = 1, message = "토큰확인대체여부는 1자리 입니다(Y|N)")
        private String tknRpmtYn;

        /**
         * <pre>
         * 열람확인대체여부 : Y|N
         * Y 인경우 토큰인증시 열람확인처리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "열람확인대체여부", example = " ")
        @Size(max = 1, message = "열람확인대체여부는 1자리 입니다(Y|N)")
        private String rdngRpmtYn;

        /**
         * <pre>
         * MMS 바이너리 : 가변 - MMS 필수
         * 이미지 등 Binary (용량 : 300KB 이내)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "MMS 바이너리", example = " ")
        private String mmsBinary;

        /**
         * <pre>
         * MMS 바이너리 파일포맷(확장자) : 10
         * MMS 바이너리 값이 있는 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "MMS 바이너리 파일포맷(확장자)", example = " ")
        @Size(max = 10, message = "MMS 바이너리 파일포맷(확장자)은 10자를 넘을 수 없습니다.")
        private String fileFmat;

        /**
         * <pre>
         * 마케팅 수신 동의 : 1
         * Y : 동의, N : 비동의 (NULL인 경우 비동의 처리)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "마케팅수신동의", example = "N")
        @Size(max = 1, message = "마게팅 수신 동의는 1(Y|N)자 입니다.")
        private String mktnRcveCsyn;

        /**
         * <pre>
         * 송신자 플랫폼 ID : max 25
         * 타 중계기관(포스토피아)에서 발송 요청인 경우 필수
         * 송신중계자(포스토피아) 플랫폼 ID
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "송신자 플랫폼 ID", example = " ")
        @Size(max = 25, message = "송신자 플랫폼 ID는 25자를 넘을 수 없습니다.")
        private String sndPlfmId;

        /**
         * <pre>
         * 송신 공인전자주소 : max 100
         * 타 중계기관(포스토피아)에서 발송 요청인 경우 필수
         * 중계자 전자유통정보 등록을 위한 송신 공인전자주소
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "송신 공인전자주소", example = " ")
        @Size(max = 100, message = "송신 공인전자주소는 100자를 넘을 수 없습니다.")
        private String sndNpost;

        /**
         * <pre>
         * 송신일시 : 14(YYYYMMDDHHMISS)
         * 타 중계기관(포스토피아)에서 발송 요청인 경우 필수
         * 중계자간의 발송시 유통정보의 송신일시는 송신중계자의 송신일시로 적용
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "송신일시", example = " ")
        @Size(max = 14, message = "송신일시는 14자 입니다.")
        private String sndDate;

        /**
         * <pre>
         * 다회선 사용자 처리구분 : 1
         * 1 : 다회선 모두 발송
         * 2 : 다회선 발송 제외(default) (NULL 일경우 동일취급)
         * 3 : 다회선 중 임의 1회선 발송
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "다회선 사용자 처리구분", example = "2")
        @Size(max = 1, message = "다회선 사용자 처리구분은 1(1|2|3)자 입니다.")
        private String multiMblPrcType;

        /**
         * <pre>
         * 테스트 발송여부 : 1
         * Y : 테스트 발송 (KISA 연동, 통계, 이력 제외)
         * N : 실발송(NULL일경우 동일 취급)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "테스트 발송 여부", example = "N")
        @Size(max = 1, message = "테스트 발송 여부는 1(Y|N)자 입니다.")
        private String testSndnYn;

        /**
         * <pre>
         * 수신거부해제 여부 : 1
         * Y : 해제 (수신거부 시 수신거부 해제)
         * N : 미해제(NULL일경우 동일취급)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신거부해제 여부", example = "Y")
        @Size(max = 1, message = "수신거부해제 여부는 1(Y|N)자 입니다.")
        private String rcvRfrlYn;


        /**
         * <pre>
         * 재열람 일수 : max 3
         * 값이 있을 경우 발송시작일시+재열람 일수 동안 재열람
         * 값이 없을 경우 열람마감이후 열람 불가
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "재열람 일수", example = " ")
        @Size(max = 3, message = "재열람 일수는 3자를 넘을 수 없습니다.")
        private String reopenDay;
    }

    @Schema(name = "KtMainSendReqData", description = "본문자수신 등록 요청 reqs DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtMainSendReqData {
        //-------------------------------------------------------------------
        // 필수
        //-------------------------------------------------------------------
        /**
         * 관리키 : 고객메시지 건별 Unique key
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키", example = "S20211229102000011")
        @NotEmpty(message = "관리키는 필수 입니다(max:50)")
        @Size(max = 50)
        private String srcKey;

        /**
         * 리스트순번 : max 8자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "리스트순번", example = "1")
        @NotEmpty(message = "리스트 순번은 필수 입니다(max:8)")
        @Size(max = 8)
        private String srcSeq;

        /**
         * 개인식별코드 : 88 자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "개인식별코드", example = "vMtqVxJX56lBgbf9heK3QTc+jVndTfK77i/UJKAzPmBG4n9CazCdd/8YytlFZnN4qofIqgxHpSoiG0yYzgEpJg==")
        @Size(min = 88, max = 88, message = "개인식별코드는 필수 입니다(88자리)")
        private String ci;

        /**
         * <pre>
         * MMS 상세내용 : max 4000
         * URL 없음
         * {#INFO_CFRM_STR}, {#RCVE_RF_STR} 문자열이 없는 경우 요청 거부 처리 함.
         * 단, 유통정보미생성여부가 'Y'인 경우 {#RCVE_RF_STR} 문자열만 체크.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "MMS 상세내용", example = "{#INFO_CFRM_STR}, {#RCVE_RF_STR}")
        @Size(min = 1, max = 4000, message = "MMS 상세내용은 4000자를 넘을 수 없습니다.")
        private String mmsDtlCnts;

        /**
         * 문서해시 : max 100
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서해시", example = "gdlIa53FZGQz5aKa3wLk33nW57N3mDpcwHytWlWMhzxHKulk7EZs143442394326642342364238648423864237")
        @Size(min = 1, max = 100, message = "문서해시 100자를 넘을 수 없습니다.")
        private String docHash;
        //-------------------------------------------------------------------

        /**
         * <pre>
         * MMS 제목 : max 40
         * utf-8 한글20자
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "MMS 제목", example = "공유재산사용료 정기분")
        @Size(max = 40, message = "MMS 제목은 40자를 넘을 수 없습니다")
        private String mmsTitle;

        /**
         * <pre>
         * RCS 상세내용 : max 4000 - RCS|Binary 전송시 필수
         * RCS 메시지 fallback 시 mms_dtl_cnts를 사용하며
         * RCS title은 mms_title 항목을 함께 사용한다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "RCS 상세내용", example = " ")
        @Size(max = 4000, message = "RCS 상세내용은 4000자를 넘을 수 없습니다.")
        private String rcsDtlCnts;

        /**
         * <pre>
         * 연결 URL : max 1000
         * dist_info_crt_yn의 값이 'Y'가 아닌 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "연결 URL", example = "http://localhost:8081/api/biz/kt/v1/cfmToken")
        @Size(max = 1000, message = "연결 URL은 1000자를 넘을 수 없습니다.")
        @Setter
        private String url;

        /**
         * <pre>
         * MMS 바이너리 : 가변 - MMS 필수
         * 이미지 등 Binary (용량 : 300KB 이내)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "MMS 바이너리", example = " ")
        private String mmsBinary;

        /**
         * <pre>
         * MMS 바이너리 파일포맷(확장자) : 10
         * MMS 바이너리 값이 있는 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "MMS 바이너리 파일포맷(확장자)", example = " ")
        @Size(max = 10, message = "MMS 바이너리 파일포맷(확장자)은 10자를 넘을 수 없습니다.")
        private String fileFmat;

        /**
         * <pre>
         * 개인휴대전화번호 : 11 자리
         * 특정 MDN으로 발송할 경우
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "개인휴대전화번호", example = "01093414345")
        @Size(max = 11, message = "개인 휴대 전화번호는 11자리 입니다")
        private String mdn;

        /**
         * <pre>
         * 유통정보 미생성여부 : Y|N
         * Y : 본문자 발송 중 안내문확인하기 문구 및 URL이 표기 되지 않아야 함.(KISA 유통정보 생성 하지 않음)
         * N Or NULL인 경우 안내문확인하기 문구 및 URL이 표기
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "유통정보 미생성 여부", example = "N")
        @Size(max = 1, message = "유통정보 미생성 여부는 1(Y|N)자 입니다.")
        private String distInfoCrtYn;

        /**
         * <pre>
         * 안내문 확인하기 치환문구 : max 50
         * 안내문 확인하기 예약어로써 API에 추가 될 {#INFO_CFRM_STR} 항목에 있는 문구를 치환 하여 사용 한다.
         * 항목이 NULL인 경우 default로 아래 문구를 사용 한다.
         * ‘○ 안내문 확인하기 : ‘
         * RCS 버튼명 사용이 가능(한글은 최대 17자 권고)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "안내문 확인하기 치환문구", example = "○ 안내문 확인하기 : ")
        @Size(max = 50, message = "안내문 확인하기 치환문구는 50자를 넘을 수 없습니다.")
        private String infoCfrmStr;

        /**
         * <pre>
         * 수신거부 및 수신 휴대폰 지정하기 치환문구 : max 50
         * 수신거부 및 수신 휴대폰 지정하기 예약어로써 API에 추가 될 {#RCVE_RF_STR} 항목에 있는 문구를 치환하여 사용 한다.
         * 항목이 NULL인 경우 default로 아래 문구를 사용 한다.
         * "○ 수신거부 및 수신 휴대폰 지정하기 : "
         * RCS 버튼명 사용이 가능(한글은 최대 17자 권고)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신거부 및 수신 휴대폰 지정하기 치환문구", example = "○ 수신거부 및 수신 휴대폰 지정하기 : ")
        @Size(max = 50, message = "수신거부 및 수신 휴대폰 지정하기 치환문구는 50자를 넘을 수 없습니다.")
        private String rcveRfStr;
    }
    //-------------------------------------------------------------------

    /**
     * <pre>
     * 사전/본 문자 발송/수신 결과 전송 : BC-AG-SN-010
     * Request : KtMsgRsltRequest
     * Response : KtCommonResponse
     * </pre>
     */
    @Schema(name = "KtMsgRsltRequest", description = "사전/본 문자 발송/수신 결과 전송 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @EqualsAndHashCode(callSuper = false)
    public static class KtMsgRsltRequest {
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
         * 발송 요청 메시지 구분 : 필수 - 0|1
         * 0: 사전문자, 1: 본문자
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송 요청 메시지 구분", example = "1")
        @NotEmpty(message = "발송 요청 메시지 구분은 필수 입니다(0|1)")
        private final String reqMsgTypeDvcd = "1";

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Valid
        private List<KtMsgRsltReqData> reqs;
    }

    @Schema(name = "KtMsgRsltReqData", description = "사전/본 문자 발송/수신 결과 전송 요청 reqs DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtMsgRsltReqData extends CmmEnsRequestDTO {
        //-------------------------------------------------------------------
        // 필수
        //-------------------------------------------------------------------
        /**
         * 관리키 : 고객메시지 건별 Unique key
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "관리키", example = "S20211229102000001")
        @Size(min = 1, max = 50, message = "관리키는 필수 입니다(max:50)")
        private String srcKey;

        /**
         * <pre>
         * MMS 발송결과 상태 순번 : 필수 - max 8
         * 1:수신시(결과), 열람시(결과):2
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "MMS 발송결과 상태 순번", example = " ")
        @Digits(integer = 8, fraction = 0, message = "MMS 발송결과 상태 순번(1|2)")
        private Integer mmsSndgRsltSqno;

        /**
         * <pre>
         * 처리일자 : 필수 - 8
         * 이통사 처리일자
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리일자", example = "20231210")
        @Size(min = 8, max = 8, message = "처리일자는 필수 입니다(YYYYMMDD)")
        private String prcsDt;

        /**
         * 문서코드 : 필수 - 5자리
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "문서코드", example = "00001")
        @Size(min = 5, max = 5, message = "문서코드는 필수 입니다(5자리)")
        private String mmsBsnsDvcd;

        /**
         * <pre>
         * 모바일사업자구분 : 필수 - 2자리
         * 발송 통신사 구분(01:KT, 02:SKT, 03:LGT)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "모바일사업자구분", example = "01")
        @Size(min = 2, max = 2, message = "모바일 사업자 구분은 필수 입니다(01|02|03)")
        private String mblBzowrDvcd;

        /**
         * <pre>
         * TODO::결과코드에 따른 메세지 처리 필요
         * 발송결과코드 : 필수 - 2자리
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
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송결과코드", example = "40")
        @Size(min = 2, max = 2, message = "발송결과코드는 필수 입니다")
        private String mmsSndgRsltDvcd;

        /**
         * <pre>
         * 발송타임스탬프 : 필수 - 14
         * YYYYMMDDHHMISS
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송타임스탬프", example = "20241030121000")
        @Size(min = 14, max = 14, message = "발송타임스탬프는 필수 입니다(YYYYMMDDHHMISS)")
        private String mmsSndgTmst;

        /**
         * <pre>
         * 문발송 메시지 타입: 필수 - 1
         * RCS/xMS (RCS :1, xMS: 2)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송 메시지 타입", example = "2")
        @Size(min = 1, max = 1, message = "발송 메시지 타입은 필수 입니다")
        private String msgType;
        //-------------------------------------------------------------------

        /**
         * <pre>
         * 실제발송번호(일부) : max 20
         * 고객휴대폰번호 일부 (****0323)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "실제발송번호(일부)", example = " ")
        @Size(max = 20, message = "실제발송번호(일부)는 20자를 넘을 수 없습니다.")
        private String rlMmsSndgTelno;

        /**
         * <pre>
         * 수신타임스탬프 : 14
         * mms_sndg_rslt_sqno 코드값이 1인 경우 필수
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신타임스탬프", example = " ")
        @Size(max = 14, message = "수신타임스탬프는 14자리 입니다")
        private String mmsRcvTmst;

        /**
         * <pre>
         * 열람타임스탬프 : 14
         * mms_sndg_rslt_sqno 코드값이 2인 경우 필수
         * </pre>>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "열람타임스탬프", example = " ")
        @Size(max = 14, message = "열람타임스탬프는 14자리 입니다")
        private String mmsRdgTmst;

        /**
         * <pre>
         * 기동의발송여부: 1
         * Y: 기동의(발송 시점 전 수신동의 및 전자주소가 생성되어있는 고객)
         * N: 미동의(발송 시점까지 수신동의 및 전자주소가 생성되어있지 않은 고객)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "기동의발송여부", example = "2")
        @Size(max = 1, message = "기동의발송여부는 1자리 입니다(Y|N)")
        private String prevApproveYn;

        /**
         * <pre>
         * 수신자 공인전자주소 : max 100
         * 타 중계기관(포스토피아)에서 발송 요청 메시지 회신인 경우 필수
         * mms_sndg_rslt_sqno이 1, 2인 경우 필수
         * 송신중계자에 제공해야할 정보
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신자 공인전자주소", example = " ")
        @Size(max = 100, message = "수신자 공인전자주소는 100자를 넘을 수 없습니다.")
        private String rcvNpost;

        /**
         * <pre>
         * 수신 중계자 플랫폼 ID : max 25
         * 타 중계기관(포스토피아)에서 발송 요청 메시지 회신인 경우 필수
         * 수신 중계자플랫폼ID (각 P/F의 중계자 ID)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수신 중계자 플랫폼 ID", example = " ")
        @Size(max = 25, message = "수신 중계자 플랫폼 ID는 25자를 넘을 수 없습니다.")
        private String rcvPlfmId;

        /**
         * <pre>
         * 클릭일시 : 14(YYYYMMDDHHMISS)
         * 지정된 기관(국민연금공단, AXA자동차보험)의 메시지 열람 클릭일시(최초1회시)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "클릭일시", example = " ")
        @Size(max = 14, message = "클릭일시는 14자 입니다.")
        private String clickDt;

        /**
         * <pre>
         * 동의일시 : 14(YYYYMMDDHHMISS)
         * 지정된 기관(국민연금공단, AXA자동차보험)의 최초 동의 일시
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "동의일시", example = " ")
        @Size(max = 14, message = "동의일시는 14자 입니다.")
        private String approveDt;

        /**
         * <pre>
         * RCS 발송 여부: 1
         * Y: RCS 발송 , N : xMS 발송
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "RCS 발송 여부", example = "N")
        @Size(max = 1, message = "RCS 발송 여부는 1자리 입니다(Y|N)")
        private String rcyYn;

        /**
         * API 정의서에 없는 필드 : 업무 편의를 위해 추가
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송결과코드메세지", example = "MMS/RCS 발송 수신성공")
        @Size(max = 100, message = "발송결과코드메세지는 100자를 넘을 수 없습니다")
        private String mmsSndgRsltDvcdMsg;


        /**
         * MENS 업무처리를 위한 추가 항목
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송구분코드", example = " ")
        private String sndngSeCode;
    }
    //-------------------------------------------------------------------
}
