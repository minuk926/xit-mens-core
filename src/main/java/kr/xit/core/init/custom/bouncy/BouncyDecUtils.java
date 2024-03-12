package kr.xit.core.init.custom.bouncy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * <pre>
 * description : 암호화(license) Utils
 *              -> bouncycastle 암호화 파일 복호화
 * packageName : kr.xit.core.spring.config.custom.bouncy
 * fileName    : BouncyDecUtils
 * author      : limju
 * date        : 2023-07-20
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-20    limju       최초 생성
 *
 * </pre>
 */
public class BouncyDecUtils {
    private static final String KEY_ALG = "RSA";

    /**
     * 암호화된 문자열 -> decoding
     *
     * @param privateKeyPath PrivateKey file path
     * @param encodeData     decoding 대상 문자열
     * @return 암호해제된 문자열
     * @throws Exception the exception
     */
    public static String decode(String privateKeyPath, String encodeData) throws Exception {

        try {
            String privateKeyPem = new String(Files.readAllBytes(Paths.get(privateKeyPath)), StandardCharsets.UTF_8);
            PrivateKey privateKey = pemToPrivateKey(privateKeyPem);

            Cipher cipher = Cipher.getInstance(KEY_ALG);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decBytes = cipher.doFinal(Base64.getDecoder().decode(encodeData.getBytes()));

            return new String(decBytes, StandardCharsets.UTF_8);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 암호화된 문자열 -> decoding
     *
     * @param privateKeyPath PrivateKey file path
     * @param key            the key
     * @param encodeData     decoding 대상 문자열
     * @return 암호해제된 문자열
     * @throws Exception the exception
     */
    public static String decode(String privateKeyPath, String key, String encodeData) throws Exception {

        try {
            byte[] encryptedFileBytes = getFileBytesFrom(privateKeyPath);
            String privateKeyPem = new String(decrypt(key, encryptedFileBytes), StandardCharsets.UTF_8);
            PrivateKey privateKey = pemToPrivateKey(privateKeyPem);

            Cipher cipher = Cipher.getInstance(KEY_ALG);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decBytes = cipher.doFinal(Base64.getDecoder().decode(encodeData.getBytes()));

            return new String(decBytes, StandardCharsets.UTF_8);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 파일 객체 복호롸(SEEDEngine 사용)
     *
     * @param key        128bits(String 16자리)
     * @param cipherText file byte 객체
     * @return byte [ ]
     */
    public static byte[] decrypt(String key, byte[] cipherText) {
        byte[] keyBytes = key.getBytes();

        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new SEEDEngine());
        cipher.init(false, new KeyParameter(keyBytes));

        return getBytes(cipherText, cipher);
    }

    //----------------------------------------------------------------------------------

    /**
     * String PrivateKey -> PrivateKey
     * - 파일에 저장한 키 read시 사용
     * @param privateKeyPem String
     * @return PrivateKey
     */
    private static PrivateKey pemToPrivateKey(String privateKeyPem) throws Exception {
        PemReader pemReader = new PemReader(new StringReader(privateKeyPem));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();

        byte[] privateKeyBytes = pemObject.getContent();
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALG);

        return keyFactory.generatePrivate(privateKeySpec);
    }

    private static byte[] getFileBytesFrom(String path) throws IOException {

        // 파일 객체 생성
        File file = new File(path);
        byte[] fileBytes = new byte[(int)file.length()];

        try(FileInputStream fis = new FileInputStream(file);){
            fis.read(fileBytes);

        } catch (IOException e) {
            throw e;
        }
        return fileBytes;
    }

    private static byte[] getBytes(byte[] targetData, BufferedBlockCipher cipher) {
        byte[] outputData = new byte[cipher.getOutputSize(targetData.length)];

        int tam = cipher.processBytes(targetData, 0, targetData.length, outputData, 0);

        try {
            cipher.doFinal(outputData, tam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputData;
    }
}
