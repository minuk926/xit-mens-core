package kr.xit.core.spring.config.properties;

import java.io.IOException;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * <pre>
 * description : yml(Properties) util
 *               static util class에서 yaml 파일 로드
 * packageName : kr.xit.core.spring.config.properties
 * fileName    : PropertiesUtils
 * author      : julim
 * date        : 2023-09-19
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-19    julim       최초 생성
 *
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesUtils {
    private static Properties properties;

    static {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new ClassPathResource("config/application-ens.yml"));
        properties = factory.getObject();

//        properties = new Properties();
//        URL url = new PropertiesUtils().getClass().getClassLoader()
//            //.getResource("config/application-ens1.properties");
//            .getResource("config/application-ens.yml");
//        try {
//            properties.load(new FileInputStream(url.getPath()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
