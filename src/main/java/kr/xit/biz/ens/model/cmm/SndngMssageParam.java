package kr.xit.biz.ens.model.cmm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.biz.ens.model.cmm
 * fileName    : SndngMssageParam
 * author      : limju
 * date        : 2023-11-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-11-01    limju       최초 생성
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SndngMssageParam {
    private String signguCode;
    private String ffnlgCode;
    private String profile;

    /**
     * 통합 발송 마스터 id
     */
    private String unitySndngMastrId;
    /**
     * 통합 발송 마스터 id
     */
    private String unitySndngDetailId;
    /**
     * 발송 마스터 id
     */
    private String sndngMastrId;

    /**
     * 발송상세ID
     */
    private String sndngDetailId;

    /**
     * 템플릿ID
     */
    private String tmplatId;

    /**
     * 발송 건수
     */
    private int sndngCo;

    private String sndngProcessSttus;
    /**
     * 발송 처리 상태
     */
    private String newSndngProcessSttus;

    private String try1;
    private String try2;
    private String try3;
    private int tryCnt;
    private int trySeq;
    private String sndngSeCode;
    private String sndngDt;
    private String sndngDt2;
    private String sndngDt3;
    private String try2Minute;
    private String try3Minute;
    private String errorMssage;
    private String errorCode;

    private String resultCd;
    private String resultDt;

    private String url;
}
