package kr.xit.core.spring.config.db;

import javax.sql.DataSource;
import kr.xit.core.consts.Constants;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * <pre>
 * description : Mybatis 설정 - spring.datasource.hikari.secondary
 *               - 조건 : spring.datasource.hikari.secondary.username이 있는 경우 loading
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
 * @see SecondaryDatasourceConfig
 */
@Configuration
@ConditionalOnProperty(value = "spring.datasource.hikari.secondary.username")
@MapperScan(
        basePackages = {"kr.xit.biz.sms.mapper"},
        sqlSessionFactoryRef = Constants.SECONDARY_SQL_SESSION
)
public class SecondaryMybatisConfig {
    @Value("${spring.datasource.hikari.secondary.database}")
    private String database;
    private static final String MYBATIS_CONFIG_FILE = "classpath:/egovframework/mapper/mapper-config.xml";

    @Bean(name = Constants.SECONDARY_SQL_SESSION)
    public SqlSessionFactory secondarySqlSession(@Qualifier(Constants.SECONDARY_DATA_SOURCE) DataSource dataSource) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(resolver.getResource(MYBATIS_CONFIG_FILE));
        sessionFactory.setMapperLocations(resolver.getResources(String.format("classpath:/egovframework/mapper/**/*-%s-mapper.xml", database)));
        return sessionFactory.getObject();
    }

    //@ConditionalOnProperty(value = "spring.datasource.hikari.secondary")
    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier(Constants.SECONDARY_SQL_SESSION) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
