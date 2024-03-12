package kr.xit.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <pre>
 * description : Audit column (등록 및 변경 정보 저장 필드)
 *
 * packageName : kr.xit.core.model
 * fileName    : AuditFields
 * author      : limju
 * date        : 2023-08-31
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-08-31    limju       최초 생성
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuditFields {
   /**
     * 등록 일시
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime registDt;
    /**
     * 등록자
     */
    private String register;
    /**
     * 수정 일시
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime updtDt;
    /**
     * 수정자
     */
    private String updusr;
}
