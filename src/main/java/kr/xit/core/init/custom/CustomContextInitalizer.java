package kr.xit.core.init.custom;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <pre>
 * description : license 암호호 적용
 *               -> local 환경이 아닌 경우만 적용 하도록
 *
 * packageName : kr.xit.core.spring.custom
 * fileName    : CustomContextInitalizer
 * author      : limju
 * date        : 2023-07-21
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-21    limju       최초 생성
 *
 * </pre>
 */
@Slf4j
public class CustomContextInitalizer implements ApplicationContextInitializer {
    /**
     * Initialize.
     *
     * @param applicationContext the application context
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if(Arrays.asList(applicationContext.getEnvironment().getActiveProfiles()).contains("dev")) {
            try {
                new AppInitHelper(applicationContext).parseInit();
            } catch (Exception e) {
                log.error(e.getMessage());
                final String line = "====================================================================";
                log.error("{}\n{}\n {}", line, "     >>>>>>>>>> 인증 되지 않은 서버 입니다(서버 인증 필요) <<<<<<<<<<", line);
                System.exit(0);
            }
        }
    }
}
