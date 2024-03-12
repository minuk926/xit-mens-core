package kr.xit.core.spring.resolver;

import kr.xit.core.model.ClientInfoDTO;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <pre>
 * description : Client 헤더값에서 ClientInfoDTO 객체 생성
 * HandlerMethodArgumentResolver 인터페이스를 구현하여 ClientInfoDTO 객체 생성
 * -> WebMvcConfigurer 인터페이스를 구현하여 Resolver 를 등록
 * packageName : kr.xit.core.spring.resolver
 * fileName    : ClientInfoArgumentResolver
 * author      : limju
 * date        : 2023-04-26
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-26    limju       최초 생성
 *
 * </pre>
 * @see ClientInfoDTO
 */
public class ClientInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_CHANNEL = "X-APP-CHANNEL";
    private static final String HEADER_CLIENT_IP = "X-FORWORD-FOR";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ClientInfoDTO.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        String channel = webRequest.getHeader(HEADER_CHANNEL);
        String clientAddress = webRequest.getHeader(HEADER_CLIENT_IP);
        return new ClientInfoDTO(channel, clientAddress);
    }
}
