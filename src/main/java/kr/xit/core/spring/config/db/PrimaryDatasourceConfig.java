package kr.xit.core.spring.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import kr.xit.core.consts.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <pre>
 * description : Datasource 설정 - spring.datasource.hikari.primary
 *               - 조건 : spring.datasource.hikari.primary.username 이 있는 경우
 *               실제 필요한 경우만 커넥션을 점유하도록
 *               LazyConnectionDataSourceProxy 사용
 *               일반 Datasource 사용시
 *               - Spring은 트랜잭션에 진입시 데이타 소스의 커넥션을 get
 *               - ehcache, hibernate 영속성 컨택슽트 1차캐시 등에도 커넥션을 get
 *               - multi-datasource 에서 트랜잭션 진입 이후 datasource 분기 불가
 * packageName : kr.xit.core.spring.config.db
 * fileName    : PrimaryDatasourceConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 * 2023-10-30    julim       dynamic 설정 적용
 *
 * </pre>
 * @see PrimaryMybatisConfig
 */
@Configuration
@ConditionalOnProperty(value = "spring.datasource.hikari.primary.username")
public class PrimaryDatasourceConfig {
    @Bean(name = "primaryHikariConfig")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
    public HikariConfig primaryHikariConfig() {
        // HikariConfig hikariConfig = new HikariConfig("spring.datasource.hikari");
        // hikariConfig.setAutoCommit(false);
        // return hikariConfig;
        return new HikariConfig();
    }

    @Bean(Constants.PRIMARY_DATA_SOURCE)
    @Primary
    public DataSource primaryDataSource() {
        //return new LazyConnectionDataSourceProxy(new HikariDataSource(primaryHikariConfig()));
        return new HikariDataSource(primaryHikariConfig());
    }
}
