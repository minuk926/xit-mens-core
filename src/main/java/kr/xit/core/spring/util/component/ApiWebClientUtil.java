package kr.xit.core.spring.util.component;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kr.xit.biz.ens.model.cmm.CmmEnsRlaybsnmDTO;
import kr.xit.core.consts.Constants;
import kr.xit.core.exception.ClientErrorException;
import kr.xit.core.exception.ErrorParse;
import kr.xit.core.exception.ServerErrorException;
import kr.xit.core.model.ApiResponseDTO;
import kr.xit.core.spring.config.support.WebClientConfig;
import kr.xit.core.support.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <pre>
 * description : react Restfull Util
 *               error(.onStatus)는 ExchangeFilterFunction {@link WebClientConfig responseFilter} 에서 처리
 * packageName : kr.xit.core.spring.util
 * fileName    : ApiWebClientUtil
 * author      : julim
 * date        : 2023-09-06
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-06    julim       최초 생성
 *
 * </pre>
 * @see WebClientConfig
 * @see kr.xit.core.spring.config.AsyncExecutorConfig
 * @see ClientErrorException
 * @see ServerErrorException
 * @see ErrorParse
 * @see kr.xit.core.spring.config.support.CustomJacksonConfig
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiWebClientUtil {
    private static final String AUTH_TYPE_BEARER = "bearer";
    private final WebClientConfig webClientConfig;

    /**
     * WebClient GET 호출 처리
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> {@link WebClientConfig responseFilter} 에서 처리
     * @param url String
     * @param responseDtoClass Class<T>
     * @param headerMap Map<String, String>
     * @return responseDtoClass
     */
    public <T> T get(final String url, final Class<T> responseDtoClass, Map<String, String> headerMap) {
        return webClientConfig.webClient()
                .method(HttpMethod.GET)
                .uri(url)
                .headers(httpHeaders -> getHeaders(httpHeaders, headerMap))
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();
    }

    /**
     * WebClient POST 호출 처리
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> {@link WebClientConfig responseFilter} 에서 처리
     * @param url String
     * @param requestDto V
     * @param responseDtoClass Class<T>
     * @param headerMap Map<String, String>
     * @return responseDtoClass
     */
    public <T, V> T post(final String url, final V requestDto, final Class<T> responseDtoClass, Map<String, String> headerMap) {
        return webClientConfig.webClient()
                .method(HttpMethod.POST)
                .uri(url)
                .headers(httpHeaders -> getHeaders(httpHeaders, headerMap))
                .bodyValue(Objects.requireNonNullElse(requestDto, ""))
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();
    }

    /**
     * kakao WebClient 호출 처리
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> {@link WebClientConfig responseFilter} 에서 처리
     * @param url String
     * @param method HttpMethod
     * @param body Object
     * @param rtnClzz Class<T>
     * @param ensDTO CmmEnsRlaybsnmDTO
     * @return rtnClzz<T>
     */
    public <T> T exchangeKko(final String url, final HttpMethod method, final Object body, final Class<T> rtnClzz, final CmmEnsRlaybsnmDTO ensDTO) {

        Map<String, String> map = new HashMap<>();
        map.put(HttpHeaders.AUTHORIZATION,
            String.format("%s %s", Constants.JwtToken.GRANT_TYPE.getCode(), ensDTO.getKakaoAccessToken()));
        map.put(Constants.HeaderName.UUID.getCode(), ensDTO.getKakaoContractUuid());

        return exchange(url, method, body, rtnClzz, map);
    }

    /**
     * KT-BC WebClient 호출 처리
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> {@link WebClientConfig responseFilter} 에서 처리
     * @param url String
     * @param method HttpMethod
     * @param body Object
     * @param rtnClzz Class<T>
     * @param ensDTO CmmEnsRlaybsnmDTO
     * @return rtnClzz<T>
     */
    public <T> T exchangeKt(final String url, final HttpMethod method, final Object body, final Class<T> rtnClzz, final CmmEnsRlaybsnmDTO ensDTO) {
        final Map<String,String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.AUTHORIZATION, String.format("%s %s", AUTH_TYPE_BEARER, ensDTO.getKtAccessToken()));
        headerMap.put("client-id", ensDTO.getKtClientId());
        headerMap.put("client-tp", ensDTO.getKtClientTp());

        return exchange(url, method, body, rtnClzz, headerMap);
    }

    /**
     * <pre>
     * WebClient 호출 처리
     * GET 요청시 url에 파라메터를 포함해야 함(?key=value&key2=value2)
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> {@link WebClientConfig responseFilter} 에서 처리
     * @param url call url
     * @param method POST|GET
     * @param body JSON String type
     * @param rtnClzz rtnClzz return type class
     *        (ex: new KkopayDocDTO.DocStatusResponse().getClass())
     * @return T rtnClzz return DTO
     * </pre>
     */
    public <T> T exchange(final String url, final HttpMethod method, final Object body, final Class<T> rtnClzz, final Map<String,String> headerMap) {

        return webClientConfig.webClient()
            .method(method)
            .uri(url)
            .headers(httpHeaders -> getHeaders(httpHeaders, headerMap))
            .bodyValue(Objects.requireNonNullElse(body, ""))
            .exchangeToMono(res -> res.bodyToMono(rtnClzz))
            .block();
    }

    /**
     * <pre>
     * WebClient form data 호출 처리
     * -> application/x-www-form-urlencoded 전송시
     * GET 요청시 url에 파라메터를 포함해야 함(?key=value&key2=value2)
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> {@link WebClientConfig responseFilter} 에서 처리
     * @param url call url
     * @param method POST|GET
     * @param body JSON String type
     * @param rtnClzz rtnClzz return type class
     *        (ex: new KkopayDocDTO.DocStatusResponse().getClass())
     * @return T rtnClzz return DTO
     * </pre>
     */
    public <T> T exchangeFormData(final String url, final HttpMethod method, final Object body, final Class<T> rtnClzz, final Map<String,String> headerMap) {

        return webClientConfig.webClient()
            .method(method)
            .uri(url)
            .headers(httpHeaders -> getHeaders(httpHeaders, headerMap))
            .body(ObjectUtils.isNotEmpty(body)? BodyInserters.fromFormData(JsonUtils.toMultiValue(body)): BodyInserters.empty())
            .exchangeToMono(res -> res.bodyToMono(rtnClzz))
            .block();
    }

    /**
     * webclient file data 호출 처리
     * -> multipart/form-data 전송시
     * url에 파라메터를 포함해야 함(?key=value&key2=value2)
     * 에러(.onStatus status.is4xxClientError() || status.is5xxServerError())
     * -> WebClientConfig.responseFilter() 에서 처리
     * @param url
     * @param method
     * @param files
     * @param rtnClzz
     * @return rtnClzz
     */
    public <T> T exchangeFileData(final String url, final HttpMethod method, final List<MultipartFile> files, final String pFileName, final Class<T> rtnClzz) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        for(MultipartFile mf : files) {
            //builder.part(mf.getOriginalFilename().split("\\.")[0], mf.getResource());
            builder.part(pFileName, mf.getResource());
        }

        return webClientConfig.webClient()
            .method(method)
            .uri(url)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(builder.build()))
            .exchangeToMono(res -> res.bodyToMono(rtnClzz))
            .block();
//            .blockOptional()
//            .orElse("");

    }

    public <T> ApiResponseDTO<T> sendError(final Throwable e) {

        return ErrorParse.extractError(e.getCause());
    }
    private URI createUrl(final String endPoint, final String... value) {
        return UriComponentsBuilder.fromUriString(endPoint)
            .build(value);
    }

    private HttpHeaders getHeaders(final HttpHeaders headers, final Map<String, String> map) {
        if(ObjectUtils.isEmpty(map))    return headers;
        for(Map.Entry<String, String> e : map.entrySet()){
            headers.add(e.getKey(), e.getValue());
        }
        return headers;
    }
}
