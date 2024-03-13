package kr.xit.core.spring.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.xit.core.spring.config.properties.CorsProperties;
import kr.xit.core.spring.util.CoreSpringUtils;

/**
 * <pre>
 * description : Cors filter
 *               {@code @Override} {@link WebMvcConfigurer#addCorsMappings}
 * packageName : kr.xit.core.spring.filter
 * fileName    : SimpleCORSFilter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Deprecated
public class SimpleCORSFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		CorsProperties corsProperties = CoreSpringUtils.getCorsProperties();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origins", toArrayString(corsProperties.allowedOrigins()));
		httpResponse.setHeader("Access-Control-Allow-Methods", toArrayString(corsProperties.allowedMethods()));
		httpResponse.setHeader("Access-Control-Allow-Headers", toArrayString(corsProperties.allowedHeaders()));
		httpResponse.setHeader("Access-Control-Allow-Credentials", corsProperties.allowCredentials().toString());
		httpResponse.setHeader("Access-Control-Max-Age", corsProperties.maxAge().toString());
		httpResponse.setHeader("Access-Control-Expose-Headers", corsProperties.exposeHeader());
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		// Do nothing
	}

	@Override
	public void destroy() {
		// Do nothing
	}

	private String toArrayString(String[] arrStr){
		return Arrays.stream(arrStr)
			.map(s -> s.trim())
			.collect(Collectors.joining(","));
	}
}
