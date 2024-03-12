package kr.xit.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.core.model
 * fileName    : FileDTO
 * author      : limju
 * date        : 2023-12-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-12-01    limju       최초 생성
 *
 * </pre>
 */
public class FileDTO {

    /**
     * <pre>
     *     TODO:: 구조 정의 필요
     * </pre>
     */
    @Schema(name = "FileRequest", description = "File upload request parameter DTO")
    @Getter
    @AllArgsConstructor
    @SuperBuilder
    public static class FileRequest {
        @Schema(requiredMode = RequiredMode.AUTO, title = "파일ID", example = " ")
        private String atchFileId;

        @Schema(requiredMode = RequiredMode.AUTO, title = "파일Sn", example = " ")
        private String fileSn;

        @Schema(requiredMode = RequiredMode.REQUIRED, title = "파일", example = " ")
        private MultipartFile[] files;
    }

    @Schema(name = "FileResponse", description = "File upload response DTO")
    @Getter
    @AllArgsConstructor
    @SuperBuilder
    public static class FileResponse {
        private String atchFileId;
        private String orignlFileNm;
        private String fileStreCours;
        private long fileSize;
    }

    @Schema(name = "FileInfo", description = "File 정보 DTO")
    @Getter
    @AllArgsConstructor
    @SuperBuilder
    public static class FileInfo extends AuditFields{
        private String atchFileId;
        private String fileSn;
        private String fileStreCours;
        private String streFileNm;
        private String orignlFileNm;
        private String fileExtsn;
        private String fileCn;
        private long fileSize;
    }
}
