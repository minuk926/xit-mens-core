package kr.xit.biz.ens.model.cntc;

import java.io.Serializable;
import kr.xit.core.model.AuditFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description : tb_cntc_ Entity DTO
 *
 * packageName : kr.xit.biz.ens.model
 * fileName    : CntcDTO
 * author      : limju
 * date        : 2023-08-31
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-08-31    limju       최초 생성
 *
 * </pre>
 */
public class CntcDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class SndngMst extends AuditFields implements Serializable {
        /**
         * 통합 발송 마스터 id
         */
        private String unitySndngMastrId;
        /**
         * 시군구 코드
         */
        private String signguCode;
        /**
         * 과태료 코드
         */
        private String ffnlgCode;
        /**
         * 템플릿 ID
         */
        private String tmplatId;
        /**
         * 발송 유형 코드
         */
        private String sndngTyCode;
        /**
         * 발송 건수
         */
        private int sndngCo;
        /**
         * 발송 처리 상태
         */
        private String sndngProcessSttus;
        /**
         * 발송 일시
         */
        private String sndngDt;
        /**
         * 마감 일시
         */
        private String closDt;
        /**
         * 에러 코드
         */
        private String errorCode;
        /**
         * 에러 메시지
         */
        private String errorMssage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class SndngDtl extends AuditFields implements Serializable {

        /**
         * 통합 발송 상세 id
         */
        private String unitySndngDetailId;
        /**
         * 통합 발송 마스터 id
         */
        private String unitySndngMastrId;
        /**
         * 시군구 코드
         */
        private String signguCode;
        /**
         * 과태료 코드
         */
        private String ffnlgCode;
        /**
         * 메인 코드
         */
        private String mainCode;
        /**
         * 주민등록번호
         */
        private String ihidnum;
        /**
         * 핸드폰 번호
         */
        private String moblphonNo;
        /**
         * 성명
         */
        private String nm;
        /**
         * 주소
         */
        private String adres;
        /**
         * 상세 주소
         */
        private String detailAdres;
        /**
         * 우편번호
         */
        private String zip;
        /**
         * 템플릿 메시지 데이터
         */
        private String tmpltMsgData;
        /**
         * 모바일 페이지 내용
         */
        private String mobilePageCn;
        /**
         * 이용 기관 식별 ID
         */
        private String useInsttIdntfcId;
        /**
         * 외부 문서 식별 번호
         */
        private String externalDocumentUuid;

        /**
         * 템플릿 ID
         */
        private String tmplatId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class SndngResult extends AuditFields implements Serializable {

        /**
         * 통합 발송 상세 id
         */
        private String unitySndngDetailId;
        /**
         * 발송 구분 코드
         */
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
         * 발송 결과 상태
         */
        private String sndngResultSttus;
        /**
         * 요청 일시
         */
        private String requstDt;
        /**
         * 조회 일시
         */
        private String inqireDt;
        /**
         * 열람 일시
         */
        private String readngDt;
        /**
         * 오류 내용
         */
        private String errorCn;

        /**
         * documentBinderUuid
         */
        private String documentBinderUuid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class PostPlusJson extends AuditFields implements Serializable {
        /**
         * 통합 발송 상세 id
         */
        private String unitySndngDetailId;
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
         * 순번
         */
        private String sn;
        /**
         * master_cols
         */
        private String masterCols;
        /**
         * master_rows
         */
        private String masterRows;
        /**
         * detail_cols
         */
        private String detailCols;
        /**
         * detail_rows
         */
        private String detailRows;
        /**
         * 기관번호
         */
        private String taxNum1;
        /**
         * 세목
         */
        private String taxNum2;
        /**
         * 납세년월기
         */
        private String taxNum3;
        /**
         * 과세번호
         */
        private String taxNum4;
    }
}
