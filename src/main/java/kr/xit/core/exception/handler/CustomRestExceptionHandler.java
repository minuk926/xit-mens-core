package kr.xit.core.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;
import javax.validation.ConstraintViolationException;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.exception.ErrorParse;
import kr.xit.core.model.ApiResponseDTO;
import kr.xit.core.support.utils.Checks;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <pre>
 * description : ExceptionHandlerFilter(Filter에서 발생한 에러 처리)와 함께 에러 처리
 *               ErrorCode 에서 해당 Exception 오류를 정의하여 사용
 *               - spring boot의 기본 properties
 *                server.error:
 *                  include-exception: false  # 응답에 exception의 내용을 포함할지 여부
 *                  include-stacktrace: never # 오류 응답에 stacktrace 내용을 포함할 지 여부
 *                  path: '/error'            # 오류 응답을 처리할 Handler의 경로
 *                  whitelabel.enabled: true  # 서버 오류 발생시 브라우저에 보여줄 기본 페이지 생성 여부
 * packageName : kr.xit.core.exception.handler
 * fileName    : CustomRestExceptionHandler
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see ResponseEntityExceptionHandler
 */
@SuppressWarnings("rawtypes")
@Slf4j
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * MethodArgumentNotValidException 에러 메세지 처리
     * Valid 체크 에러 메세지 처리를 위한 ResponseEntityExceptionHandler#handleMethodArgumentNotValid override
     *
     * Customize the response for MethodArgumentNotValidException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error("==== MethodArgumentNotValidException override ====\n{}", ex.getMessage());
        Map<String, String> validErrorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(e -> validErrorMap.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity
            .ok()
            .body(ApiResponseDTO.error(validErrorMap.toString()));
//        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }


    /**
     * Customize the response for HttpMessageNotReadableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error("==== HttpMessageNotReadableException override ====\n{}", ex.getMessage());

        return ResponseEntity
            .ok()
            .body(ApiResponseDTO.error(ex.getLocalizedMessage()));
        //return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    /**
     * BizRuntimeException
     *
     * @param e BizRuntimeException
     * @return ErrorApiResponse
     */
    @ExceptionHandler(value = {BizRuntimeException.class})
    protected ApiResponseDTO handleBizRutimeException(BizRuntimeException e) {
        log.error("==== throw BizRutimeException====\n{}", e.getMessage());
        return sendError(e);
    }

    /**
     * EgovBizException
     *
     * @param e EgovBizException
     * @return ErrorApiResponse
     */
    @ExceptionHandler(value = {EgovBizException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseDTO handleEgovBizException(EgovBizException e) {
        log.error("==== throw EgovBizException ====================\n{}", e.getMessage());
        return sendError(e);
    }

   /**
     * NoSuchElementException
     *
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseDTO handleNoSuchElementException(NoSuchElementException e) {
        log.error("==== throw NoSuchElementException ====================\n{}", e.getMessage());
        return sendError(e);
    }

    /**
     * NoSuchElementException
     *
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseDTO handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("==== throw IllegalArgumentException ====================\n{}", e.getMessage());
        return sendError(e);
    }

    /**
     * Data 중복
     *
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseDTO handleDataException(ConstraintViolationException e) {
        log.error("==== throw ConstraintViolationException ====================\n{}", e.getMessage());
        return sendError(e);
        //return ApiResponseDTO.error(ErrorCode.SQL_DATA_RESOURCE_INVALID);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseDTO handleDataException(DataIntegrityViolationException e) {
        log.error("==== throw DataIntegrityViolationException ====================\n{}", e.getMessage());
        return sendError(e);
        //return ApiResponseDTO.error(ErrorCode.SQL_DATA_RESOURCE_INVALID);
    }

    /**
     * 비동기 호출 에러 : timeout
     * @return
     */
    @ExceptionHandler(value = {CompletionException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponseDTO handleCompletionException(CompletionException e) {
        log.error("==== throw CompletionException ====================\n{}", e.getMessage());
        return sendError(e);
    }

    /**
     * RuntimeException
     *
     * @param e RuntimeException
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponseDTO handleRuntimeException(RuntimeException e) {
        log.error("==== throw RuntimeException ====================\n{}", e.getMessage());
        return sendError(e);
    }

    /**
     * Exception
     *
     * @param e Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiResponseDTO handleException(Exception e) {
        log.error("==== throw Exception ====================\n{}", e.getMessage());
        return sendError(e);
    }


    private ApiResponseDTO sendError(Throwable e) {
        return ErrorParse.extractError(Checks.checkVal(e.getCause(), e));
    }
}
