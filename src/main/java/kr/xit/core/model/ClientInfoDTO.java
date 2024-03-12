package kr.xit.core.model;

import kr.xit.core.spring.resolver.ClientInfoArgumentResolver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <pre>
 * description : Client 헤더값 class
 * 헤더 값 set
 * -> 1. HandlerMethodArgumentResolver 인터페이스를 구현하여 ClientInfoDTO 객체 생성
 *    2. WebMvcConfigurer 인터페이스를 구현하여 Resolver 를 등록
 *    3. Controller 에서 헤더 값을 객체로 받는다.
 * packageName : kr.xit.core.model
 * fileName    : ClientInfoDTO
 * author      : limju
 * date        : 2023-04-26
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-26    limju       최초 생성
 *
 * </pre>
 * @see ClientInfoArgumentResolver
 */
@Getter
@AllArgsConstructor
@ToString
@Builder
public class ClientInfoDTO {

    private final String channel;
    private final String clientAddress;
}
