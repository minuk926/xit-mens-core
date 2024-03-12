package kr.xit.core.spring.config.support;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : 비동기 처리를 위해 TaskExecutor 사용시 executor 는 새로운 Thread 를 생성
 *               -> 기존 쓰레드의 context 가 넘어가지 않는다
 *               -> TaskDecorator(spring4.3 이상에서 사용) TaskExecutor 생성시
 *               -> RequestContextHolder의 ThreadLocal attributes copy
 *               -> MDC 전체 copy
 *               -> TaskExecutor.setTaskDecorator()에 주입
 * packageName : kr.xit.core.spring.config.support
 * fileName    : ClonedTaskDecorator
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see kr.xit.core.spring.config.AsyncExecutorConfig
 */
@Slf4j
public class ClonedTaskDecorator implements TaskDecorator {
	@Override
	public Runnable decorate(Runnable task) {
		Map<String, String> mdcMap = MDC.getCopyOfContextMap();
		//MDC.clear();
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

		return () -> {
			if (attributes != null) {
				RequestContextHolder.setRequestAttributes(attributes);
			}
			if (mdcMap != null) {
				MDC.setContextMap(mdcMap);
				log.info(">>>>>>>>>>>>>>>ClonedTaskDecorator: {}", mdcMap);
			}
			task.run();
		};
	}
}
