package kr.xit.core.spring.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.xit.core.consts.Constants;
import kr.xit.core.spring.auth.interceptor.AuthentificationInterceptor;
import kr.xit.core.spring.config.properties.CorsProperties;
import kr.xit.core.spring.filter.LoggingFilter;
import kr.xit.core.spring.filter.ReadableRequestWrapperFilter;

/**
 * <pre>
 * description : Spring MVC 설정
 *               - filter, interceptor
 *               - {@link AuthentificationInterceptor} : 인증처리
 *               - {@link CommonsRequestLoggingFilter} : request logging
 *               - {@link ReadableRequestWrapperFilter} : post logging 처리를 위한 필터
 *               - {@link LoggingFilter} : 실행 log 처리를 위한 필터
 *               - CORS 설정
 * {@code @EnabledWebMVC} 사용 - 스프링 부트의 웹 MVC 기본 설정을 사용하지 않는다
 * -> {@code InternalResourceViewResolver}등을 직접 등록해야
 * -> 스프링 부트가 제공하는 웹 MVC 관련 자동 설정을 유지하면서 커스터마이징하려면
 *    {@code @EnableWebMVC}없이 {@code @Configuration} + implements {@code WebMvcConfigurer}만 사용
 *    {@code WebMvcConfigurer}는 SpringBoot의 자동 설정({@code WebMvcAutoConfiguration})을 유지하면서
 *    기능을 쉽게 확장할 수 있게 해준다
 *
 * packageName : kr.xit.core.spring.config
 * fileName    : WebMvcConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see AuthentificationInterceptor
 * @see CommonsRequestLoggingFilter
 * @see ReadableRequestWrapperFilter
 * @see LoggingFilter
 */
//@Slf4j
//@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * logging exclude path
     */
    @Value("${app.log.request.exclude-patterns}")
    private List<String> EXCLUDE_URL_REGEXS;

    private final CorsProperties corsProperties;
    private final Environment env;

	public WebMvcConfig(CorsProperties corsProperties, Environment env) {
		this.corsProperties = corsProperties;
		this.env = env;
	}

	/**
     * MappingJackson2XmlHttpMessageConverter 순서를 가장 후순위로 조정
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //converters.forEach(System.out::println);
        //System.out.println("--------------------------------------------");;
        reorderXmlConvertersToEnd(converters);
        //converters.forEach(System.out::println);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthentificationInterceptor())
            .addPathPatterns("/**/*")
            .excludePathPatterns(
                "/api/core/*",
                "/swagger-ui.html",
                "/swagger-ui/*",
                "/api-docs/*"
            );
        //registry.addInterceptor(localeChangeInterceptor());
    }

    // -------------------------------------------------------------
    // RequestMappingHandlerMapping 설정 View Controller 추가
    // -------------------------------------------------------------
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/cmmn/validator.do")
            .setViewName("cmmn/validator");
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOriginPatterns("*")
            .allowedOrigins(corsProperties.allowedOrigins())
            .allowedMethods(corsProperties.allowedMethods())
            .allowedHeaders(corsProperties.allowedHeaders())
            .allowCredentials(corsProperties.allowCredentials())
            .maxAge(corsProperties.maxAge())
            .exposedHeaders(corsProperties.exposeHeader());
    }

//    @Bean
//    protected CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOriginPatterns(List.of("*"));
//        configuration.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods()));
//        configuration.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins()));
//        configuration.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders()));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    /**
     * Post 요청시 request(stream) logging 처리를 위한 필터
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public FilterRegistrationBean readableRequestWrapperFilter() {
        ReadableRequestWrapperFilter readableRequestWrapperFilter = new ReadableRequestWrapperFilter();

        //noinspection unchecked
        FilterRegistrationBean bean = new FilterRegistrationBean(readableRequestWrapperFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns(Constants.API_URL_PATTERNS);
        return bean;
    }

    /**
     * Filter Exception 처리
     * @return
     */
//    @Bean
//    public FilterRegistrationBean exceptionHandlerFilter() {
//        ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter(env);
//
//        //noinspection unchecked
//        FilterRegistrationBean bean = new FilterRegistrationBean(exceptionHandlerFilter);
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        bean.addUrlPatterns("/*");
//        return bean;
//    }

    // -------------------------------------------------------------
    // Locale
    // -------------------------------------------------------------

    /**
     * 세션에 지역설정. default는 KOREAN = 'ko'
     * @return
     */
//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.ENGLISH);
//        //slr.setDefaultLocale(Locale.KOREAN);
//        return slr;
//    }

    /**
     * 지역설정 인터셉터.
     * 요청시 파라미터에 lang 정보를 지정하면 언어가 변경.
     * @return
     */
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        lci.setParamName("lang");
//        return lci;
//    }
    // -------------------------------------------------------------

    // -------------------------------------------------------------
    // Log
    // -------------------------------------------------------------
    /**
     * CommonsRequestLoggingFiler 등록
     * app.param.log.enabled: true시 로그 출력
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ConditionalOnProperty(value = "app.log.request.common-enabled", havingValue = "true", matchIfMissing = false)
    @Bean
    public FilterRegistrationBean requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter(){
            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
                String path = request.getServletPath();
                return EXCLUDE_URL_REGEXS.stream().anyMatch(regex -> path.matches(regex));
            }
        };
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeHeaders(false);
        loggingFilter.setBeforeMessagePrefix("\n//========================== Request(Before) ================================\n");
        loggingFilter.setBeforeMessageSuffix("\n//===========================================================================");

        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(1024* 1024);
        loggingFilter.setAfterMessagePrefix("\n//=========================== Request(After) ================================\n");
        loggingFilter.setAfterMessageSuffix("\n//===========================================================================");

        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(loggingFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.addUrlPatterns(Constants.API_URL_PATTERNS);
        return bean;
    }

    /**
     * <pre>
     * Logging Filter 지정
     * - FilterRegistrationBean.setInitParameters()로 excludedUrls set
     * @return FilterRegistrationBean
     * </pre>
     * @see LoggingFilter
     */
    @SuppressWarnings("rawtypes")
    @ConditionalOnProperty(value = "app.log.request.filter-enabled", havingValue = "true", matchIfMissing = false)
    @Bean
    public FilterRegistrationBean loggingFilter() {
        Map<String, String> initMap = new HashMap<>();
        initMap.put("excludedUrls", StringUtils.join(EXCLUDE_URL_REGEXS,","));

        FilterRegistrationBean<Filter> frb = new FilterRegistrationBean<>(new LoggingFilter());
        frb.setOrder(1);
        frb.addUrlPatterns(Constants.API_URL_PATTERNS);
        frb.setInitParameters(initMap);
        frb.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC);

        return frb;
    }
    // -------------------------------------------------------------

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.jsp("/WEB-INF/jsp/",".jsp");
//    }


    //TODO :: ArgumentResolver add
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

//        resolvers.add(new CustomArgumentResolver());
//        resolvers.add(new PageableArgumentResolver());
    }

    /**
     * MappingJackson2XmlHttpMessageConverter 를 가장 후순위로 조정하는 메소드
     * @param converters
     */
    private void reorderXmlConvertersToEnd(List<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> xml = new ArrayList<>();
        for (Iterator<HttpMessageConverter<?>> iterator =
            converters.iterator(); iterator.hasNext();) {
            HttpMessageConverter<?> converter = iterator.next();
            if ((converter instanceof AbstractXmlHttpMessageConverter)
                || (converter instanceof MappingJackson2XmlHttpMessageConverter)) {
                xml.add(converter);
                iterator.remove();
            }
        }
        converters.addAll(xml);
    }

   	@Bean
	public CommonsMultipartResolver springRegularCommonsMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(1000000000);
		commonsMultipartResolver.setMaxInMemorySize(1000000000);
		return commonsMultipartResolver;
	}

    private String[] toArray(String commaStr){
        return Arrays.stream(commaStr.split(","))
            .map(s -> s.trim())
            .toArray(size -> new String[size]);
    }
//    /**
//     * HandlerExceptionResolver 를 상속받은 resolver 등록
//     * @param resolvers the list of configured resolvers to extend
//     */
//     @Override
//     public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//         HandlerExceptionResolver exceptionHandlerExceptionResolver = resolvers.stream().filter(x -> x instanceof ExceptionHandlerExceptionResolver).findAny().get();
//         int index = resolvers.indexOf(exceptionHandlerExceptionResolver);
//         resolvers.add(index, new CustomRuntimeResolver());
//         WebMvcConfigurer.super.extendHandlerExceptionResolvers(resolvers);
//     }

}
