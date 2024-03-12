package kr.xit.core.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * description : 인증 정책 정의 annotation
 *               Secured(policy = SecurityPolicy.DEFAUTL|TOKEN|SESSION|COOKIE|NONE)
 * packageName : kr.xit.core.spring.annotation
 * fileName    : Secured
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see SecurityPolicy
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Secured {
	SecurityPolicy policy() default SecurityPolicy.DEFAULT;
}
