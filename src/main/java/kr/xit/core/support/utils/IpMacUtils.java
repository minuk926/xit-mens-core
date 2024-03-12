package kr.xit.core.support.utils;

import kr.xit.core.exception.BizRuntimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>
 * description : IP address and Mac address Utils
 *
 * packageName : kr.xit.core.support.utils
 * fileName    : IpMacUtils
 * author      : limju
 * date        : 2023-07-20
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-20    limju       최초 생성
 *
 * </pre>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpMacUtils {
    /**
     * Mac address List<String> return
     *
     * @return {@code List<String>} Mac address List
     */
    public static List<String> getMacAddress() {
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
            log.error("{}", e.getLocalizedMessage());
            throw BizRuntimeException.create(e.getLocalizedMessage());
        }
        return macs;
    }

    /**
     * Mac address String return
     * @param ip String
     * @return {@code String} Mac address String
     */
    public static String getMacAddress(String ip) {
        StringBuilder sb = new StringBuilder();

        try {
            InetAddress add = InetAddress.getByName(ip);

            NetworkInterface ni = NetworkInterface.getByInetAddress(add);
            if (ni != null) {
                byte[] bmac = ni.getHardwareAddress();
                if (bmac != null) {
                    for (int i = 0; i < bmac.length; i++) {
                        sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));
                    }
                }
            }
            return sb.toString().toUpperCase();
        } catch (UnknownHostException | SocketException e) {
            log.error("{}", e.getLocalizedMessage());
            throw BizRuntimeException.create(e.getLocalizedMessage());
        }
    }

    /**
     * IP address List<String></String> return
     *
     * @return {@code List<String>} IP address or ""
     */
    public static List<String> getIpAddress() {
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
            log.error("{}", e.getLocalizedMessage());
            throw BizRuntimeException.create(e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        log.info("==>>{}", getIpAddress());
        log.info("==>>{}", getMacAddress());
        log.info("==>>{}", getMacAddress("211.119.124.73"));
    }
}
