package kr.xit.core.exception;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.concurrent.ExecutionException;
import kr.xit.core.model.ApiResponseDTO;
import kr.xit.core.support.utils.Checks;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientRequestException;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.core.exception
 * fileName    : ErrorParse
 * author      : limju
 * date        : 2023-06-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-06-01    limju       최초 생성
 *
 * </pre>
 */
public class ErrorParse {

    @SuppressWarnings("rawtypes")
    public static ApiResponseDTO extractError(final Throwable e){
        String errCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String message = Checks.isNotNull(e) ? e.getLocalizedMessage() : StringUtils.EMPTY;
        HttpStatus httpStatus = null;

        if(e instanceof BizRuntimeException be) {
            return ApiResponseDTO.error(be.getCode(), be.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if(e instanceof ClientErrorException ce) {
            return ApiResponseDTO.error(String.valueOf(ce.getStatus()), ce.getBody(), ce.getStatus());
        }

        if(e instanceof ServerErrorException ce) {
            return ApiResponseDTO.error(String.valueOf(ce.getStatus()), ce.getBody(), ce.getStatus());
        }

        // Async(React) Exception 처리
        if(e instanceof WebClientRequestException) {
            if (e.getCause() instanceof ConnectTimeoutException || e.getCause() instanceof ReadTimeoutException) {
                return getTimeoutException();
            }
        }

        if(e instanceof ExecutionException) {
            if (e.getCause() instanceof WebClientRequestException) {
                message = e.getCause().getMessage();

                // Timeout 에러
                if (e.getCause().getCause() instanceof ReadTimeoutException || e.getCause().getCause() instanceof ConnectTimeoutException) {
                    return getTimeoutException();
                }
            }
        }

        if(e instanceof ReadTimeoutException){
            return getTimeoutException();
        }

        // Async(React) Exception 처리
        if(e.getCause() instanceof WebClientRequestException){

            // Timeout 에러
            if(e.getCause().getCause() instanceof ReadTimeoutException){
                return getTimeoutException();
            }

        }

        if(Checks.isNotEmpty(e.getCause())) {
            message = e.getCause().getMessage();
        }

        return ApiResponseDTO.error(errCode, message, httpStatus);
    }

    @SuppressWarnings("rawtypes")
    private static  ApiResponseDTO getTimeoutException(){
        return ApiResponseDTO.error(
                String.valueOf(HttpStatus.REQUEST_TIMEOUT.value()),
                HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(),
                HttpStatus.REQUEST_TIMEOUT);
    }
}
