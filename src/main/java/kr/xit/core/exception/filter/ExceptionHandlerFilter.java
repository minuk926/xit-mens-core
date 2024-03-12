package kr.xit.core.exception.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.exception.FilterErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <pre>
 * description : CustomExceptionHandler 와 함께 에러 처리
 *               - Filter에서 발생한 오류 처리
 * packageName : kr.xit.core.spring.filter
 * fileName    : ExceptionHandlerFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see OncePerRequestFilter
 */
@Slf4j
//@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final Environment env;

    public ExceptionHandlerFilter(Environment env) {
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (BizRuntimeException ex){
            log.error("exception exception handler filter");
            FilterErrorUtils.writeErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, ex, env.getActiveProfiles());
        }catch (RuntimeException ex){
            log.error("runtime exception exception handler filter");
            FilterErrorUtils.writeErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, ex, env.getActiveProfiles());
        }
    }
}

