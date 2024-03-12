package kr.xit.core.spring.config.db;

import javax.sql.DataSource;
import kr.xit.core.consts.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <pre>
 * description : Mybatis 설정
 *               - single DB or multi DB 설정에 따라 transaction 설정 적용
 * packageName : kr.xit.core.spring.config.db
 * fileName    : MybatisConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 * 2023-10-30    julim       dynamic 설정 적용
 *
 * </pre>
 * @see PrimaryDatasourceConfig
 * @see SecondaryMybatisConfig
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    /**
     * primary DB 만 선언 된 경우
     * @param ds DataSource
     * @return PlatformTransactionManager
     */
    @ConditionalOnMissingBean(SecondaryMybatisConfig.class)
    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(@Qualifier(Constants.PRIMARY_DATA_SOURCE)DataSource ds) {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager(ds);
        dstm.setGlobalRollbackOnParticipationFailure(false);
        return dstm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // ChainedTransactionManager : trsnsaction binding
    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * primary DB & secondary DB Transaction binding
     * @param primaryDS primary DataSource
     * @param secondaryDS secondary DataSource
     * @return PlatformTransactionManager
     */
    @ConditionalOnProperty(value = "spring.datasource.hikari.secondary.username")
    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier(Constants.PRIMARY_DATA_SOURCE)DataSource primaryDS,
                                                         @Qualifier(Constants.SECONDARY_DATA_SOURCE) DataSource secondaryDS) {
        DataSourceTransactionManager mariaTm = new DataSourceTransactionManager(primaryDS);
        mariaTm.setGlobalRollbackOnParticipationFailure(false);
        mariaTm.setNestedTransactionAllowed(true);

        DataSourceTransactionManager oracleTm = new DataSourceTransactionManager(secondaryDS);
        oracleTm.setGlobalRollbackOnParticipationFailure(false);
        oracleTm.setNestedTransactionAllowed(true);

        // creates chained transaction manager
        return new ChainedTransactionManager(mariaTm, oracleTm);
    }
    /////////////////////////////////////////////////////////////////////////////////////
}
