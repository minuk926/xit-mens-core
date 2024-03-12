package kr.xit.core.spring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kr.xit.core.support.utils.Checks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * description : Springdoc(swagger) 설정
 *               설정내용이 상이한 경우 동일한 파일로 재정의 하거나 상속받아 사용
 *               - swagger url을 강제하고 싶으면
 *                 -> app.swagger-url 에 설정
 * packageName : kr.xit.core.spring.config
 * fileName    : SpringDocsConfig
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@ConditionalOnProperty(value = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = false)
@Configuration
public class SpringDocsConfig {
    @Value("${server.port}")
    private int SERVER_PORT;
    @Value("${server.http:0}")
    private int HTTP_PORT;
    @Value("${app.swagger-url:}")
    private String swaggerUrl;

    @Bean
    public OpenAPI openAPI(
        @Value("${springdoc.version:v1}") String version,
        @Value("${app.desc}") String desc,
        @Value("${app.name}") String name,
        @Value("${spring.profiles.active}") String active) {

        Info info = new Info()
            .title(String.format("%s : %s 서버( %s )", desc, name, active))  // 타이틀
            .version(version)           // 문서 버전
            .description("잘못된 부분이나 오류 발생 시 바로 말씀해주세요.") // 문서 설명
            .contact(new Contact()      // 연락처
                .name("관리자")
                .email("admin@xit.co.kr"));
                //.url("http://www.xerotech.co.kr/"));

        // https enabled
        List<Server> servers = new ArrayList<>();
        if(HTTP_PORT != 0){
            String httpUrl = Checks.isNotEmpty(swaggerUrl)? swaggerUrl : String.format("http://localhost:%d", HTTP_PORT);
            String httpsUrl = Checks.isNotEmpty(swaggerUrl)? swaggerUrl : String.format("https://localhost:%d", SERVER_PORT);
            servers.add(new Server().url(httpUrl).description(name + "(" + active + ")"));
            servers.add(new Server().url(httpsUrl).description(name + "(" + active + ")"));
        }else {
            String httpUrl = Checks.isNotEmpty(swaggerUrl)? swaggerUrl : String.format("http://localhost:%d", SERVER_PORT);
            servers.add(new Server().url(httpUrl).description(name + "(" + active + ")"));
        }

        // Security 스키마 설정
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            // .name(HttpHeaders.AUTHORIZATION);
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            // Security 인증 컴포넌트 설정
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            // API 마다 Security 인증 컴포넌트 설정
            //.addSecurityItem(new SecurityRequirement().addList("JWT"))
            .security(Collections.singletonList(securityRequirement))
            .info(info)
            .servers(servers);
    }
}
