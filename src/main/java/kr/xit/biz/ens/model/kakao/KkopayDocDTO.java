package kr.xit.biz.ens.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.cmm.CmmEnsRequestDTO;
import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : 카카오페이 전자문서 요청 DTO
 *
 * packageName : kr.xit.ens.model.kakao
 * fileName    : KkopayDocDTO
 * author      : limju
 * date        : 2023-05-03
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-03    limju       최초 생성
 *
 * </pre>
 */
public class KkopayDocDTO extends KkopayDocAttrDTO {

    //------------------ SendRequest ----------------------------------------------------------------------
    @Schema(name = "SendRequest DTO", description = "문서발송 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class SendRequest extends CmmEnsRequestDTO {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private RequestSend document;
    }

    @Schema(name = "RequestSend", description = "문서발송 요청 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class RequestSend extends KkopayDocAttrDTO.Send {
        /**
         * 문서 속성 정보 - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private Property property;
    }
    //-------------------------------------------------------------------------------------
    //------------------ SendResponse ----------------------------------------------------------------------
    @Schema(name = "SendResponse DTO", description = "문서발송 응답 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class SendResponse {
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "카카오페이 문서식별번호(max:40)", example = " ")
        @Size(min = 1, max = 40)
        private String document_binder_uuid;

        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 코드(max:40)", example = " ")
        @Size(max = 40, message = "에러 코드(max:40)")
        private String error_code;

        /**
         * 에러 메세지(max:500)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 메세지(max:500)", example = " ")
        @Size(max = 500, message = "에러 메세지(max:500)")
        private String error_message;
    }
    //-------------------------------------------------------------------------------------

    //------------------- ValidTokenRequest ------------------------------------------------------------------
    @Schema(name = "ValidTokenRequest DTO", description = "카카오페이 전자문서 토큰 유효성 검증 파라메터 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class ValidTokenRequest extends DocumentBinderUuid {
        /**
         * 카카오페이 전자문서 서버에서 생성한 토큰(max:50) : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "카카오페이 전자문서 서버로 부터 받은 토큰(max:50)", example = "CON-cc375944ae3d11ecb91e42193199ee3c")
        @NotEmpty(message = "카카오페이 전자문서 서버 토큰은 필수입니다(max:50)")
        @Length(max = 50)
        private String token;
    }
    //-----------------------------------------------------------------------------------------

    //----------------- ValidTokenResponse -----------------------------------------------------------------------
    @Schema(name = "ValidTokenResponse DTO", description = "카카오페이 전자문서 토큰 유효성 검증 결과 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidTokenResponse implements IApiResponse {
        /**
         * 토큰상태값(성공시 USED) : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "토큰상태값(성공시 USED)", example = " ")
        @Size(min = 1, max = 10, message = "토큰상태값은 필수입니다(성공:USED)")
        private String token_status;

        /**
         * 토큰 만료일시(max:20) : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "토큰 만료일시(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "토큰 만료일시는 필수입니다(max:20)")
        private Long token_expires_at;

        /**
         * 토큰 사용일시(long max:20)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "토큰 사용일시(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "토큰 사용일시는 필수입니다(max:20)")
        private Long token_used_at;

        /**
         * 송신시간(long max:20)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "송신시간(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "송신시간(max:20)")
        private Long doc_box_sent_at;

        /**
         * 수신시간(long max:20)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "수신시간(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "수신시간(max:20)")
        private Long doc_box_received_at;

        /**
         * 열람인증을 성공한 최초 시간(long max:20)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "열람인증을 성공한 최초 시간(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "열람인증 최초 시간(max:20)")
        private Long authenticated_at;

        /**
         * 토큰 사용일시(long max:20)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "알림톡 수신 시간(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "알림톡 수신 시간(max:20)")
        private Long user_notified_at;

        /**
         * 이용기관 생성 payload(max:200)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "이용기관 생성 payload(max:200)", example = " ")
        @Size(max = 200, message = "이용기관 생성 payload(max:200)")
        private String payload;

        /**
         * 토큰 서명일시(long max:20)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "토큰 서명일시(max:20)", example = " ")
        @Digits(integer = 20, fraction = 0, message = "토큰 서명일시(max:20)")
        private Long signed_at;

        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 코드(max:40)", example = " ")
        @Size(max = 40, message = "에러 코드(max:40)")
        private String error_code;

        /**
         * 에러 메세지(max:500)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 메세지(max:500)", example = " ")
        @Size(max = 500, message = "에러 메세지(max:500)")
        private String error_message;
    }
    //-----------------------------------------------------------------------------------------------------


    //----------------------- OneTimeToken -----------------------------------------------------------------
    @Schema(name = "OneTimeToken DTO", description = "카카오페이 OTT 요청 파라메터 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode(callSuper = true)
    public static class OneTimeToken extends ValidTokenRequest {
        /**
         * 문서 식별 번호(외부 - max:40)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "외부 문서 식별 번호(max:40)", example = " ")
        @Length(max = 40, message = "외부 문서 식별 번호(max:40)")
        private String external_document_uuid;
    }
    //-----------------------------------------------------------------------------------------------

    //----------------- DocStatusResponse ------------------------------------------------------------------------------
    @Schema(name = "DocStatusResponse DTO", description = "카카오페이 전자문서 상태 응답 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class DocStatusResponse extends DocStatus {
    }
}
