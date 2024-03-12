package kr.xit.biz.ens.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import kr.xit.biz.ens.model.cmm.CmmEnsRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.biz.ens.model.kakao
 * fileName    : KkopayDocBulkDTO
 * author      : limju
 * date        : 2023-05-12
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-12    limju       최초 생성
 *
 * </pre>
 */
public class KkopayDocBulkDTO extends KkopayDocAttrDTO {

    //------------ BulkSendRequests -----------------------------------------------------------------------------
    @Schema(name = "BulkSendRequests DTO", description = "문서발송(bulk) 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class BulkSendRequests extends CmmEnsRequestDTO {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private List<BulkSendReq> documents;
    }

    @Schema(name = "BulkSendReq", description = "문서발송(bulk) 요청 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class BulkSendReq extends Send {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private PropertyBulk property;
    }

    @Schema(name = "PropertyBulk", description = "SendRequestBulk(문서발송 요청 파라메터)의 property(문서속성)에 대한 정보 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class PropertyBulk extends Property {
        /**
         * 문서 아이디(외부)(max=40) - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "외부 문서 아이디(max=40)", example = "A000001")
        @Size(min = 1, max = 40, message = "문서 아이디(외부)는 필수 입니다(max=40)")
        private String external_document_uuid;
    }
    //----------------------------------------------------------------------------------

    //------------------- BulkSendResponses ---------------------------------------------------------------
    @Schema(name = "BulkSendResponses DTO", description = "문서발송(bulk) 응답 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BulkSendResponses {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private List<BulkSendRes> documents;
    }

    @Schema(name = "BulkSendRes DTO", description = "문서발송(bulk) 요청 결과 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BulkSendRes {
        /**
         * 문서 아이디(외부)(max=40) - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "외부 문서 아이디(max=40)", example = " ")
        @NotEmpty
        @Size(min = 1, max = 40)
        private String external_document_uuid;

        /**
         * 카카오페이 문서식별 번호(max:40)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "카카오페이 문서식별 번호(max:40)", example = " ")
        @Size(max = 40)
        private String document_binder_uuid;

        /**
         * 에러 코드(max:40)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 코드(max:40)", example = " ")
        @Size(max = 40)
        private String error_code;

        /**
         * 에러 메세지(max:500)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 메세지(max:500)", example = " ")
        @Size(max = 500)
        private String error_message;
    }
    //---------------------------------------------------------------------------------------


    //----------------- BulkStatusRequests ----------------------------------------------------------------------
    @Schema(name = "BulkStatusRequests DTO", description = "문서상태(bulk) 조회 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    public static class BulkStatusRequests extends CmmEnsRequestDTO {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private List<String> document_binder_uuids;
    }
    //--------------------------------------------------------------------------------------

    //-------------- BulkStatusResponses ------------------------------------------------------------------------
    @Schema(name = "BulkStatusResponses DTO", description = "문서상태(bulk) 조회 응답 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BulkStatusResponses {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        private List<BulkStatus> documents;
    }

    @Schema(name = "BulkStatus DTO", description = "BulkStatusReponse()의 문서상태(bulk)에 대한 정보 DTO")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BulkStatus {
        /**
         * 카카오페이 문서식별 번호(max:40) - 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, title = "카카오페이 문서식별번호(max:40)", example = " ")
        @Size(min = 1, max = 40, message = "카카오페이 문서식별번호는 필수입니다(max:40)")
        private String document_binder_uuid;

        /**
         * 에러 코드(max:40)
         * @see kr.xit.biz.common.ApiConstants.Error
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 코드(max:40)", example = " ")
        @Size(max = 40, message = "에러 코드(max:40)")
        private String error_code;

        /**
         * 에러 메세지(max:500)
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 메세지(max:500)", example = " ")
        @Size(max = 500, message = "에러 메세지(max:500)")
        private String error_message;

        /**
         * 문서 상태 - 성공시 필수
         */
        @Schema(requiredMode = Schema.RequiredMode.AUTO)
        @Valid
        private DocStatus status_data;
    }
    //-----------------------------------------------------------------------------------------------
}
