package kr.xit.core.spring.config.support;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * description : Http, Https(SSL) 적용 - 내장 톰캣 SSL use
 *               server.ssl.redirect : false인 경우 HTTP와 SSL 동시 사용
 *               application-https.yml 선언 사용
 *               - server.ssl.enabled: true인 경우 활성화(적용)
 *               - server.port : SSL port
 *               - server.http : http port
 *               - server.ssl.redirect : true|false
 * packageName : kr.xit.core.spring.config.support
 * fileName    : HttpsConnectorConfig
 * author      : julim
 * date        : 2023-11-08
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-11-08    julim       최초 생성
 *
 * </pre>
 */
@ConditionalOnProperty(value = "server.http")
@Configuration
public class HttpsConnectorConfig {
    @Value("${server.port}")
	private int SSL_PORT;
	@Value("${server.http}")
	private int HTTP_PORT;
	@Value("${server.ssl.redirect}")
	private boolean SSL_REDIRECT;
    @Bean
    public ServletWebServerFactory servletContainer() {

		TomcatServletWebServerFactory tomcat = null;
//		try {
//			ignoreSsl();
//		} catch (Exception e) {
//			throw BizRuntimeException.create(e.getMessage());
//		}

		if(SSL_REDIRECT){
			tomcat = new TomcatServletWebServerFactory() {
				@Override
				protected void postProcessContext(Context context) {
					SecurityConstraint securityConstraint = new SecurityConstraint();
					securityConstraint.setUserConstraint("CONFIDENTIAL");
					SecurityCollection collection = new SecurityCollection();
					collection.addPattern("*");
					securityConstraint.addCollection(collection);
					context.addConstraint(securityConstraint);
				}
			};
		}else{
			tomcat = new TomcatServletWebServerFactory();
		}
		tomcat.addAdditionalTomcatConnectors(createSslConnector());
		return tomcat;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(HTTP_PORT);
        if(SSL_REDIRECT)	connector.setRedirectPort(SSL_PORT);
        return connector;
    }

	//------------------------------------------------------------------------------------------------
	// SSL 인증서 skip
	//------------------------------------------------------------------------------------------------
	/**
	 * SSL 인증서 skip
	 * @throws Exception
	 */
	public void ignoreSsl() throws Exception {
		HostnameVerifier hv = (urlHostName, session) -> true;
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements TrustManager, X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}
	}
	//-------------------------------------------------------------------------------------------
}

