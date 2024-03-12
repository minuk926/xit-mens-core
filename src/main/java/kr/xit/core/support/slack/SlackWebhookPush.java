package kr.xit.core.support.slack;

import static com.slack.api.webhook.WebhookPayloads.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.xit.core.support.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : Slack webhook push
 *               Error 혹은 메세지를 slack에 push할 경우 사용
 * packageName : kr.xit.core.spring.support
 * fileName    : SlackWebhookPush
 * author      : limju
 * date        : 2023-05-11
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-11    limju       최초 생성
 *
 * </pre>
 * @see kr.xit.core.spring.annotation.TraceLogging
 */
@Slf4j
@Component
public class SlackWebhookPush {

    private final Slack slackClient = Slack.getInstance();

    @Value("${app.slack-webhook.url}")
    private String webhookUrl;

    private static final String DT_FMT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String API_CALL_ERR_MSG = "API 호출 에러";
    private static final String SLACK_CALL_ERR_MSG = "Slack 호출 에러";
    private static final String IP_HEADER = "X-FORWARDED-FOR";
    private static final String MSG_COLOR = "0xff0000";

    /**
     * http call Exception Slack push
     * @param e Exception
     * @param request HttpServletRequest
     */
    @Async
    public void sendSlackAlertErrorLog(final Exception e, final HttpServletRequest request) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                .text(API_CALL_ERR_MSG)
                .attachments(List.of(generateSlackAttachment(e, request)))
            ));
        } catch (IOException slackError) {
            log.error(SLACK_CALL_ERR_MSG);
        }
    }

    /**
     * http call message slack push
     * @param message String
     * @param request HttpServletRequest
     */
    @Async
    public void sendSlackAlertLog(final String message, final HttpServletRequest request) {
        sendSlackAlertLog(request.getRequestURL() + " " + request.getMethod(), message, getIp(request));
        try {
            final String requestTime = DateTimeFormatter.ofPattern(DT_FMT).format(LocalDateTime.now());

            slackClient.send(webhookUrl, payload(p -> p
                .text(API_CALL_ERR_MSG)
                .attachments(
                    List.of(Attachment.builder()
                        .color(MSG_COLOR)
                        .title(requestTime + " 발생 에러 로그")
                        .fields(
                            getFields(message, request.getRequestURL() + " " + request.getMethod(), getIp(request))
                        )
                        .build())
                )
            ));
        } catch (IOException slackError) {
            log.error(SLACK_CALL_ERR_MSG);
        }
    }

    /**
     * 에러 내용 slack push
     * @param message String
     * @param url String
     * @param ip String
     */
    @Async
    public void sendSlackAlertLog(final String message, final String url, String ip) {
        try {
            final String requestTime = DateTimeFormatter.ofPattern(DT_FMT).format(LocalDateTime.now());

            slackClient.send(webhookUrl, payload(p -> p
                .text(API_CALL_ERR_MSG)
                .attachments(
                    List.of(Attachment.builder()
                        .color(MSG_COLOR)
                        .title(requestTime + " 발생 에러 로그")
                        .fields(getFields(message, url, ip)
                        )
                        .build())
                )
            ));
        } catch (IOException slackError) {
            log.error(SLACK_CALL_ERR_MSG);
        }
    }

    private Attachment generateSlackAttachment(final Exception e, final HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern(DT_FMT).format(LocalDateTime.now());
        String xffHeader = request.getHeader(IP_HEADER);
        return Attachment.builder()
                .color(MSG_COLOR)
                .title(requestTime + " 발생 에러 로그")
                .fields(
                    getFields(
                        Checks.isEmpty(e.getCause()) ? e.getMessage() : e.getCause().getMessage(),
                        request.getRequestURL() + " " + request.getMethod(),
                        xffHeader == null ? request.getRemoteAddr() : xffHeader)
                )
                .build();
    }

    private Field generateSlackField(final String title, final String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }

    @NotNull
    private List<Field> getFields(String message, String url, String ip) {
        return List.of(
                generateSlackField("Request IP", ip),
                generateSlackField("Request URL", url),
                generateSlackField("Error Message", message)
        );
    }

    private String getIp(final HttpServletRequest request) {
        return request.getHeader(IP_HEADER) == null ? request.getRemoteAddr() : request.getHeader(IP_HEADER);
    }
}
