package kr.xit.core.spring.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import kr.xit.core.consts.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * description : Datasource 설정 - spring.datasource.hikari.secondary
 *               - 조건 : spring.datasource.hikari.secondary.username 이 있는 경우
 *               실제 필요한 경우만 커넥션을 점유하도록
 *               LazyConnectionDataSourceProxy 사용
 *               일반 Datasource 사용시
 *               - Spring은 트랜잭션에 진입시 데이타 소스의 커넥션을 get
 *               - ehcache, hibernate 영속성 컨택슽트 1차캐시 등에도 커넥션을 get
 *               - multi-datasource 에서 트랜잭션 진입 이후 datasource 분기 불가
 * packageName : kr.xit.core.spring.config.db
 * fileName    : SecondaryMybatisConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 * 2023-10-30    julim       dynamic 설정 적용
 *
 * </pre>
 * @see SecondaryMybatisConfig
 */
@Configuration
@ConditionalOnProperty(value = "spring.datasource.hikari.secondary.username")
public class SecondaryDatasourceConfig {
    @Bean(name = "secondaryHikariConfig")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
    public HikariConfig secondaryHikariConfig() {
        // HikariConfig hikariConfig = new HikariConfig("spring.datasource.hikari");
        // hikariConfig.setAutoCommit(false);
        // return hikariConfig;
        return new HikariConfig();
    }

    @Bean(name = Constants.SECONDARY_DATA_SOURCE)
    public DataSource secondaryDataSource() {
        //return new LazyConnectionDataSourceProxy(new HikariDataSource(secondaryHikariConfig()));
        return new HikariDataSource(secondaryHikariConfig());
    }
}
