package kr.xit.core.spring.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import kr.xit.core.spring.config.support.ClonedTaskDecorator;
import kr.xit.core.spring.config.support.CustomAsyncExceptionHandler;

/**
 * <pre>
 * description : Async 설정
 *               - @Scheduler와 동시 사용시는 반드시 Bean name을 지정
 *                 사용 서비스에서 지정해 줘야 한다 (@Async("asyncExecutor")
 * packageName : kr.xit.core.spring.config
 * fileName    : AsyncExecutorConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see kr.xit.core.spring.util.ApiWebClient
 */
@Configuration
@EnableAsync
public class AsyncExecutorConfig extends AsyncConfigurerSupport {

    @Value("${app.contract.connection.thread.corePoolSize:5}")
    private int corePoolSize;

    @Value("${app.contract.connection.thread.maxPoolSize:10}")
    private int maxPoolSize;

    /**
     * <pre>
     * 비동기 호출 Thread 설정
     * AsyncConfigurerSupport 상속
     * 서비스의 public 메소드에 @Async annotation 사용하여 적용(private 메소드는 불가)
     * corePoolSize: 지정한 사이즈 만큼 동시 처리
     * MaxPoolSise: 동시 동작하는, 최대 Thread 갯수
     * QueueCapacity : MaxPoolSize 초과 요청시 해당 내용을 Queue에 저장하고, 여유가 발생하면 하나씩 꺼내져서 동작
     * ThreadNamePrefix: spring이 생성하는 쓰레드의 접두사 지정
     * org.springframework.util.concurrent.ListenableFuture 타입으로 받아 처리
     *
     * TaskExecutor 사용시 executor는 새로운 Thread를 생성
     * -> 기존 쓰레드의 context가 넘어오지 않는다
     * -> TaskDecorator(spring4.3 이상에서 사용) TaskExecutor 생성시 MDC 전체 copy
     * @return Executor
     * </pre>
     * @see ClonedTaskDecorator
     */
    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);

        // MDC copy
        executor.setTaskDecorator(new ClonedTaskDecorator());

        //shutdown 상태가 아니라면 ThreadPoolTaskExecutor에 요청한 thread에서 직접 처리
        //예외와 누락 없이 최대한 처리하려면 CallerRunsPolicy로 설정하는 것이 좋음
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //최대 종료 대기 시간
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);	// shutdown 최대 60초 대기
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();

        return executor;
        //return threadPoolExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return new CustomAsyncExceptionHandler();
    }
}
