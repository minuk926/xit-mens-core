package kr.xit.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.Checks;
import kr.xit.core.support.utils.ConvertHelper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * <pre>
 * description : Api 응답
 *              TODO :: 프로젝트별 json 결과에서 제외하려면 @JsonIgnore 사용
 * packageName : kr.xit.core.model
 * fileName    : ApiResponseDTO
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Schema(name = "ApiResponseDTO", description = "Restful API 결과")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonRootName("result")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> implements IApiResponse {
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    @Schema(example = "true", description = "에러인 경우 false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean success;

    @Schema(example = " ", description = "HttpStatus.OK", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @Schema(description = "결과 데이타, 오류시 null", example = " ")
    private T data;

    @Schema(description = "오류 발생시 오류 메세지", example = " ", requiredMode = Schema.RequiredMode.AUTO)
    @Setter
    private String message;

    @JsonIgnore
    @Schema(example = " ", description = "HttpStatus.OK", requiredMode = Schema.RequiredMode.AUTO)
    private HttpStatus httpStatus;

    @Schema(description = "API 실행 결과 데이타 수")
    private int count;

    @JsonIgnore
    @Schema(description = "페이징 정보 - 결과값이 Collection type인 경우 사용됨", example = " ")
    private PaginationInfo paginationInfo;

    /**
     * 비동기 정상 데이타 ApiResponseDTO<T> return
     *
     * @param future CompletableFuture<T>
     * @return ApiResponseDTO<T>
     */
    //@SuppressWarnings("unchecked")
    @SuppressWarnings("rawtypes")
    public static <T> ApiResponseDTO of(final CompletableFuture<T> future) {
        try {
            if(future.get() instanceof ApiResponseDTO<?>)  return (ApiResponseDTO)future.get();
            return new ApiResponseDTO<>(true, future.get(), String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.name(), HttpStatus.OK);

        } catch (InterruptedException ie){
            // thread pool에 에러 상태 전송
            Thread.currentThread().interrupt();
            throw BizRuntimeException.create(ie);

        } catch (ExecutionException ee) {
            throw BizRuntimeException.create(ee);
        }
    }

    /**
     * 정상 데이타 ApiResponseDTO<T> return
     * @return ApiResponseDTO<T>
     */
    public static <T> ApiResponseDTO<T> success() {
        return new ApiResponseDTO<>(true, null, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name(), HttpStatus.OK);
    }

    /**
     * 정상 데이타 ApiResponseDTO<T> return
     * @param data T
     * @return ApiResponseDTO<T>
     */
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, data, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name(), HttpStatus.OK);
    }


    /**
     * 정상 데이타 ApiResponseDTO<T> return
     * @param httpStatus HttpStatus
     * @return ApiResponseDTO<T>
     */
    public static <T> ApiResponseDTO<T> success(final HttpStatus httpStatus) {
        return new ApiResponseDTO<>(true, null, String.valueOf(httpStatus.value()), httpStatus.name(), httpStatus);
    }

    /**
     * 정상 데이타 ApiResponseDTO<T> return
     * @param data T 데이타
     * @param httpStatus HttpStatus
     * @return ApiResponseDTO<T>
     */
    public static <T> ApiResponseDTO<T> success(final T data, final HttpStatus httpStatus) {
        return new ApiResponseDTO<>(true, data, String.valueOf(httpStatus.value()), httpStatus.name(), httpStatus);
    }

    /**
     * 정상 데이타 ApiResponseDTO<T> return
     * @param data T 데이타
     * @param message String
     * @return ApiResponseDTO<T>
     */
    public static <T> ApiResponseDTO<T> success(final T data, final String message) {
        return new ApiResponseDTO<>(true, data, String.valueOf(HttpStatus.OK.value()), message, HttpStatus.OK);
    }

    /**
     * 정상 return - body empty
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> empty() {
        return new ApiResponseDTO<>(true, null, String.valueOf(HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT.name(), HttpStatus.OK);
    }

    /**
     * Error ApiResponseDTO return
     * Hibernate Validator에 의해 유효하지 않은 데이터로 인해 API 호출이 거부될때 반환
     * @param bindingResult BindingResult
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(final BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put( error.getObjectName(), error.getDefaultMessage());
            }
        }
        return new ApiResponseDTO<>(false, null, FAIL_STATUS, errors.toString(), null);
    }

    /**
     * Error ApiResponseDTO return
     * @param message 에러 메세지
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(final String message) {
        return new ApiResponseDTO<>(false, null, ERROR_STATUS, message, null);
    }

    /**
     * Error ApiResponseDTO return
     * @param errorCode ErrorCode
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(final ErrorCode errorCode) {
        return new ApiResponseDTO<>(false, null, errorCode.name(), errorCode.getMessage(), errorCode.getHttpStatus());
    }

    /**
     * Error ApiResponseDTO return
     * @param e BizRuntimeException
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(final BizRuntimeException e) {

        if (Checks.isNotEmpty(e.getErrorCode())) {
            return ApiResponseDTO.error(e.getErrorCode());
        }
        return new ApiResponseDTO<>(
            false,
            null,
            org.apache.commons.lang3.StringUtils.defaultString(e.getCode(), org.apache.commons.lang3.StringUtils.EMPTY),
            org.apache.commons.lang3.StringUtils.defaultString(e.getMessage(), org.apache.commons.lang3.StringUtils.EMPTY),
            e.getHttpStatus());
    }

    /**
     * Error ApiResponseDTO return
     * @param code 에러코드
     * @param message 에러메세지
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(final String code, final String message) {
        return new ApiResponseDTO<>(false, null, code, message, null);
    }

    /**
     * Error ApiResponseDTO return
     * @param code 에러코드
     * @param message 에러메세지
     * @param httpStatus HttpStatus
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(final String code, final String message, HttpStatus httpStatus) {
        return new ApiResponseDTO<>(false, null, code, message, httpStatus);
    }

    /**
     *
     * @param success true|false
     * @param data data
     * @param code 에러코드
     * @param message 메세지(에러 발생시 필수)
     * @param httpStatus HttpStatus
     */
    private ApiResponseDTO(final boolean success, final T data, final String code, final String message, final HttpStatus httpStatus) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;

        if(data == null){
            this.count = 0;

        }else {

            if (Collection.class.isAssignableFrom(data.getClass())) {
                this.count = (((Collection<?>) data).size());

            } else {
                this.count =  1;
            }
        }

        if(httpStatus == null){
            if(!success)    this.httpStatus = HttpStatus.BAD_REQUEST;
            else            this.httpStatus = HttpStatus.OK;
        }
    }

    @Override
    public String toString() {
        // value가 null값인 경우도 생성
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        builder.disableHtmlEscaping();
        return builder.setPrettyPrinting().create().toJson(this);
    }

    public String convertToJson() {
        return ConvertHelper.jsonToObject(this);
    }
}
