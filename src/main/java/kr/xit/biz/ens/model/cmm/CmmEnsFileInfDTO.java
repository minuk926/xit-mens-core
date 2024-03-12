package kr.xit.biz.ens.model.cmm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.nice.NiceCiDTO.IpinCiResEncData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>
 * description : 전자고지 파일 연계 DTO
 *
 * packageName : kr.xit.biz.ens.model.cmm
 * fileName    : CmmEnsFileInfDTO
 * author      : limju
 * date        : 2023-09-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-04    limju       최초 생성
 *
 * </pre>
 */
public class CmmEnsFileInfDTO {
    @Schema(name = "FmcExcelUpload", description = "시설관리공단 전자문서 발송대상 엑셀 파일 업로드 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode(callSuper = true)
    public static class FmcExcelUpload extends CmmEnsRequestDTO {

        //-----------------------------------------------------------------------------------
        // 시설관리공단 전자고지 대상 엑셀파일 업로드 필수 속성
        //-----------------------------------------------------------------------------------

        /**
         * 등록자 : 필수
         * 전자문서를 발송할 일시 - yyyyMMdd
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "등록자", example = "userid", description = "등록자")
        @Size(message = "등록자는 필수 입니다")
        private String register;

        /**
         * excel file : 필수
         */
        @JsonIgnore
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "excel file", example = "null", description = "업로드 excel file")
        @NotEmpty(message = "첨부파일은 필수 입니다")
        private MultipartFile[] files;


        //-----------------------------------------------------------------------------------
        // 시설관리공단 전자고지 대상 엑셀파일 업로드 결과 속성
        //-----------------------------------------------------------------------------------
        /**
         * 발송건수 : response
         * 엑셀업로드 건수
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송 건수", example = "0", description = "발송건수")
        private int sndngCo;

        /**
         * 통합발송마스터ID : response
         * 엑셀업로드 건에 대한 마스터 정보 key - 엑셀 업로드 파일 건당 생성
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "통합발송마스터ID", example = "0", description = "통합발송마스터ID")
        private String unitySndngMastrId;

        private String postDlvrSe;
        private String postTmplatCode;
    }

    @Schema(name = "FmcInfExcel", description = "시설관리공단 전자문서 발송대상 엑셀 파일 interface DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FmcInfExcel {

        //-----------------------------------------------------------------------------------
        // 시설관리공단 전자고지 대상 엑셀파일 헤더
        //-----------------------------------------------------------------------------------
        /**
         * 통합발송상세ID : 필수
         * 시설관리공단의 마스터key - unique key 이어야 함
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "파일 유일키", example = " ", description = "통합발송상세ID")
        @NotEmpty(message = "파일 유일키는 필수입니다.")
        private String unitySndngMastrId;

        /**
         * 우편물 일련번호 : 필수
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우편물 일련번호", example = " ", description = "우편물 일련번호")
        @NotEmpty(message = "우편물 일련번호는 필수입니다.")
        private String unitySndngDetailId;

        /**
         * 우편물 요청일시 : 필수
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우편물 요청일시", example = " ", description = "우편물 요청일시")
        @NotEmpty(message = "우편물 요청일시는 필수입니다.")
        private String sndngDt;

        /**
         * 우편물 발송구분 : 필수
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우편물 발송구분", example = " ", description = "우편물 발송구분")
        @NotEmpty(message = "우편물 발송구분은 필수입니다.")
        private String tmplatId;

        //-----------------------------------------------------------------------------------
        // 겉봉투 인쇄용
        //-----------------------------------------------------------------------------------
        /**
         * 발송인 명 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송인 명", example = " ", description = "발송인 명")
        private String senderNm;
        /**
         * 발송인 우편번호 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송인 우편번호", example = " ", description = "발송인 우편번호")
        private String senderZipNo;
        /**
         * 발송인 주소 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송인 주소", example = " ", description = "발송인 주소")
        private String senderAddr;
        /**
         * 발송인 상세주소 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송인 상세주소", example = " ", description = "발송인 상세주소")
        private String senderDetailAddr;
        /**
         * 발송인 전화번호 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "발송인 전화번호", example = " ", description = "발송인 전화번호")
        private String senderTelno;
        /**
         * 수취인 명 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인 명", example = " ", description = "수취인 명")
        private String recevNm;
        /**
         * 수취인 우편번호 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인 우편번호", example = " ", description = "수취인 우편번호")
        private String recevZipNo;
        /**
         * 수취인 주소 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인 주소", example = " ", description = "수취인 주소")
        private String recevAddr;
        /**
         * 수취인 상세주소 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인 상세주소", example = " ", description = "수취인 상세주소")
        private String recevDetailAddr;
        /**
         * 수취인 전화번호 : 겉봉투 인쇄용
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인 전화번호", example = " ", description = "수취인 전화번호")
        private String recevTelno;


        //-----------------------------------------------------------------------------------
        // 전자고지
        //-----------------------------------------------------------------------------------
        /**
         * 수취인 주민등록번호
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "수취인 주민등록번호", example = " ", description = "수취인 주민등록번호-전자고지 일 경우 필수")
        private String recveJuminno;


        //-----------------------------------------------------------------------------------
        // 내용물
        //-----------------------------------------------------------------------------------
        /**
         * 고지서 구분명
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "고지서 구분명", example = " ", description = "고지서 구분명")
        private String gojiGubun;

        /**
         * 고지서 명 
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "고지서 명", example = " ", description = "고지서 명")
        private String gojiNm;

        /**
         * 고지서 상세명
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "고지서 상세명", example = " ", description = "고지서 상세명")
        private String gojiDetailNm;

        /**
         * QR 바코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "QR 바코드", example = " ", description = "QR 바코드")
        @NotEmpty(message = "QR 바코드는 필수입니다.")
        private String qrBarcode;

        /**
         * 부과대상
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "부과대상", example = " ", description = "부과대상")
        @NotEmpty(message = "부과대상은 필수입니다.")
        private String buTarget;

        /**
         * 자료관리번호
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "자료관리번호", example = " ", description = "자료관리번호")
        @NotEmpty(message = "자료관리번호는 필수입니다.")
        private String dataSn;

        /**
         * 관리비기간
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "관리비기간", example = " ", description = "관리비기간")
        private String costPd;

        /**
         * 기관번호
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "기관번호", example = " ", description = "기관번호")
        private String taxNum1;
        /**
         * 세목
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "세목", example = " ", description = "세목")
        private String taxNum2;

        /**
         * 납세년월기
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "납세년월기", example = " ", description = "납세년월기")
        private String taxNum3;

        /**
         * 과세번호
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "과세번호", example = " ", description = "과세번호")
        private String taxNum4;

        /**
         * 부과내역1
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "부과내역1", example = " ", description = "부과내역1")
        private String buContent1;
        /**
         * 부과내역2
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "부과내역2", example = " ", description = "부과내역2")
        private String buContent2;

        /**
         * 부과내역3
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "부과내역3", example = " ", description = "부과내역3")
        private String buContent3;

        /**
         * 부과내역4
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "부과내역4", example = " ", description = "부과내역4")
        private String buContent4;

        /**
         * 납부금액명1
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납부금액명1", example = " ", description = "납부금액명1")
        @NotEmpty(message = "납부금액명1은 필수입니다.")
        private String napAmountNm1;
        /**
         * 납부금액명2
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납부금액명2", example = " ", description = "납부금액명2")
        @NotEmpty(message = "납부금액명2는 필수입니다.")
        private String napAmountNm2;

        /**
         * 납부금액상세명1
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납부금액상세명1", example = " ", description = "납부금액상세명1")
        @NotEmpty(message = "납부금액상세명1은 필수입니다.")
        private String napAmountDetailNm1;

        /**
         * 납부금액상세명2
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납부금액상세명2", example = " ", description = "납부금액상세명2")
        @NotEmpty(message = "납부금액상세명2는 필수입니다.")
        private String napAmountDetailNm2;

        /**
         * 납부금액상세명3
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납부금액상세명3", example = " ", description = "납부금액상세명3")
        @NotEmpty(message = "납부금액상세명3은 필수입니다.")
        private String napAmountDetailNm3;
        /**
         * 납기구분1
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기구분1", example = " ", description = "납기구분1")
        @NotEmpty(message = "납기구분1은 필수입니다.")
        private String napGubun1;

        /**
         * 납기구분2
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기구분2", example = " ", description = "납기구분2")
        @NotEmpty(message = "납기구분2는 필수입니다.")
        private String napGubun2;

        /**
         * 납기내기한
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "납기내기한", example = " ", description = "납기내기한")
        private String napPd;

        /**
         * 납기내금액1
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기내금액1", example = " ", description = "납기내금액1")
        @NotEmpty(message = "납기내금액1은 필수입니다.")
        private String napAmount1;
        /**
         * 납기내금액2
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기내금액2", example = " ", description = "납기내금액2")
        @NotEmpty(message = "납기내금액2는 필수입니다.")
        private String napAmount2;

        /**
         * 납기내금액3
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기내금액3", example = " ", description = "납기내금액3")
        @NotEmpty(message = "납기내금액3은 필수입니다.")
        private String napAmount3;

        /**
         * 납기내금액4
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기내금액4", example = " ", description = "납기내금액4")
        @NotEmpty(message = "납기내금액4는 필수입니다.")
        private String napAmount4;

        /**
         * 납기내합계액
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기내합계액", example = " ", description = "납기내합계액")
        @NotEmpty(message = "납기내합계액은 필수입니다.")
        private String napAmountTotal;
        /**
         * 납기후기한
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기후기한", example = " ", description = "납기후기한")
        @NotEmpty(message = "납기후기한은 필수입니다.")
        private String napAftPd;

        /**
         * 납기후금액1
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기후금액1", example = " ", description = "납기후금액1")
        @NotEmpty(message = "납기후금액1은 필수입니다.")
        private String napAftAmount1;

        /**
         * 납기후금액2
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기후금액2", example = " ", description = "납기후금액2")
        @NotEmpty(message = "납기후금액2는 필수입니다.")
        private String napAftAmount2;

        /**
         * 납기후금액3
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기후금액3", example = " ", description = "납기후금액3")
        @NotEmpty(message = "납기후금액3은 필수입니다.")
        private String napAftAmount3;
        /**
         * 납기후금액4
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기후금액4", example = " ", description = "납기후금액4")
        @NotEmpty(message = "납기후금액4는 필수입니다.")
        private String napAftAmount4;

        /**
         * 납기후합계액
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "납기후합계액", example = " ", description = "납기후합계액")
        @NotEmpty(message = "납기후합계액는 필수입니다.")
        private String napAftAmountTotal;

        /**
         * 출력일자
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "출력일자", example = " ", description = "출력일자")
        @NotEmpty(message = "출력일자는 필수입니다.")
        private String prtDe;

        /**
         * 담당자
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "담당자", example = " ", description = "담당자")
        @NotEmpty(message = "담당자는 필수입니다.")
        private String worker;
        /**
         * 농협가상계좌
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "농협가상계좌", example = " ", description = "농협가상계좌")
        @NotEmpty(message = "농협가상계좌는 필수입니다.")
        private String nVacct;

        /**
         * 국민가상계좌
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "국민가상계좌", example = " ", description = "국민가상계좌")
        @NotEmpty(message = "국민가상계좌는 필수입니다.")
        private String kVacct;

        /**
         * 우리가상계좌
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우리가상계좌", example = " ", description = "우리가상계좌")
        @NotEmpty(message = "우리가상계좌는 필수입니다.")
        private String wVacct;

        /**
         * 신한가상계좌
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "신한가상계좌", example = " ", description = "신한가상계좌")
        @NotEmpty(message = "신한가상계좌는 필수입니다.")
        private String sVacct;
        /**
         * 하나가상계좌
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "하나가상계좌", example = " ", description = "하나가상계좌")
        @NotEmpty(message = "하나가상계좌는 필수입니다.")
        private String hVacct;

        /**
         * 우체국가상계좌
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "우체국가상계좌", example = " ", description = "우체국가상계좌")
        @NotEmpty(message = "우체국가상계좌는 필수입니다.")
        private String pVacct;

        /**
         * 가상계좌받는분
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "가상계좌받는분", example = " ", description = "가상계좌받는분")
        @NotEmpty(message = "가상계좌받는분은 필수입니다.")
        private String vacctNm;
    }

    @Schema(name = "FmcInfExcelRslt", description = "시설관리공단 전자문서 발송대상 엑셀 파일 interface 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode(callSuper = true)
    public static class FmcInfExcelRslt extends IpinCiResEncData {

        /**
         * 통합발송상세ID : 필수
         * 시설관리공단의 마스터key - unique key 이어야 함
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "통합발송상세ID")
        private String unitySndngDetailId;

        /**
         * code - error code
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "result")
        private String resultCd;

        /**
         * message - error message
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "message")
        private String message;

        /**
         * 등록 일시
         */
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        private LocalDateTime registDt;
        /**
         * 등록자
         */
        private String register;
    }
}
