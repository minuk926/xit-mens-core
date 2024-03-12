package kr.xit.core.spring.auth.util;

import javax.servlet.http.HttpServletRequest;
import kr.xit.core.consts.Constants;
import kr.xit.core.consts.ErrorCode;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.spring.util.CoreSpringUtils;
import kr.xit.core.support.utils.Checks;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <pre>
 * description : Header token utility
 * packageName : kr.xit.core.spring.config.auth.util
 * fileName    : HeaderUtil
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderUtil {

    public static String getAccessToken(){
        return getAccessToken(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
    }

    public static String getAccessToken(HttpServletRequest request) {
        return extractAccessToken(request.getHeader(Constants.JwtToken.HEADER_NAME.getCode()));
    }

    /**
     * get access token
     * @param tokenStr String
     * @return String
     */
    public static String extractAccessToken(String tokenStr) {
        if (tokenStr == null) {
            return null;
        }

        if (tokenStr.startsWith(Constants.JwtToken.GRANT_TYPE.getCode())) {
            return tokenStr.substring(Constants.JwtToken.GRANT_TYPE.getCode().length() + 1);
        }

        return null;
    }

    /**
     *
     * @param request
     * @return String userId
     */
    public static String getUserId(HttpServletRequest request){
        return getUserIdFromToken(getAccessToken(request));
    }


    public static String getUserId(){
        return getUserIdFromToken(getAccessToken(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
    }

    public static String getUserName(){
        return getUserNameFromToken(getAccessToken(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
    }

    private static String getUserIdFromToken(String accessToken){
        if(Checks.isEmpty(accessToken))     throw BizRuntimeException.create(ErrorCode.AUTH_HEADER_NOT_EXISTS);

        try {
            String username = CoreSpringUtils.getJwtTokenProvider().getUsernameFromToken(accessToken);
            return username;
        }catch (Exception e){
            throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
        }
    }

    private static String getUserNameFromToken(String accessToken){
        if(Checks.isEmpty(accessToken))     throw BizRuntimeException.create(ErrorCode.AUTH_HEADER_NOT_EXISTS);

        try {
            return CoreSpringUtils.getJwtTokenProvider().getUsernameFromToken(accessToken);
        }catch (Exception e){
            throw BizRuntimeException.create(ErrorCode.INVALID_TOKEN);
        }
    }
}
