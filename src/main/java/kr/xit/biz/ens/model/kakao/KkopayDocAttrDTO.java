package kr.xit.biz.ens.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import kr.xit.biz.common.ApiConstants;
import kr.xit.biz.ens.model.cmm.CmmEnsRequestDTO;
import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description : 카카오페이 전자문서 DTO
 *
 * packageName : kr.xit.biz.ens.model.kakao
 * fileName    : KkopayDocAttrDTO
 * author      : limju
 * date        : 2023-05-03
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-03    limju       최초 생성
 *
 * </pre>
 */
public class KkopayDocAttrDTO {

    //------------------- requestSend ------------------------------------------------------------------------------------------------
    @Schema(name = "Send", description = "RequestSend(문서발송 요청 파라메터)의 요청 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Send implements IApiResponse {
        /**
         * 발송할 문서의 제목 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "발송할 문서의 제목", example = "문서 제목")
        @NotEmpty(message = "문서제목은 필수입니다(max:40)")
        @Size(max = 40)
        private String title;

        /**
         * 처리마감시간(절대시간) - 카카오톡 메시지를 수신한 사용자가 전자문서를 열람을 할 수 있는 시간
         * read_expired_sec 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "처리마감시간(절대시간)", example = "1617202800")
        @Digits(integer = 10, fraction = 0, message = "처리마감시간(절대시간:max=10자리)")
        private Long read_expired_at;

        /**
         * 처리마감시간(상대시간) - 권장값: 30일 (2592000 sec)
         * read_expired_at 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "처리마감시간(상대시간)", example = " ")
        @Digits(integer = 8, fraction = 0, message = "처리마감시간(절대시간:max=8자리)")
        private Integer read_expired_sec;

        /**
         * 문서 원문(열람정보)에 대한 hash 값 - 공인전자문서 유통정보 등록 시 필수
         */
        @Schema(title = "문서 원문(열람정보)에 대한 hash 값", example = "6EFE827AC88914DE471C621AE")
        @NotEmpty(message = "문서 원문(열람정보)에 대한 hash 값(max=99)")
        @Size(max = 99)
        private String hash;

        /**
         * 문서의 메타정보 - 향후 문서 검색을 위해서 활용될 메타 정보(현재 미 제공)
         */
        @Schema(title = "문서의 메타정보(현재 미 제공)", example = "[\"NOTICE\"]")
        private List<ApiConstants.Categories> common_categories;

        /**
         * 받는이에 대한 정보 - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private Receiver receiver;

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private Property property;
    }

    @Schema(name = "Receiver", description = "RequestSend(문서발송 요청 파라메터)의 receiver(받는이)에 대한 정보 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Receiver {
        /**
         * 받는이 CI
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는이 CI", example = " ")
        @Size(max = 88, message = "받는이 CI(max=88)")
        private String ci;

        /**
         * 받는이 전화번호
         * ci 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는이 전화번호", example = "01012345678")
        @Size(max = 11, message = "받는이 전화번호(max=11)")
        private String phone_number;

        /**
         * 받는 이 이름
         * ci 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는이 이름", example = "김페이")
        @Size(max = 20, message = "받는이 이름(max=20)")
        private String name;

        /**
         * 받는 이 생년월일 (YYYYMMDD 형식)
         * ci 미전송시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "받는이 생년월일 (YYYYMMDD 형식)", example = "19801101")
        //@Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$")
        @Size(max = 8, message = "받는이 생년월일(YYYYMMDD:max=8)")
        private String birthday;

        /**
         * 성명 검증 옵션
         * CI 전송 시 생략 가능
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "성명 검증 옵션", example = "false")
        private Boolean is_required_verify_name;
    }

    @Schema(name = "Property", description = "RequestSend(문서발송 요청 파라메터)의 property(문서속성)에 대한 정보 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Property {
        /**
         * 본인인증 후 사용자에게 보여줄 웹페이지 주소 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "본인인증 후 사용자에게 보여줄 웹페이지 주소", example = "http://ipAddress/api/kakaopay/v1/ott")
        @Size(min = 10, max = 100, message = "본인인증후 사용자에게 보여줄 페이지 주소는 필수입니다(max=100)")
        private String link;

        /**
         * 고객센터 전화번호 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "고객센터 전화번호", example = "02-123-4567")
        @Size(min = 10, max = 20, message = "고객센터 전화번호는 필수입니다(max=20)")
        private String cs_number;

        /**
         * 고객센터 전화번호 : 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "고객센터 명", example = "콜센터")
        @Size(min = 1, max = 10, message = "고객센터명은 필수입니다(max=10)")
        private String cs_name;

        /**
         * 이용기관에서 해당 값을 다시 받고자 할 내용의 값
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "이용기관에서 해당 값을 다시 받고자 할 내용의 값", example = "payload 파라미터 입니다.")
        @Size(max = 200, message = "이용기관에서 해당 값을 다시 받고자 할 내용의 값(max=200)")
        private String payload;

        /**
         * 메세지 - 사용자에게 전송하는 문서에 대한 설명
         * 노출위치 : 문서수신 메시지(알림톡)가 도착했음을 알리는 카카오톡 메시지 내부
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "사용자에게 전송하는 문서에 대한 설명", example = "해당 안내문은 다음과 같습니다.")
        @Size(max = 500, message = "메세지(max=500)")
        private String message;
    }
    //-------------------------------------------------------------------------------------------------------------------


    @Schema(name = "DocumentBinderUuid DTO", description = "카카오페이 문서식별번호")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode(callSuper = false)
    public static class DocumentBinderUuid extends CmmEnsRequestDTO implements IApiResponse {
        /**
         * 카카오페이 문서식별번호(max:40) - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "카카오페이 문서식별번호(max:40)", example = "BIN-ff806328863311ebb61432ac599d6150")
        @Size(min = 1, max = 40, message = "카카오페이 문서식별번호는 필수입니다(max:40)")
        private String document_binder_uuid;
    }

    @Schema(name = "DocStatus DTO", description = "카카오페이 전자문서 상태 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DocStatus implements IApiResponse {
        /**
         * <pre>
         * 진행상태(max:20) : 필수
         * 송신|수신|열람|만료
         * SENT|RECEIVED|READ|EXPIRED
         * SENT - 문서발송요청 성공(실 사용자에게 문서가 도달되지 않은 상태, 알림톡은 수신했으나 페이회원이 아니어서 실 문서 발송이 않됨)
         * RECEIVED - 사용자에 실문서 도달 완료
         * READ - OTT 검증 완료후 문서 상태 변경 API 호출에 성공한 상태
         * EXPIRED - 미열람 문서에 대한 열람 만료 시간이 지난 상태
         * </pre>
         * @see ApiConstants.DocBoxStatus
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 20, title = "진행상태(max:20)", example = " ")
        @Size(min = 1, max = 20, message = "진행상태는 필수입니다(max:20)")
        private ApiConstants.DocBoxStatus doc_box_status;

        /**
         * 송신 시간(long max:10)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "송신 시간(max:10)", example = " ")
        @Digits(integer = 10, fraction = 0, message = "송신시간(max:10)")
        private Long doc_box_sent_at;

        /**
         * 수신 시간(long max:10)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "수신 시간(max:10)", example = " ")
        @Digits(integer = 10, fraction = 0, message = "수신시간(max:10)")
        private Long doc_box_received_at;

        /**
         * 열람인증성공 최초 시간(long max:10)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "열람인증성공 최초 시간(max:10)", example = " ")
        @Digits(integer = 10, fraction = 0, message = "열람인증성공 최초시간(max:10)")
        private Long authenticated_at;

        /**
         * OTT 검증 성공 최초 시간(long max:10)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "OTT 검증 성공 최초 시간(max:10)", example = " ")
        @Digits(integer = 10, fraction = 0, message = "OTT 검증 성공 최초 시간(max:10)")
        private Long token_used_at;

        /**
         * 최초 열람 시간(long max:10)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "최초 열람 시간(max:10)", example = " ")
        @Digits(integer = 10, fraction = 0, message = "최초 열람 시간(max:10)")
        private Long doc_box_read_at;

        /**
         * 송신 시간(long max:10)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "알림톡 수신 시간(max:10)", example = " ")
        @Digits(integer = 10, fraction = 0, message = "알림톡 수신 시간(max:10)")
        private Long user_notified_at;

        /**
         * 이용기관 생성 payload(max:200)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "이용기관 생성 payload(max:200)", example = " ")
        @Size(max = 200, message = "이용기관 생성 payload(max:200)")
        private String payload;
    }
}
