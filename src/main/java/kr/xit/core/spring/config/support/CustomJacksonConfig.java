package kr.xit.core.spring.config.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.xit.core.spring.util.CoreSpringUtils;
import kr.xit.core.support.utils.ConvertHelper;
import kr.xit.core.support.utils.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <pre>
 * description : Jackson(ObjectMapper) 설정 재정의
 *               모든 ObjectMapper 사용시 이 설정이 적용되도록 해야 한다.
 *               - Cannot coerce empty String("") to ...
 *                 value(but could if coercion was enabled using `CoercionConfig`) 에러 방어
 * packageName : kr.xit.core.spring.config.support
 * fileName    : CustomJacksonConfig
 * author      : julim
 * date        : 2023-09-13
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-13    julim       최초 생성
 *
 * </pre>
 * @see CoreSpringUtils#getObjectMapper()
 * @see JsonUtils
 * @see ConvertHelper
 * @see WebClientConfig
 */
@Configuration
public class CustomJacksonConfig {

    /**
     * <pre>
     * 기본 설정에 override
     * Cannot coerce empty String("") to ...
     * value(but could if coercion was enabled using `CoercionConfig`) 에러 방어
     * </pre>
     *
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            //.setSerializationInclusion(Include.NON_NULL)
            //.setSerializationInclusion(Include.NON_EMPTY)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)
            .registerModule(new JavaTimeModule());

        om.coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
//        om.coercionConfigFor(LogicalType.Enum)
//            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
//        om.coercionConfigFor(LogicalType.POJO)
//            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        return om;
    }
}
