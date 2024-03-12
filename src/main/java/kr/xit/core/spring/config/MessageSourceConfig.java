package kr.xit.core.spring.config;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * <pre>
 * description : * Spring MessageSource 설정
 *
 * packageName : kr.xit.core.spring.config
 * fileName    : MessageSourceConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
@Configuration
public class MessageSourceConfig {

    @Value("${spring.messages.basename}")
    private String[] basenames;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource rrbms = new ReloadableResourceBundleMessageSource();
        rrbms.setBasenames(basenames);
        //EgovWildcardReloadableResourceBundleMessageSource rrbms = new EgovWildcardReloadableResourceBundleMessageSource();
        //rrbms.setEgovBasenames(basenames);
        rrbms.getBasenameSet().forEach(s -> log.info("messageSource getBasename = {}", s));

        rrbms.setCacheSeconds(60);
        // 메세지가 없으면 기본 메세지 반환
        rrbms.setUseCodeAsDefaultMessage(true);
        rrbms.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return rrbms;
    }

}
