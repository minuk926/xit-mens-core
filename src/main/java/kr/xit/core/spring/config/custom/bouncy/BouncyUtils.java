package kr.xit.core.spring.config.custom.bouncy;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import kr.xit.biz.common.ApiConstants;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.support.utils.DateUtils;
import kr.xit.core.support.utils.FileUtil;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import kr.xit.core.init.custom.bouncy.BouncyDecUtils;

/**
 * <pre>
 * description : DSA 파일 생성 / 암/복호화 Utils
 *               DSA(Digital Signature Algorithm) 파일 암/복호화
 *              -> bouncycastle 암/복호화
 * packageName : kr.xit.core.spring.config.custom.bouncy
 * fileName    : BouncyUtils
 * author      : limju
 * date        : 2023-07-20
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-20    limju       최초 생성
 *
 * </pre>
 */
public class BouncyUtils {

    private static final String PRIVATE_KEY_FILE_NAME = "private_key.pem";
    private static final String PUBLIC_KEY_FILE_NAME = "public_key.pem";
    private static final String KEY_ALG = "RSA";

    /**
     * RSA PublicKey and PrivateKey 생성
     * @return KeyPair
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALG);
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        }catch(NoSuchAlgorithmException | InvalidParameterException e){
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    /**
     * RSA PublicKey and PrivateKey file 생성
     * @param keyFilePath key 파일 생성 위치
     */
    public static void generateKeyPairFile(String keyFilePath) {
        KeyPair keyPair = generateKeyPair();
        String privateKeyPem = keyToPem(keyPair.getPrivate(), "RSA PRIVATE KEY");
        String publicKeyPem = keyToPem(keyPair.getPublic(), "RSA PUBLIC KEY");

        try {
            Files.write(Paths.get(keyFilePath, PRIVATE_KEY_FILE_NAME), privateKeyPem.getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get(keyFilePath, PUBLIC_KEY_FILE_NAME), publicKeyPem.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e){
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    /**
     * RSA PublicKey and PrivateKey file 생성
     * -> 파일 암호화 적용 생성
     * @param keyFilePath key 파일 생성 위치
     */
    public static void generateKeyPairFile(String keyFilePath, String key) {
        KeyPair keyPair = generateKeyPair();
        String privateKeyPem = keyToPem(keyPair.getPrivate(), "RSA PRIVATE KEY");
        String publicKeyPem = keyToPem(keyPair.getPublic(), "RSA PUBLIC KEY");

        try {
            Files.write(Paths.get(keyFilePath, PRIVATE_KEY_FILE_NAME), encrypt(key, privateKeyPem.getBytes(StandardCharsets.UTF_8)));
            Files.write(Paths.get(keyFilePath, PUBLIC_KEY_FILE_NAME), encrypt(key, publicKeyPem.getBytes(StandardCharsets.UTF_8)));

        } catch (IOException e){
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    /**
     * 평문 암호화
     * @param publicKeyPath PublicKey file path
     * @param plainText 암호화 대상 문자열
     * @return 암호화된 문자열
     */
    public static String encode(String publicKeyPath, String plainText) {

        try {
            String publicKeyPem = new String(Files.readAllBytes(Paths.get(publicKeyPath)), StandardCharsets.UTF_8);
            PublicKey publicKey = pemToPublicKey(publicKeyPem);

            Cipher cipher = Cipher.getInstance(KEY_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytePlain = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(bytePlain);
        }catch (Exception e){
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    /**
     * 평문 암호화
     * @param publicKeyPath PublicKey file path
     * @param plainText 암호화 대상 문자열
     * @return 암호화된 문자열
     */
    public static String encode(String publicKeyPath, String key, String plainText) {

        try {
            byte[] encryptedFileBytes = FileUtil.getFileBytesFrom(publicKeyPath);
            String publicKeyPem = new String(BouncyDecUtils.decrypt(key, encryptedFileBytes), StandardCharsets.UTF_8);
            PublicKey publicKey = pemToPublicKey(publicKeyPem);

            Cipher cipher = Cipher.getInstance(KEY_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytePlain = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(bytePlain);
        }catch (Exception e){
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    public static void encryptionFile(final String key, final String filePath, final String fileName) throws IOException {
        byte[] plainFileBytes = FileUtil.getFileBytesFrom(filePath);
        FileUtil.saveFile(filePath, fileName, encrypt(key, plainFileBytes));
    }

    public static void decryptionFile(final String key, final String filePath, final String fileName) throws IOException {
        byte[] encryptedFileBytes = FileUtil.getFileBytesFrom(filePath);
        FileUtil.saveFile(filePath, fileName, BouncyDecUtils.decrypt(key, encryptedFileBytes));
    }

    /**
     * 파일 객체 암호화(SEEDEngine 사용)
     * @param key 128bits(String 16자리)
     * @param plainText file byte 객체
     * @return
     */
    public static byte[] encrypt(String key, byte[] plainText) {
        byte[] keyBytes = key.getBytes();

        // 블록 암호 운용
        // 블록보다 데이터가 짧을 경우 패딩을 사용함
        // 블록 암호 알고리즘으로는 SEED 알고리즘을 사용함
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new SEEDEngine());
        // 초기화 및 키 파라미터 생성 첫 번째 매개변수가 true 라면 암호화 모드
        cipher.init(true, new KeyParameter(keyBytes));

        return getBytes(plainText, cipher);
    }

    /**
     * PublicKey and PrivateKey file로 부터  KeyPair(PublicKey and PrivateKey) return
     * @param keyFilePath
     * @return KeyPair(PublicKey and PrivateKey)
     */
    public static KeyPair loadKeyPair(String keyFilePath) {
        try{
            String privateKeyPem = new String(Files.readAllBytes(Paths.get(keyFilePath, PRIVATE_KEY_FILE_NAME)), StandardCharsets.UTF_8);
            String publicKeyPem = new String(Files.readAllBytes(Paths.get(keyFilePath, PUBLIC_KEY_FILE_NAME )), StandardCharsets.UTF_8);

            PrivateKey privateKey = pemToPrivateKey(privateKeyPem);
            PublicKey publicKey = pemToPublicKey(publicKeyPem);

            return new KeyPair(publicKey, privateKey);
        }catch (Exception e){
            throw BizRuntimeException.create(e.getMessage());
        }
    }
    //----------------------------------------------------------------------------------

    /**
     * Key -> String 변환
     * @param key
     * @param type
     * @return
     */
    private static String keyToPem(Key key, String type) {
        StringWriter stringWriter = new StringWriter();
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
            pemWriter.writeObject(new PemObject(type, key.getEncoded()));
        } catch (IOException e) {
            throw BizRuntimeException.create(e.getMessage());
        }
        return stringWriter.toString();
    }

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

    /**
     * String publicKey -> PublicKey
     * - 파일에 저장한 키 read시 사용
     * @param publicKeyPem String
     * @return PublicKey
     */
    private static PublicKey pemToPublicKey(String publicKeyPem) throws Exception {
        PemReader pemReader = new PemReader(new StringReader(publicKeyPem));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();

        byte[] publicKeyBytes = pemObject.getContent();
        X509EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALG);

        return keyFactory.generatePublic(privateKeySpec);
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

     /**
     * 파일 객체 복호롸(SEEDEngine 사용)
     * @param key 128bits(String 16자리)
     * @param cipherText file byte 객체
     * @return
     */
    public static byte[] decrypt(String key, byte[] cipherText) {
        byte[] keyBytes = key.getBytes();

        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new SEEDEngine());
        cipher.init(false, new KeyParameter(keyBytes));

        return getBytes(cipherText, cipher);
    }

    // TODO::암호화할 속성
    public static void main(String[] args) throws Exception {

        String path = "d:/data/mens/.pem/";
        String macAddress = "00-21-5E-DB-59-36";  //dev mac IpMacUtils.getMacAddress("ip");
        //String macAddress = "FC-34-97-15-04-44";    //local mac
        String key = "mxLAM1fAEDPWkFz8";

        // enc public / private create
        //BouncyUtils.encryptionFile(key, "C:/workspace/git/ens-parent/xit-init/target/xit-init.jar");
        //BouncyUtils.encryptionFile(key, path+"public_key.pem");
        // dec public / private create
        //BouncyFileHelper.decryptionFile(key, path);
        //BouncyFileHelper.decryptionFile(key, path);


        long expiredDate = DateUtils.parseStringToLong("20991231135959", ApiConstants.FMT_DT_EMPTY_DLT);
        String ip = "211.119.124.9";// IpMacUtils.getIpAddress();
        //String ip = "211.119.124.73";// local ip
        long curDate = DateUtils.getLongTodayAndNowTime();

        String mariaAccount = "org.mariadb.jdbc.Driver;root;xit5811807;jdbc:mariadb://211.119.124.9:4407/ens?useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true&autoReconnect=true;false";
        String oracleAccount = "oracle.jdbc.OracleDriver;xit_sms_lg;xit_sms_lg;jdbc:oracle:thin:@211.119.124.115:1521:XITSMS;false";

        // 10 + 17 + ~
        String encData = String.format("%d%s%s", expiredDate, macAddress, ip, mariaAccount, oracleAccount);

        System.out.println(encData.substring(0, 10));
        System.out.println(encData.substring(10, 27));
        System.out.println(encData.substring(27));

        BouncyUtils.generateKeyPairFile(path, key);
        String license = encode(path+"public_key.pem", key, encData);
        String db1 = encode(path+"public_key.pem", key, mariaAccount);
        String db2 = encode(path+"public_key.pem", key, oracleAccount);
        //String decStr = decode(path+"private_key.pem", key, license);
        //String[] arrDec = decStr.split(";");
        System.out.println(license);
        System.out.println(db1);
        System.out.println(db2);

        System.out.println(BouncyDecUtils.decode(path+"private_key.pem", key, license));
        System.out.println(BouncyDecUtils.decode(path+"private_key.pem", key, db1));
        System.out.println(BouncyDecUtils.decode(path+"private_key.pem", key, db2));
    }
}
