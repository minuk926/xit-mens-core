package kr.xit.biz.ens.model.kt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description : KT 공인 알림문자 공통 DTO
 *
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : KtCommonDTO
 * author      : limju
 * date        : 2023-09-22
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-22    limju       최초 생성
 *
 * </pre>
 */
public class KtCommonDTO {

    @Schema(name = "KtMnsRequest", description = "KT MMS 공통 파라메터 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KtMnsRequest {

        /**
         * 시군구 코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "시군구코드", example = "88328")
        @Size(min = 1, max = 10, message = "시군구 코드는 필수 입니다")
        @JsonProperty("signguCode")
        private String signguCode;

        /**
         * 과태료 코드
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "과태료코드", example = "11")
        @Size(min = 1, max = 2, message = "과태료 코드는 필수 입니다")
        @JsonProperty("ffnlgCode")
        private String ffnlgCode = "11";

        /**
         * active profile
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "profile", example = "local")
        @JsonProperty("profile")
        private String profile;
    }

    @Schema(name = "KtCommonResponse", description = "KT 응답 공통 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KtCommonResponse implements IApiResponse {

        /**
         * <pre>
         * 처리코드 : 2
         * 00 : 정상, 01: 에허
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리코드", example = " ")
        @Size(min = 2, max = 2)
        private String resultCd;

        /**
         * <pre>
         * 처리일시 : 14
         * Biz Center 응답 처리 일시(YYYYMMDDHHMISS)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리일시", example = " ")
        @Size(min = 14, max = 14)
        private String resultDt;

        /**
         * <pre>
         * 에러내용
         * 처리코드(resultCd) '01' 인 경우 필수
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO)
        @Valid
        private List<ErrorMsg> errors;
        public String toStringErrorMsg(){
            return this.errors.stream()
                .map(ErrorMsg::getErrorMsg)
                .collect(Collectors.joining(","));
        }
    }

    @Schema(name = "ErrorMsg", description = "KT 응답 공통 errors DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorMsg {

        /**
         * <pre>
         * 오류메세지
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED)
        @Size(max = 255)
        private String errorMsg;
    }
}
