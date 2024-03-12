package kr.xit.core.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * description : Method 단위 로깅 annotation(AOP 사용 예정)
 *               Around("@annotation(kr.xit.core.spring.annotation.TraceLogging)")
 * packageName : kr.xit.core.spring.annotation
 * fileName    : classAop
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
//@Target({ElementType.TYPE, ElementType.METHOD})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceLogging {
}
