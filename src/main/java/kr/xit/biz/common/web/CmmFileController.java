package kr.xit.biz.common.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import kr.xit.core.model.ApiResponseDTO;
import kr.xit.core.model.FileDTO.FileInfo;
import kr.xit.core.model.FileDTO.FileRequest;
import kr.xit.core.model.FileDTO.FileResponse;
import kr.xit.core.model.IApiResponse;
import kr.xit.core.spring.util.CoreSpringUtils;
import kr.xit.core.support.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.biz.common.web
 * fileName    : CmmFileController
 * author      : limju
 * date        : 2023-12-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-12-01    limju       최초 생성
 *
 * </pre>
 */
@Tag(name = "CmmFileController", description = "파일 처리(공통)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/framework/biz/cmm")
public class CmmFileController {

//    @Value("${app.file.cmm.upload.root:}")
//    private String uploadPath;

    @Operation(summary = "파일 업로드 Form" , description = "파일 업로드 Form")
    @GetMapping(value = "/uploadFileForm")
    public ModelAndView uploadFileForm(){
        return new ModelAndView("/cmm/cmmFileUploadForm.html");
    }

    @Operation(summary = "파일 업로드" , description = "파일 업로드")
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public IApiResponse uploadFile(@ModelAttribute final FileRequest fileRequest){
        FileResponse fileResponse = CoreSpringUtils.getCmmFileService().uploadFile(fileRequest);
        return ApiResponseDTO.success(fileResponse);
    }

    @Operation(summary = "파일 다운로드" , description = "파일 다운로드")
    @GetMapping("/downloadFile/{atchFileId}/{fileSn}")
    public ResponseEntity<?> downloadFile(@PathVariable("atchFileId") final String atchFileId,  @PathVariable("fileSn") String fileSn){
        FileInfo fileInfo = CoreSpringUtils.getCmmFileService().findFileInfo(
            FileInfo.builder()
                .atchFileId(atchFileId)
                .fileSn(fileSn)
                .build()
        );
        Resource resource = FileUtil.getFileAsResource(fileInfo.getFileStreCours(), fileInfo.getAtchFileId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileInfo.getOrignlFileNm(), StandardCharsets.UTF_8) + "\"");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
