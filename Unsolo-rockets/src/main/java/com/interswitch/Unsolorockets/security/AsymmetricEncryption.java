package com.interswitch.Unsolorockets.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Slf4j
@Service
@Deprecated
public class AsymmetricEncryption {
    private final Cipher cipher;

    public AsymmetricEncryption() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("RSA");
    }

    //https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
    public PrivateKey getPrivate(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public String getEncryptedPassword(String pass) throws Exception {
        String privateKeyPath = "KeyPair/privateKey";
        InputStream resourceInputStream = getClass().getClassLoader().getResourceAsStream(privateKeyPath);
        if (resourceInputStream == null) {
            throw new RuntimeException("Private key not found: " + privateKeyPath);
        }

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];

            while ((nRead = resourceInputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            byte[] keyBytes = buffer.toByteArray();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);

            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encryptedBytes = cipher.doFinal(pass.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeBase64String(encryptedBytes);
        } finally {
            resourceInputStream.close();
        }
    }


    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String newEncodedPassword = getEncryptedPassword((String) rawPassword);
            return newEncodedPassword.equals(encodedPassword);
        } catch (Exception e){
            log.error(getClass().getSimpleName(), e);
        }
        return false;
    }
}
