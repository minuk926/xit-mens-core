package kr.xit.core.spring.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.xit.core.exception.FilterErrorUtils;
import kr.xit.core.spring.config.WebMvcConfig;
import kr.xit.core.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * <pre>
 * description : Logging filter
 * - excludedUrls: 필터 생성시 FilterRegistrationBean.setInitParameters()로 set한 list
 * packageName : kr.xit.core.spring.filter
 * fileName    : LoggingFilter
 * author      : limju
 * date        : 2023-06-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-06-01    limju       최초 생성
 *
 * </pre>
 * @see WebMvcConfig
 */

@Slf4j
public class LoggingFilter implements Filter {
    private List<String> excludedUrls;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
        IOException,
        ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            String path = httpRequest.getServletPath();

            if(DispatcherType.REQUEST.equals(request.getDispatcherType())
                && excludedUrls.stream().noneMatch(path::matches)){
                requestLog(httpRequest, getParams(httpRequest));
            }
            chain.doFilter(request, response);

        } catch (Exception e) {
            FilterErrorUtils.writeErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                (HttpServletResponse) response, e, new String[]{"dev"});
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludePattern = filterConfig.getInitParameter("excludedUrls");
        excludedUrls = Arrays.asList(excludePattern.split(","));
    }

    @Override
    public void destroy() {
        //Filter.super.destroy();
    }

    private void requestLog(HttpServletRequest request, JSONObject params) {

        if (log.isDebugEnabled()) {
            Map<String, Object> map = new LinkedHashMap<>();
            //sb.append("Ajax Call : " + "XMLHttpRequest".equals(request.getHeader(Globals.AJAX_HEADER))).append("\n");
            map.put("URI", request.getRequestURI());
            map.put("URL", request.getRequestURL());
            map.put("IP", request.getRemoteAddr());
            map.put("Referer URI", request.getHeader("referer"));
            map.put("Method", request.getMethod());
            map.put("User Agent", request.getHeader("User-Agent"));
            map.put("Session", request.getSession().getId());
            map.put("Locale", request.getLocale().getCountry());
            map.put("ContentType", request.getContentType());
            map.put("Parameters", params);

            log.info("{}{}{}",
                "\n//============================= Http Request ==============================",
                LogUtils.toString(map),
                "\n=========================================================================//"
            );
            map.clear();
        }
    }

    private JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }
}
