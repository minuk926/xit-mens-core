package kr.xit.core.exception;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.model.ApiResponseDTO;
import kr.xit.core.support.utils.Checks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.core.exception
 * fileName    : FilterErrorUtils
 * author      : limju
 * date        : 2023-12-05
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-12-05    limju       최초 생성
 *
 * </pre>
 */
@Slf4j
public class FilterErrorUtils {
    public static void writeErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex, String[] profiles){
        response.setStatus(status.value());
        response.setContentType("application/json");
        ApiResponseDTO<?> errorResponse = ApiResponseDTO.error(ErrorCode.INTERNAL_SERVER_ERROR);

        // 운영 환경인 경우는 상세 정보 미출력
        if(Arrays.asList(profiles).contains("prod"))
            errorResponse.setMessage(Checks.isNotEmpty(ex.getCause())? ex.getCause().getMessage() : ex.getMessage());
        else
            errorResponse.setMessage(Checks.isNotEmpty(ex.getCause())? ex.getCause().getMessage() : ex.getMessage());
        try{
            String json = errorResponse.convertToJson();
            log.error(json);
            response.getWriter().write(json);
        }catch (IOException e){
            log.error("FilterErrorUtils::setErrorResponse", e);
        }

    }
}
