package kr.xit.core.spring.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <pre>
 * description : Cache 설정
 * packageName : kr.xit.core.spring.config.cache
 * fileName    : CachingConfig
 * author      : limju
 * date        : 2023-09-06
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-06    limju       최초 생성
 *
 * </pre>
 * @see CacheType
 * @see SimpleKeyGenerator
 */
@Slf4j
@EnableCaching
@Configuration
public class CachingConfig {

    /**
     * <pre>
     * ✔ initialCapacity: 내부 해시 테이블의 최소한의 크기를 설정합니다.
     * ✔ maximumSize: 캐시에 포함할 수 있는 최대 엔트리 수를 지정합니다.
     * ✔ maximumWeight: 캐시에 포함할 수 있는 엔트리의 최대 무게를 지정합니다.
     * ✔ expireAfterAccess: 캐시가 생성된 후, 해당 값이 가장 최근에 대체되거나 마지막으로 읽은 후 특정 기간이 경과하면 캐시에서 자동으로 제거되도록 지정합니다.
     * ✔ expireAfterWrite: 항목이 생성된 후 또는 해당 값을 가장 최근에 바뀐 후 특정 기간이 지나면 각 항목이 캐시에서 자동으로 제거되도록 지정합니다.
     * ✔ refreshAfterWrite: 캐시가 생성되거나 마지막으로 업데이트된 후 지정된 시간 간격으로 캐시를 새로 고칩니다.
     * ✔ weakKeys: 키를 weak reference로 지정합니다. (GC에서 회수됨)
     * ✔ weakValues: Value를 weak reference로 지정합니다. (GC에서 회수됨)
     * ✔ softValues: Value를 soft reference로 지정합니다. (메모리가 가득 찼을 때 GC에서 회수됨)
     * ✔ recordStats: 캐시에 대한 Statics를 적용합니다.
     *
     * ⚠️ expireAfterWrite 와 expireAfterAccess 가 함께 지정된 경우, expireAfterWrite가 우선순위로 적용됩니다.
     * ⚠️ maximumSize와 maximumWeight는 함께 지정될 수 없습니다.
     * </pre>
     */
    @Bean
    public CacheManager cacheManager() {
        final List<CaffeineCache> caffeineCaches =  Arrays.stream(CacheType.values())
            .map(cache -> new CaffeineCache(
                cache.getCacheName(),
                Caffeine.newBuilder()
                    .recordStats()
                    .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS)
                    .maximumSize(cache.getMaximumSize())
                    .evictionListener((Object key, Object value, RemovalCause cause) ->
                        log.info("Key {} was evicted ({}): {}", key, cause, value))
                    .build()
            ))
            .toList();

        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }

    @Bean
    public KeyGenerator simpleKeyGenerator() {
        return new SimpleKeyGenerator();
    }
}
