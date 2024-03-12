package kr.xit.core.spring.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * description : properties 암호화 설정
 * packageName : kr.xit.core.spring.config
 * fileName    : JasyptConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
//FIXME :: properties 암호화시 사용
@Configuration
public class JasyptConfig {
    @Value("${app.jasypt.secretKey:none}")
    private String secretKey;

    @Value("${app.jasypt.alg:none}")
    private String secretAlg;

    @Value("${app.jasypt.type:none}")
    private String secretType;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 암/복호화 키
        config.setPassword(secretKey);
        // 암/복호화 알고리즘
        config.setAlgorithm(secretAlg);
        // 반복할 해싱 회수
        config.setKeyObtentionIterations("1000");
        config.setProviderName("SunJCE");
        // salt 생성 클래스
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        // 암/복호화 인스턴스 : 0보다 커야
        config.setPoolSize("1");
        config.setStringOutputType(secretType);

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }

/*
    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 암/복호화 키
        config.setPassword(secretKey);
        // 암/복호화 알고리즘
        config.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
        // 반복할 해싱 회수
        config.setKeyObtentionIterations("1000");
        config.setProvider(new BouncyCastleProvider());
        // salt 생성 클래스
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        // 암/복호화 인스턴스 : 0보다 커야
        config.setPoolSize("1");
        config.setStringOutputType(secretType);

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config);

        return encryptor;
    }
*/


    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("xit5811807!@");

        String url = encryptor.encrypt("jdbc:mariadb://127.0.0.1:4407/ens?useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true&autoReconnect=true");
        String id = encryptor.encrypt("root");
        String passwd = encryptor.encrypt("xit1807");
        String url2 = encryptor.encrypt("jdbc:oracle:thin:@211.119.124.115:1521:XITSMS");
        String id2 = encryptor.encrypt("xit_sms_lg");
        String passwd2 = encryptor.encrypt("xit_sms_lg");
        System.out.println(url);
        System.out.println(id);
        System.out.println(passwd);
        System.out.println(encryptor.decrypt(url));
        System.out.println(url2);
        System.out.println(id2);
        System.out.println(passwd2);
        System.out.println(encryptor.decrypt(url2));
    }

}
