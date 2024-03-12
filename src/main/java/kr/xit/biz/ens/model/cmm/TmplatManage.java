package kr.xit.biz.ens.model.cmm;

import java.io.Serializable;
import kr.xit.core.model.AuditFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TmplatManage extends AuditFields implements Serializable {

    /**
     * 템플릿 ID
     */
    private String tmplatId;
    /**
     * 시군구 코드
     */
    private String signguCode;
    /**
     * 과태료 코드
     */
    private String ffnlgCode;
    /**
     * 발송 유형 코드
     */
    private String sndngTyCode;
    /**
     * 템플릿 명
     */
    private String tmplatNm;
    /**
     * 템플릿 제목
     */
    private String tmplatSj;
    /**
     * 템플릿 내용
     */
    private String tmplatCn;
    /**
     * 고객 센터 전화 번호
     */
    private String cstmrCnterTlphonNo;
    /**
     * REDIRECT URL
     */
    private String redirectUrl;
    /**
     * try1
     */
    private String try1;
    /**
     * try2
     */
    private String try2;
    /**
     * try3
     */
    private String try3;
    /**
     * try2_minute
     */
    private int try2Minute;
    /**
     * try3_minute
     */
    private int try3Minute;
    /**
     * 사용 여부
     */
    private String useAt;

    private String postDlvrSe;
    private String postTmplatCode;
}
