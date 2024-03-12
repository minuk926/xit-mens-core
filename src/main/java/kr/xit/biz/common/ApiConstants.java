package kr.xit.biz.common;

import java.util.Arrays;
import kr.xit.core.exception.BizRuntimeException;
import lombok.Getter;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.ens.support.common
 * fileName    : KakaoConstants
 * author      : limju
 * date        : 2023-05-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-04    limju       최초 생성
 *
 * </pre>
 */
public class ApiConstants {

    public static final String PROFILE = System.getProperty("spring.profiles.active");
    public static final String FFNLN_CODE = "11";

    /**
     * 구분자 없는 date-time 포맷
     */
    public static final String FMT_DT_EMPTY_DLT = "yyyyMMddHHmmss";

    /**
     * date-time 표준 포맷
     */
    public static final String FMT_DT_STD = "yyyy-MM-dd HH:mm:ss";

    /**
     * <pre>
     * 문서 조회 버튼의 명칭을 구분하기 위한 값
     * code(실제 파라미터로 보내는 값) : button_name(내문서함 내부에 표기되는 버튼 명칭)- name(내문서함 내부에서 구분하기 위한 명칭)
     * Default : 문서확인 - Default
     * BILL : 문서확인 - 고지서
     * BILL_PAY : 문서확인후 납부 - 납부가 포함된 고지서
     * NOTICE : 문서확인 - 안내문
     * CONTRACT : 문서확인 - 계약서
     * REPORT : 문서확인 - 리포트
     * </pre>
     */
    @Getter
    public enum Categories {
        DEFAULT("Default")
        , BILL("BILL")
        , BILL_PAY("BILL_PAY")
        , NOTICE("NOTICE")
        , CONTRACT("CONTRACT")
        , REPORT("REPORT")
        ;

        private final String code;

        Categories(String code) {
            this.code = code;
        }

    }

    /**
     * <pre>
     * INVALID_VALUE : http status code : 400
     *                 파라미터가 형식에 맞지않음 혹은 필수파라미터 누락
     *                 {"error_code": "INVALID_VALUE", "error_message": "유효하지 않은 값입니다."
     * UNIDENTIFIED_USER : http status code : 400
     *                     받는이의 정보로 발송대상을 특정 할 수 없을때
     *                     {"error_code": "INVALID_VALUE", "error_message": "유효하지 않은 값입니다."
     * UNAUTHORIZED : http status code : 401
     *                access token이 유효하지 않거나 잘못된 경우
     *                {"error_code": "UNAUTHORIZED","error_message": "접근 권한이 없습니다."
     * FORBIDDEN : http status code : 403
     *             문서의 대상자가 내문서함에서 수신거부를 한 경우
     *             {"error_code": "FORBIDDEN","error_message": "허용되지 않는 요청입니다. 수신거부된 사용자 입니다."}
     * NOT_FOUND : http status code : 404
     *             "Contract-Uuid" or "document_binder_uuid"가 유효하지 않거나 잘못된 경우
     *             {"error_code": "NOT_FOUND","error_message": "요청 정보를 찾을 수 없습니다."
     * INTERNAL_ERROR : http status code : 500
     *                  카카오페이 서버에러
     *                  {"error_code": "INTERNAL_SERVER_ERROR","error_message": "서버 에러입니다. 다시 시도해 주세요."}
     *  </pre>
     * 카카오페이 전자문서 발송 요청 에러 코드
     */
    @Getter
    public enum Error {
        INVALID_VALUE("INVALID_VALUE")
        , UNIDENTIFIED_USER("UNIDENTIFIED_USER")
        , UNAUTHORIZED("UNAUTHORIZED")
        , FORBIDDEN("FORBIDDEN")
        , NOT_FOUND("NOT_FOUND")
        , INTERNAL_ERROR("INTERNAL_ERROR")
        ;

        private final String code;

        Error(String code) {
            this.code = code;
        }

    }

    /**
     * 카카오페이 문서 상태
     * SENT(송신) > RECEIVED(수신) > READ(열람)/EXPIRED(미열람자료의 기한만료)
     */
    @Getter
    public enum DocBoxStatus {
        SENT("SENT")
        , RECEIVED("RECEIVED")
        , READ("READ")
        , EXPIRED("EXPIRED")
        ;

        private final String code;

        DocBoxStatus(String code) {
            this.code = code;
        }

    }

    /**
     * 발송처리상태 : ENS003
     */
    @Getter
    public enum SndngProcessStatus {
        ACCEPT("accept"),
        ACCEPT_OK("accept-ok"),
        ACCEPT_FAIL("accept-fail"),
        MAKE_OK("make-ok"),
        MAKE_FAIL1("make-fail1"),
        MAKE_FAIL2("make-fail2"),
        MAKE_FAIL3("make-fail3"),
        SENDING1("sending1"),
        SENDING2("sending2"),
        SEND_OK("send-ok"),
        SEND_FAIL1("send-fail1"),
        SEND_FAIL2("send-fail2"),
        SEND_FAIL3("send-fail3"),
        CLOSE("close")

        ;

        private final String code;

        SndngProcessStatus(String code) {
            this.code = code;
        }

    }

    /**
     * 발송구분코드
     */
    @Getter
    public enum SndngSeCode {
        SMS("SMS", "SMS"),
        KAKAO("KKO-MY-DOC", "카카오"),
        E_GREEN("E-GREEN", "E그린"),
        KT_BC("KT-BC", "공공알림문자"),
        PPLUS("POST-PLUS", "Post Plus")
        ;

        private final String code;
        private final String desc;

        SndngSeCode(final String code, final String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static SndngSeCode compare(final String code){
            return Arrays.stream(SndngSeCode.values())
                .filter(ssc -> ssc.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> BizRuntimeException.create(String.format("미정의된 문서 중계자가[%s]", code)));
        }
    }

    /**
     * SignguCode
     */
    @Getter
    public enum SignguCode {
        /**
         * 교통시설운영처
         */
        TRAFFIC("88328"),
        /**
         * 승화원 : NICE CI는 교통시설운영처와 동일한 코드 사용
         */
        FUNERAL("88316"),
        ;

        private final String code;

        SignguCode(String code) {
            this.code = code;
        }

    }

    public enum NiceCiWrkDiv {
        TOKEN,
        PUBLIC_KEY,
        SYM_KEY,
        CI
    }

    public enum KtServiceCode {
        SISUL,
        CHUMO;

        public static KtServiceCode compare(final String en){
            return valueOf(en);
        }
    }
}
