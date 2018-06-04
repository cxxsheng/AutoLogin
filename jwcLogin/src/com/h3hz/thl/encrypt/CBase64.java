package com.h3hz.thl.encrypt;


import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CBase64 {

    public static byte[] b64tohex(String b){
        try {
            return Base64.decode(b.getBytes());
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static String hextob64(byte[] a){
        return Base64.encode(a);
    }
}
