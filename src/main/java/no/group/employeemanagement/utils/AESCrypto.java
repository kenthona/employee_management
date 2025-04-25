package no.group.employeemanagement.utils;
import lombok.extern.slf4j.Slf4j;
import no.group.employeemanagement.exceptions.ProcessException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Slf4j
@Component
public class AESCrypto {
    private static final String COMPONENT = "000000000000000楳-" +
            "000000000000000瑍-" +
            "000000000000000㑳-" +
            "000000000000000琳-" +
            "000000000000000牋-" +
            "000000000000000㍹-" +
            "000000000000000䈴-" +
            "000000000000000猱-";

    private static String MASTER_KEY;
    private static final String ALG = "AES/ECB/PKCS5Padding ";
    private static final String ALG_2 = "AES/CBC/PKCS5Padding ";

    private static final String ALGSEC = "AES";
    public static final String DEFAULT_SOURCE_SYSTEM = "EMPLOY-MGMT";
    public static final String ENC_INVALID_CIPHER = "0032";

    public static String encrypt(String value) throws ProcessException {
        if (MASTER_KEY == null) {
            try {
                getKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encrypt(value, MASTER_KEY);
    }

    public static String encryptSalt(String value, String salt) throws ProcessException {
        if (MASTER_KEY == null) {
            try {
                getKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encrypt(value, MASTER_KEY + salt);
    }

    public static String decrypt(String value) throws ProcessException {
        if (MASTER_KEY == null) {
            try {
                getKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return decrypt(value, MASTER_KEY);
    }

    public static String decryptSalt(String value, String salt) throws ProcessException {
        if (MASTER_KEY == null) {
            try {
                getKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return decrypt(value, MASTER_KEY + salt);
    }

    public static String encrypt(String value, String key) throws ProcessException {
        if (value == null || value.isEmpty()) return "";
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGSEC);

            Cipher cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new ProcessException(DEFAULT_SOURCE_SYSTEM, ENC_INVALID_CIPHER);
        }
    }

    public static String decrypt(String encrypted, String key) throws ProcessException {
        if (encrypted == null || encrypted.isEmpty()) return "";
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGSEC);

            Cipher cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            throw new ProcessException(DEFAULT_SOURCE_SYSTEM, ENC_INVALID_CIPHER);
        }
    }

    public static String encrypt(String value, String key, byte[] iv) throws ProcessException {
        if (value == null || value.isEmpty()) return "";
        try {
            SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGSEC);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALG_2);
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new ProcessException(DEFAULT_SOURCE_SYSTEM, ENC_INVALID_CIPHER);
        }
    }

    public static String decrypt(String encrypted, String key, byte[] iv) throws ProcessException {
        if (encrypted == null || encrypted.isEmpty()) return "";
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGSEC);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALG_2);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            log.error("decrypt error log aesC: " + ExceptionUtils.getStackTrace(e));
            throw new ProcessException(DEFAULT_SOURCE_SYSTEM, ENC_INVALID_CIPHER);
        }
    }


    public static void getKey() {
        StringBuilder sb = new StringBuilder();

        final String[] component = COMPONENT.split("-");
        for (int a = 0; a < component.length; a++) {
            sb.append(component[a].charAt(component[a].length() - 1));
        }
        String rawString = sb.toString();
        byte[] bytes = rawString.getBytes(StandardCharsets.UTF_16);
        MASTER_KEY = new String(bytes, StandardCharsets.UTF_8).substring(2);
    }
}
