package kr.xit.core.spring.config.support;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;

import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import kr.xit.core.support.utils.Checks;

/**
 * <pre>
 * description : P6Spy SQL 로그 Multiline 출력 포맷 Custom
 *               - sql이 있는 경우만 출력
 *               - prepared, url(DB), now 출력 제거
 * packageName : kr.xit.core.spring.config.support
 * fileName    : P6SpySqlMultilineFormat
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 * @see com.p6spy.engine.spy.appender.MultiLineFormat
 */

@Configuration
public class P6SpySqlMultilineFormat implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
        if(Checks.isEmpty(sql)) return StringUtils.EMPTY;

        return "connection " + connectionId + " | took " + elapsed + "ms | " + category + "\n" + sql +";";
    }
}
