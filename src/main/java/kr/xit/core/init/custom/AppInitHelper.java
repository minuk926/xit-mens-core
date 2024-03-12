package kr.xit.core.init.custom;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import kr.xit.core.init.custom.bouncy.BouncyDecUtils;

/**
 * <pre>
 * description : licence module
 *               app.licence.path, key, data1, data2, data3
 *               path - private key file path
 *               key - key
 *               data1 - license
 *               data2 - primary DB info
 *               data3 - secondary DB info
 *
 * packageName : kr.xit.core.spring.config.custom
 * fileName    : AppInitHelper
 * author      : limju
 * date        : 2023-07-24
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-24    limju       최초 생성
 *
 * </pre>
 */
@Slf4j
public class AppInitHelper {
    private ConfigurableApplicationContext applicationContext;

    /**
     * Instantiates a new App init helper.
     *
     * @param applicationContext the application context
     */
    public AppInitHelper(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Parse init.
     *
     * @throws Exception the exception
     */
    public void parseInit() throws Exception {
        init();
    }

    private void init() throws Exception {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        Properties props = new Properties();

        String p = env.getProperty("app.license.path");
        String k = env.getProperty("app.license.key");
        // 10 + 17 + ~
        String license = env.getProperty("app.license.data1");
        long curDate = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
        String decLicense = BouncyDecUtils.decode(p+"private_key.pem", k, license);

        if(Long.parseLong(decLicense.substring(0,10)) < curDate){
            log.error("유효기간이 만료 되었습니다.");
            System.exit(0);
        }

        try {
            if (!getMacAddress().contains(decLicense.substring(10, 27).toUpperCase())) {
                log.info("Mac Address :: {}", getMacAddress());
                log.info("MAC Data :: {}", decLicense.substring(10, 27).toUpperCase());
                log.error("인증된 서버가 아닙니다.");
                System.exit(0);
            }

            if (!getIpAddress().contains(decLicense.substring(27))) {
                log.error("IP :: {}", getIpAddress());
                log.error("IP Data :: {}", decLicense.substring(27));
                log.error("인증된 서버가 아닙니다.");
                System.exit(0);
            }
        } catch (SocketException e) {
            log.error(e.getLocalizedMessage());
            System.exit(0);
        }

        String db = BouncyDecUtils.decode(p+"private_key.pem", k, env.getProperty("app.license.data2"));
        String[] arrDb = db.split(";");
        props.put("spring.datasource.hikari.primary.driver-class-name", arrDb[0]);
        props.put("spring.datasource.hikari.primary.read-only", arrDb[4]);
        props.put("spring.datasource.hikari.primary.jdbc-url", arrDb[3]);
        props.put("spring.datasource.hikari.primary.username", arrDb[1]);
        props.put("spring.datasource.hikari.primary.password", arrDb[2]);
        db = null;
        arrDb = null;

        db = BouncyDecUtils.decode(p+"private_key.pem", k, env.getProperty("app.license.data3"));
        arrDb = db.split(";");
        props.put("spring.datasource.hikari.secondary.driver-class-name", arrDb[0]);
        props.put("spring.datasource.hikari.secondary.read-only", arrDb[4]);
        props.put("spring.datasource.hikari.secondary.jdbc-url", arrDb[3]);
        props.put("spring.datasource.hikari.secondary.username", arrDb[1]);
        props.put("spring.datasource.hikari.secondary.password", arrDb[2]);

        env.getPropertySources().addFirst(new PropertiesPropertySource("decodeProps", props));
    }

    private List<String> getMacAddress() throws SocketException {
        List<String> macs = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {
                NetworkInterface network = e.nextElement();

                byte[] bmac = network.getHardwareAddress();
                if (bmac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bmac.length; i++) {
                        sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));
                    }

                    if (sb.toString().isEmpty() == false) {
                        macs.add(sb.toString().toUpperCase());
                    }
                }
            }
        } catch (SocketException e){
            throw e;
        }
        return macs;
    }

    private List<String> getIpAddress() throws SocketException {
        List<String> ips = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                Enumeration<InetAddress> kk = ni.getInetAddresses();

                while (kk.hasMoreElements()) {
                    InetAddress inetAddress = kk.nextElement();
                    if(inetAddress.getAddress().length == 4)    ips.add(inetAddress.getHostAddress().toString());
                }
            }
            return ips.stream().distinct().toList();
        } catch (SocketException e) {
            throw e;
        }
    }

}
