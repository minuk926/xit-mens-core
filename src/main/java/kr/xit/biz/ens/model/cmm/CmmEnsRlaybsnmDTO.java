package kr.xit.biz.ens.model.cmm;

import java.io.Serializable;
import kr.xit.core.model.AuditFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description : 전자고지 문서중계자 정보 DTO
 *
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : KtMmsDTO
 * author      : limju
 * date        : 2023-09-22
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-22    limju       최초 생성
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CmmEnsRlaybsnmDTO extends AuditFields implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 시군구 코드
     */
    private String signguCode;
    /**
     * 과태료 코드
     */
    private String ffnlgCode;
    /**
     * profile
     */
    private String profile;
    /**
     * 시군구 명
     */
    private String signguNm;
    /**
     * 과태료 명
     */
    private String ffnlgNm;
    /**
     * KAKAO CLIENT ID
     */
    private String kakaoClientId;
    /**
     * KAKAO 상품 코드
     */
    private String kakaoProductCd;
    /**
     * KAKAO ACCESS TOKEN
     */
    private String kakaoAccessToken;
    /**
     * KAKAO CONTRACT UUID
     */
    private String kakaoContractUuid;
    /**
     * KT client id
     */
    private String ktClientId;
    /**
     * KT client tp
     */
    private String ktClientTp;
    /**
     * KT Scope
     */
    private String ktScope;
    /**
     * KT Service code
     */
    private String ktServiceCode;
    /**
     * KT service client ID
     */
    private String ktSvcClientId;
    /**
     * KT service client secret
     */
    private String ktSvcClientSecret;
    /**
     * KT service cerf key
     */
    private String ktSvcCerfKey;
    /**
     * KT_ACCESS_TOKEN
     */
    private String ktAccessToken;

    /**
     * KT 토큰 유효 기간
     */
    private String ktTokenExpiresIn;
    /**
     * KT 토큰 식별자
     */
    private String ktTokenJti;
    /**
     * postplus apiKey
     */
    private String pplusApiKey;

    /**
     * EPost service key
     */
    private String epostServiceKey;

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
}
