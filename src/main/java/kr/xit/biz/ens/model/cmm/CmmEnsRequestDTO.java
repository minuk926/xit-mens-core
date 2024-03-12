package kr.xit.biz.ens.model.cmm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description : 전자고지 문서중계자 Request 공통 DTO
 *
 * packageName : kr.xit.biz.ens.model.kt
 * fileName    : CmmEnsRequestDTO
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
@JsonInclude(Include.NON_NULL)
public class CmmEnsRequestDTO {

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

    /**
     * 1차 발송
     */
    @Schema(hidden = true, requiredMode = RequiredMode.AUTO, title = "1차 발송", example = "KKO-MY-DOC")
    @JsonProperty("try1")
    private String try1;
}
