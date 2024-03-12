package kr.xit.core.exception.handler;

import java.util.Map;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * <pre>
 * description :  Catch 되지 않은 에러인 경우 처리 되는 class
 *                개발자가 처리한 예외처리중 오류 및 framework에서 처리되지 않은 오류 발생시 반드시 처리 필요
 * packageName : kr.xit.core.exception.handler
 * fileName    : ReadableRequestWrapperFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see DefaultErrorAttributes
 */
@Slf4j
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> result = super.getErrorAttributes(webRequest, options);

        final Throwable error = super.getError(webRequest);
        if(error instanceof BizRuntimeException be){
            ErrorCode errorCode = be.getErrorCode();
            result.put("status", errorCode.getHttpStatus());
            result.put("code", errorCode.getHttpStatus().toString());
            result.put("message", errorCode.getMessage());
        }

        log.error("====================== Exception handler 에서 catch하지 못한 에러 :: CustomErrorAttributes 에서 처리 ==================================");
        log.error("========================== 처리되지 않은 에러 발생 ==================================");
        log.error("{}", result);
        log.error("========================== 반드시 처리해 주세요 =====================================");
        log.error("==================================================================================");

        return result;
    }
}
