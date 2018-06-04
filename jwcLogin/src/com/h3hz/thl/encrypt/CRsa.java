package com.h3hz.thl.encrypt;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CRsa {
    public static RSAPublicKey getPublicKey(byte[] modulus, byte[] publicExponent){
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
                bigIntPrivateExponent);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            String p = new String(CBase64.hextob64(publicKey.getEncoded()));
            return publicKey;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static byte[] encrypt(RSAPublicKey key, String input){
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
