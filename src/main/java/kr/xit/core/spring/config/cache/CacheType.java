package kr.xit.core.spring.config.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

/**
 * <pre>
 * description : Cache type 정의
 *               -> cache method parmeter -> key로 Generator
 * packageName : kr.xit.core.spring.config.cache
 * fileName    : CacheKeyGenerator
 * author      : limju
 * date        : 2023-09-06
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-06    limju       최초 생성
 *
 * </pre>
 * @see SimpleKeyGenerator
 * @see CachingConfig
 */
@Getter
@AllArgsConstructor
public enum CacheType {

    /**
     * NICE CI 정보
     */
    NICE_CRTF("niceCiInfo", 60 * 60 * 24, 10),
    /**
     * KT / KAKAO 정보
     */
    RLAY_BSNM("rlaybsnmInfo", 60 * 60 * 24, 10),
    ;

    /**
     * 캐시 이름
     */
    private final String cacheName;

    /**
     * 만료시간
     */
    private final int expireAfterWrite;
    /**
     * 최대갯수
     */
    private final int maximumSize;
}
