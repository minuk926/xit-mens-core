package kr.xit.biz.ens.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;

import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.biz.ens.model.kakao
 * fileName    : KkopayErrorDTO
 * author      : limju
 * date        : 2023-06-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-06-01    limju       최초 생성
 *
 * </pre>
 */
@Schema(name = "KkopayErrorDTO", description = "에러 DTO")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KkopayErrorDTO implements IApiResponse {
    /**
     * 에러 코드(max:40)
     */
    @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 코드(max:40)", example = " ")
    @Size(max = 40)
    @JsonProperty("error_code")
    private String errorCode;

    /**
     * 에러 메세지(max:500)
     */
    @Schema(requiredMode = Schema.RequiredMode.AUTO, title = "에러 메세지(max:500)", example = " ")
    @Size(max = 500)
    @JsonProperty("error_message")
    private String errorMessage;
}
