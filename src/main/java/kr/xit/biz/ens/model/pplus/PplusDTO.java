package kr.xit.biz.ens.model.pplus;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.Serializable;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.cmm.CmmEnsRequestDTO;
import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : Postopia(Postplus) DTO
 *               - 우편제작 접수 요청
 *                 Request :
 *                 Response : {@link PpCommonResponse}
 *               - 우편제작 상태 조회
 *                 Request : {@link PpStatusRequest}
 *                 Response : {@link PpStatusResponse}
 * packageName : kr.xit.biz.ens.model.pplus
 * fileName    : PplusDTO
 * author      : limju
 * date        : 2023-10-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-10-04    limju       최초 생성
 *
 * </pre>
 */
public class PplusDTO {
    /**
     * <pre>
     * Postplus 우편제작 접수 요청 결과 DTO
     * 성공 : 결과 - "OK"
     * </pre>
     */
    @Schema(name = "PpAcceptResponse", description = "Postplus 우편제작 접수 요청 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PpCommonResponse implements IApiResponse {

        /**
         * <pre>
         * 결과
         * 처리결과 코드 OK 또는 ERROR
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 결과;

        /**
         * <pre>
         * 비고
         * 처리결과 실패인 경우 에러 메시지
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 비고;

        @Setter
        private String unitySndngMastrId;
    }

    /**
     * <pre>
     * Postplus 우편제작 접수 요청 Data Master DTO
     * </pre>
     */
    @Schema(name = "PpAcceptReqDataMst", description = "Postplus 우편제작 접수 요청 Data master DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PpAcceptReqDataMst {
        //-----------------------------------------------------------------------
        // 필수
        //-----------------------------------------------------------------------
        /**
         * <pre>
         * 버전 : v1.10
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "버전", example = "v1.10")
        @NotEmpty(message = "버전은 필수 입니다")
        private final String 버전 = "v1.10";

        /**
         * <pre>
         * 테스트여부 : default "N" - 인쇄 직전 단계까지 진행
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "테스트여부", example = "N")
        @NotEmpty(message = "테스트여부는 필수 입니다(Y|N)")
        private final String 테스트여부 = "N";

        /**
         * <pre>
         * 서비스 : PST - 우편, SMS - 문자, KKO - 카카오톡
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스", example = "PST")
        @NotEmpty(message = "서비스 코드는 필수 입니다(PST)")
        private final String 서비스 = "PST";

        /**
         * <pre>
         * 연계식별키 : 사용자 지정 unique key
         * max : 40byte
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "연계식별키", example = " ")
        @Size(min = 5, max = 40, message = "연계식별키는 필수입니다(max:40)")
        private String 연계식별키;

        /**
         * <pre>
         * 봉투 : 소봉투|대봉투|접착시|엽서
         * 소봉투/대봉투는 A4(210*297) 기준
         * 접착식이 첨부파일의 경우 주소가 포함되어야 하며 '주소페이지유무' 항목이 'Y여야 합니다.
         * 접착식이 템플릿의 경우는 data에 주소가 명시되어야 합니다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "봉투", example = "소봉투")
        @NotEmpty(message = "봉투는 필수입니다(소봉투|대봉투|접착시|엽서)")
        private String 봉투;

        /**
         * <pre>
         * 봉투창 : 이중창|단창|무창
         * 소봉투(이중창,무창) 대봉투(단창,무창) 접착식(공백), 엽서(공백)
         * 무창은 사전협의가 필요합니다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "봉투창", example = "이중창")
        @NotEmpty(message = "봉투창은 필수입니다(이중창|단창|무창)")
        private String 봉투창;

        /**
         * <pre>
         * 흑백칼라 : 흑백|칼라
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "흑백칼라", example = "흑백")
        @NotEmpty(message = "흑백칼라는 필수입니다(흑백|칼라)")
        private String 흑백칼라;

        /**
         * <pre>
         * 단면양면 : 단면|양면
         * 접착식 경우 페이지수가 2매인 경우는 양면인쇄 됩니다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "단면양면", example = "단면")
        @NotEmpty(message = "단면양면은 필수입니다(단면|양면)")
        private String 단면양면;

        /**
         * <pre>
         * 배달 : 일반|등기|준등기|악일특급|선택등기
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "배달", example = "일반")
        @NotEmpty(message = "배달은 필수입니다(일반|등기|준등기|악일특급|선택등기)")
        private String 배달;

        /**
         * <pre>
         * 템플릿출력여부 : 템플릿코드 존재시 Y
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "템플릿코드", example = "N")
        @NotEmpty(message = "템플릿출력여부는 필수입니다(Y|N)")
        private final String 템플릿출력여부 = StringUtils.isEmpty(this.템플릿코드) ? "N" : "Y";

        /**
         * <pre>
         * 여백생성유무
         * Y: 첨부파일 내용문을 자동으로 축소
         * 소봉투는 봉입 바코드 추가위해 왼쪽 여백 필요
         * 프린터기를 이용하여 출력하므로 상하좌우의 margin이 필요
         *  - 대봉투의 경우 상 8mm, 하좌우 5mm의 margin이 필요
         *  - 소봉투의 경우 상 8mm, 하우 5mm, 좌 17mm의 margin이 필요
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "여백생성유무", example = "N")
        @NotEmpty(message = "여백생성유무는 필수입니다(Y|N)")
        private final String 여백생성유무 = "N";

        /**
         * <pre>
         * 주소페이지유무
         * 주소페이지가 첨부파일에 포함 되어있으면 Y
         * 주소페이지 생성이 필요하면 N
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "여백생성유무", example = "N")
        @NotEmpty(message = "주소페이지유무는 필수입니다(Y|N)")
        private final String 주소페이지유무 = "N";

        /**
         * <pre>
         * 맞춤자제유무
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "맞춤자제유무", example = "N")
        @NotEmpty(message = "맞춤자제유무는 필수입니다(Y|N)")
        private final String 맞춤자제유무 = "N";

        /**
         * <pre>
         * 메일머지유무
         * Y 일경우 템플릿코드 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "메일머지유무", example = "N")
        @NotEmpty(message = "메일머지유무는 필수입니다(Y|N)")
        private final String 메일머지유무 = "N";

        /**
         * <pre>
         * 동봉물유무
         * Y 일경우 템플릿코드 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "동봉물유무", example = "N")
        @NotEmpty(message = "동봉물유무는 필수입니다(Y|N)")
        private final String 동봉물유무 = "N";

        /**
         * <pre>
         * 반송여부
         * 반송불필요: N
         * 반송필요: Y (반송요금 별도 협의후 이용)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "반송여부유무", example = "N")
        @NotEmpty(message = "반송여부는 필수입니다(Y|N)")
        //private final String 반송여부 = "N";
        private final String[] 반송여부 = new String[]{"Y", "N"};

        /**
         * <pre>
         * 스테이플러유무
         * 협의 후 사용 가능합니다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "스테이플러유무", example = "N")
        @NotEmpty(message = "스테이플러유무는 필수입니다(Y|N)")
        private final String 스테이플러유무 = "N";

        /**
         * <pre>
         * 발송인명
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송인명", example = "포스토피아")
        @NotEmpty(message = "발송인명은 필수입니다")
        private String 발송인명;

        /**
         * <pre>
         * 발송인우편번호
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송인우편번호", example = "05048")
        @NotEmpty(message = "발송인우편번호는 필수입니다")
        private String 발송인우편번호;

        /**
         * <pre>
         * 발송인주소
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송인주소", example = "서울특별시 광진구 강변로역2")
        @NotEmpty(message = "발송인주소는 필수입니다")
        private String 발송인주소;

        /**
         * <pre>
         * 발송인상세주소
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "발송인상세주소", example = "구의동, 서울광진우체국")
        @NotEmpty(message = "발송인상세주소는 필수입니다")
        private String 발송인상세주소;
        //-----------------------------------------------------------------------

        /**
         * <pre>
         * 템플릿코드 : 고객협의 후 포스토피아에서 템플릿 설정, 발급
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "템플릿코드", example = " ")
        private String 템플릿코드;

        /**
         * <pre>
         * 수취인수
         * 미입력시 주소록개수 참조
         * 입력시 주소록개수와 같을시만 발송
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인수", example = " ")
        private Integer 수취인수;

        /**
         * <pre>
         * 동봉물페이지수
         * 동봉물 존재시 동봉물의 페이지수 기재
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "동봉물페이지수", example = " ")
        private Integer 동봉물페이지수;

        /**
         * <pre>
         * 로고 파일
         * 미입력시 로고가 없거나 템플릿에 포함되어 있음
         * 협의 후 사용 가능합니다.
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "로고파일", example = " ")
        private String 로고파일;

        /**
         * <pre>
         * 발송인전화번호
         * 준등기/등기/익일특급/선택등기 발송시 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송인전화번호", example = "010-XXXX-XXXX")
        private String 발송인전화번호;
    }

    /**
     * <pre>
     * Postplus 우편제작 접수 요청 Data Detail DTO
     * </pre>
     */
    @Schema(name = "PpAcceptReqDataDtl", description = "Postplus 우편제작 접수 요청 Data detail DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PpAcceptReqDataDtl {
        //-----------------------------------------------------------------------
        // 필수
        //-----------------------------------------------------------------------
        /**
         * <pre>
         * 순번 : "1" 부터 시작
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "순번", example = "1")
        @NotEmpty(message = "순번은 필수입니다")
        private String 순번;

        /**
         * <pre>
         * 이름 : 수취인명
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "이름", example = "홍길동")
        @NotEmpty(message = "이름은 필수입니다")
        private String 이름;

        /**
         * <pre>
         * 우편번호 : 수취인 우편번호
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우편번호", example = "05048")
        @NotEmpty(message = "우편번호는 필수입니다")
        private String 우편번호;

        /**
         * <pre>
         * 주소 : 수취인 주소
         * 상세주소 포함 최대 200자
         * 상세주소를 포함하지 않는 경우 최대 100자
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "주소", example = "서울특별시 광진구 강변역로2")
        @Size(min = 5, max = 200, message = "주소는 필수입니다(max:200)")
        private String 주소;


        //-----------------------------------------------------------------------
        /**
         * <pre>
         * 상세주소 : 수취인 주소
         * 주소에 포함된 경우 상세주소는 공백
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "상세주소", example = "서울광진우체국 B동 4층")
        private String 상세주소;

        /**
         * <pre>
         * 전화번호 : 수취인 전화번호(준등기/등기/익일특급/선택등기 발송시 필수)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "전화번호", example = " ")
        private String 전화번호;

        /**
         * <pre>
         * 첨부파일
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "첨부파일", example = " ")
        private String 첨부파일;

        /**
         * <pre>
         * 이미지갯수 : 수취인별 이미지파일의 개수
         * 오류시 정상 발송 불가
         * 여러 파일 첨부시 전체파일을 1개의 ZIP파일로 압축하여 pstFile로 전송
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "이미지갯수", example = " ")
        private Integer 이미지갯수;

        /**
         * <pre>
         * 이미지1 파일명
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "이미지1", example = " ")
        private String 이미지1;

        /**
         * <pre>
         * 이미지2 파일명
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "이미지2", example = " ")
        private String 이미지2;

        /**
         * <pre>
         * 이미지3 파일명
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "이미지3", example = " ")
        private String 이미지3;

        /**
         * <pre>
         * 가변 : 최대 200개 까지
         * </pre>
         */
    }

    /**
     * <pre>
     * Postplus 우편제작 상태 조회 요청 DTO
     * Request: PpStatusRequest
     * Response: PpStatusResponse
     * </pre>
     */
    @Schema(name = "PpStatusRequest", description = "Postplus 우편제작 상태 조회 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode(callSuper = false)
    public static class PpStatusRequest extends CmmEnsRequestDTO {

        /**
         * <pre>
         * apiKey
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "apiKey", example = " ")
        @NotEmpty(message = "apiKey는 필수 입니다.")
        private String apiKey;

        /**
         * <pre>
         * 우편 제작 요청 연계 식별키
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우편제작요청연계식별키", example = "TEST20230221_000001")
        @NotEmpty(message = "우편 제작 요청 연계 식별키는 필수 입니다.")
        private String inputCode;
    }

    /**
     * <pre>
     * Postplus 우편제작 상태 조회 결과 DTO
     * 성공 :
     * </pre>
     */
    @Schema(name = "PpStatusResponse", description = "Postplus 우편제작 상태 조회 결과 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PpStatusResponse extends PpCommonResponse {
        //---------------------------------------------------------
        // 필수
        //---------------------------------------------------------
        /**
         * <pre>
         * 신청일자 : YYYYMMDD0000
         * 우편 제작 요청 접수일
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 신청일자;

        /**
         * <pre>
         * 봉투 : 소봉투|대봉투|접착시|엽서
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 봉투;

        /**
         * <pre>
         * 배달 : 일반|등기|준등기|악일특급|선택등기
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 배달;

        /**
         * <pre>
         * 흑백칼라 : 흑백|칼라
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 흑백칼라;

        /**
         * <pre>
         * 단면양면 : 단면|양면
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 단면양면;

        /**
         * <pre>
         * 발송건수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private Integer 발송건수;

        /**
         * <pre>
         * 상태: 출력대기|출력|봉입|우체국접수중|제작발송완료|접수취소|확인불가
         * 확인불가 -> 일반 오류상태 msg
         * 출력대기 -> 제작 전단계
         * 출력/봉입/우체국접수중 -> 제작 중
         * 제작발송완료 -> 제작 후 우체국접수, 후불결제시 과금대상
         * 접수취소 -> 제작 취소
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 상태;

        /**
         * <pre>
         * 연계식별키 : 우편 제작 요청 연계 식별키
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        private String 연계식별키;
        //---------------------------------------------------------

        /**
         * <pre>
         * 시작등기번호 : 등기의 경우 표기
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO)
        private String 시작등기번호;

        /**
         * <pre>
         * 종료등기번호 : 등기의 경우 표기
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO)
        private String 종료등기번호;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BatchAcceptRequest implements Serializable {
        /**
         * 통합 발송 마스터 id
         */
        private String unitySndngMastrId;
        /**
         * 통합 발송 상세 id
         */
        private String unitySndngDetailId;
        /**
         * 발송 마스터 id
         */
        private String sndngMastrId;
        /**
         * 발송 상세 id
         */
        private String sndngDetailId;
        /**
         * 시군구 코드
         */
        private String signguCode;
        /**
         * 과태료 코드
         */
        private String ffnlgCode;
        /**
         * 서비스 코드
         */
        private String serviceCd;
        /**
         * 연계 식별 키
         */
        private String conKey;
        /**
         * 발송 처리 상태
         */
        private String sndngProcessSttus;

        private String masterCols;
        private String masterRows;
        private String detailCols;
        private String detailRows;







        /**
         * 발송인 명
         */
        private String senderNm;
        /**
         * 발송인 우편번호
         */
        private String senderZipNo;
        /**
         * 발송인 주소
         */
        private String senderAddr;
        /**
         * 발송인 상세 주소
         */
        private String senderDetailAddr;
        /**
         * 수취인 일련 번호
         */
        private String receiverSendNo;
        /**
         * 수취인 명
         */
        private String receiverNm;
        /**
         * 수취인 우편번호
         */
        private String receiverZipNo;
        /**
         * 수취인 주소
         */
        private String receiverAddr;
        /**
         * 수취인 상세 주소
         */
        private String receiverDetailAddr;
        /**
         * 가변 1
         */
        private String sschnge1;
        /**
         * 가변 2
         */
        private String sschnge2;
        /**
         * 가변 3
         */
        private String sschnge3;



    }

    @Schema(name = "PpStatusResMapping", description = "Postplus 우편제작 상태 조회 결과 Mapping DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class PpStatusResMapping {
        /**
         * <pre>
         * 연계식별키 : 우편 제작 요청 연계 식별키
         * </pre>
         */
        private String unitySndngMastrId;

        /**
         * 순번
         */
        private Integer sn;

        /**
         * <pre>
         * 상태: 출력대기|출력|봉입|우체국접수중|제작발송완료|접수취소|확인불가
         * 확인불가 -> 일반 오류상태 msg
         * 출력대기 -> 제작 전단계
         * 출력/봉입/우체국접수중 -> 제작 중
         * 제작발송완료 -> 제작 후 우체국접수, 후불결제시 과금대상
         * 접수취소 -> 제작 취소
         * </pre>
         */
        private String processSttus;

        /**
         * <pre>
         * 배달상태
         * </pre>
         */
        private String dlvrSttus;

        /**
         * <pre>
         * 등기번호
         * </pre>
         */
        private String rgistNo;
    }


    @Schema(name = "PplusResult", description = "Postplus 상태 조회 결과 Mapping DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class PplusResult extends PpStatusResMapping {
        /**
         * <pre>
         * 통합발송 상세ID
         * </pre>
         */
        private String unitySndngDetailId;

        private String sndngSeCode;

        /**
         * 시군구 코드
         */
        private String signguCode;
        /**
         * 과태료 코드
         */
        private String ffnlgCode;

        /**
         * <pre>
         * 보낸 날자 : 필수 - 10자리
         * 발신일자 yyyy-mm-dd
         * </pre>
         */
        private String senderData;

        /**
         * <pre>
         * 받는 사람 : 필수 - max 50
         * </pre>
         */
        private String receiveName;

        /**
         * <pre>
         * 받은 날자 : 필수 - 10자리
         * 수신일자 yyyy-mm-dd
         * </pre>
         */
        private String receiveDate;

        private String errorCn;
    }
}
