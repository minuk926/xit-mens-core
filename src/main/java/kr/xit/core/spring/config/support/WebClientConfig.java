package kr.xit.core.spring.config.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.exception.ClientErrorException;
import kr.xit.core.exception.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

/**
 * <pre>
 * description : WebClient configuration
 *               Spring WebFlux 기반 HTTP Client Config
 *               logging : - ExchangeFilterFunction 구현 처리 가능(requestFilter, responseFilter)
 *                         - logging.level.reactor.netty.http.client: DEBUG|ERROR
 *
 * packageName : kr.xit.core.spring.config.support
 * fileName    : WebClientConfig
 * author      : julim
 * date        : 2023-09-06
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-06    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

    @Value("${app.contract.connection.timeout:5000}")
    private int connectTimeout;
    @Value("${app.contract.connection.readTimeout:5000}")
    private int readTimeout;

    private final ObjectMapper objectMapper;
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

    /**
     * setEncodingMode : GET 요청의 파라미터 셋팅을 하기 위한 URI 템플릿의 인코딩을 위한 설정
     * @return
     */
    @Bean
    public WebClient webClient() {
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .uriBuilderFactory(factory)
                .clientConnector(new ReactorClientHttpConnector(defaultHttpClient()))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .exchangeStrategies(defaultExchangeStrategies())
                .filters(exchangeFilterFunctions -> {
                    //exchangeFilterFunctions.add(requestFilter());
                    exchangeFilterFunctions.add(responseFilter());
                })
                .build();
    }

    /**
     * <pre>
     * Http client 생성
     * - 요청 / 응답 debugging을 위해 wiretap 설정 - AdvancedByteBufFormat.TEXTUAL
     * @return HttpClient
     * </pre>
     */
    @Bean
    public HttpClient defaultHttpClient() {
        try {
            // SSL check bypass
            SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

            return HttpClient.create(connectionProvider())
                .secure(t -> t.sslContext(sslContext))
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG,
                    AdvancedByteBufFormat.TEXTUAL)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .responseTimeout(Duration.ofMillis(this.connectTimeout))
                .doOnConnected(conn ->
                    conn.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)));
        }catch(SSLException se){
            throw BizRuntimeException.create(se.getMessage());
        }
    }

    /**
     * maxConnections : connection pool의 갯수
     * pendingAcquireTimeout : 커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
     * pendingAcquireMaxCount : 커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
     * maxIdleTime : 커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
     * @return ConnectionProvider
     */
    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(2000L))
                .build();
    }

    /**
     * <pre>
     * 1. 256KB 보다 큰 HTTP 메시지를 처리 시도 → DataBufferLimitException 에러 발생 방어
     * 2. messageWriters를 통한 logging(setEnableLoggingRequestDetails(true)
     *    -> org.springframework.web.reactive.function.client.ExchangeFunctions: DEBUG 하여 활성
     *    -> defaultHttpClient()의 wiretap 사용으로 비활성
     * </pre>
     * @return ExchangeStrategies
     */
    @Bean
    public ExchangeStrategies defaultExchangeStrategies() {
        // 256KB 보다 큰 HTTP 메시지를 처리 시도 → DataBufferLimitException 에러 발생 방어
        return ExchangeStrategies.builder()
            .codecs(config -> {
                config.defaultCodecs().maxInMemorySize(2 * 1024 * 1024);
                config.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                config.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
            })
            .build();

//        es.messageWriters()
//            .stream()
//            .filter(LoggingCodecSupport.class::isInstance)
//            .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));
    }

    private ExchangeFilterFunction requestFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(cr -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("\n>>>>>>>>>> WebClient Http Request <<<<<<<<<<<<<\n");
                sb.append(logMethodAndUrl(cr));
                sb.append(logHeaders(cr));
                sb.append("-------------------------------------------------------");
                log.debug(sb.toString());
            }
            return Mono.just(cr);
        });
    }

    /**
     * reponse logging && error Handling
     * @return ExchangeFilterFunction
     */
    private ExchangeFilterFunction responseFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(cr -> {

            HttpStatus status = cr.statusCode();

            if(cr.statusCode().is4xxClientError()) {
                return cr.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ClientErrorException(status, errorBody)));

            } else if(cr.statusCode().is5xxServerError()) {
                return cr.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new ServerErrorException(status, errorBody)));
            }

//            if(log.isDebugEnabled()) {
//                StringBuilder sb = new StringBuilder(
//                    "\n>>>>>>>>>> WebClient Http Response <<<<<<<<<<<<<\n");
//                sb.append(logStatus(cr));
//                sb.append(logHeaders(cr));
//                sb.append("-------------------------------------------------------");
//                log.debug(sb.toString());
//            }
            return Mono.just(cr);
        });
    }

    private static String logStatus(ClientResponse response) {
        HttpStatus status = response.statusCode();
        return String.format("Returned staus code %s (%s)", status.value(), status.getReasonPhrase());
    }

    private static String logHeaders(ClientRequest request) {
        StringBuilder sb =  new StringBuilder();

        request.headers()
                .forEach((name, values) ->
                        values.forEach(value -> sb.append(name).append(": ").append(value).append("\n"))
                );
        return sb.toString();
    }

    private static String logHeaders(ClientResponse response) {
        StringBuilder sb =  new StringBuilder();
        response.headers()
            .asHttpHeaders()
            .forEach((name, values) ->
                values.forEach(value -> sb.append(name).append(": ").append(value).append("\n"))
        );
        return sb.toString();
    }

    private static String logMethodAndUrl(ClientRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.method().name());
        sb.append(" to ");
        sb.append(request.url());

        return sb.append("\n").toString();
    }
}
