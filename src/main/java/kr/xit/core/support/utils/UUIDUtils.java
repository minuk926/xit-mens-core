package kr.xit.core.support.utils;

import java.util.Random;
import java.util.UUID;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.core.support.utils
 * fileName    : UUIDUtils
 * author      : limju
 * date        : 2023-12-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-12-04    limju       최초 생성
 *
 * </pre>
 */
public class UUIDUtils {

    /**
     * length 길이의 UUID String return -> '-' remove
     * @param length
     * @return
     */
    public static String generateLengthUuid(int length) {
        final String allChars = UUID.randomUUID().toString().replace("-", "");
        final Random random = new Random();
        final char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = allChars.charAt(random.nextInt(allChars.length()));
        }
        return String.valueOf(otp);
    }

    /**
     * 32자 UUID String return -> '-' remove
     * @return 32자 UUID String return -> '-' remove
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
