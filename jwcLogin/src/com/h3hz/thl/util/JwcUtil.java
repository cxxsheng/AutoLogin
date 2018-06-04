package com.h3hz.thl.util;

import com.h3hz.thl.encrypt.CBase64;
import com.h3hz.thl.encrypt.CRsa;
import org.json.JSONObject;


import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/10.
 */

public class JwcUtil {

    private static  String HOST = "211.83.241.245";
    private static String encryptPassword(String mm) {
        try {
            ResponData rd = HttpUtil.sendGet("http://"+ HOST +"/jwglxt/xtgl/login_getPublicKey.html", "time=" + System.currentTimeMillis() + "&_=" + System.currentTimeMillis(), null);
            if (rd.getRetCode()!=200)
                return null;
            String jsonData = rd.getRetData();
            JSONObject jsobj = new JSONObject(jsonData);
            String exponent = jsobj.getString("exponent");
            String modulus = jsobj.getString("modulus");
            PublicKey key = CRsa.getPublicKey(CBase64.b64tohex(modulus), CBase64.b64tohex(exponent));
            if (key != null)
                return CBase64.hextob64(CRsa.encrypt((RSAPublicKey) key, mm));
            else
                return null;
        } catch (Exception e) {
            return null;
        }
    }
    private static String getToken(String webSource){
        String regEx = "name=\"csrftoken\" value=\"\\S*\"";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(webSource);
        if (m.find( )) {
            String tokenStr = m.group(0);
            String[] res = tokenStr.split("\"");
            return res[res.length-1];
        }
        return null;
    }

    public static  String entryPage(){
        HttpUtil.clearCookie();
        Map<String,String> request = new HashMap<String, String>();
        request.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        request.put("Accept-Encoding","gzip, deflate");
        request.put("Accept-Language","zh-CN,zh;q=0.9");
        request.put("Cache-Control","max-age=0");
        request.put("Connection","keep-alive");
        request.put("Host",HOST);

        request.put("Referer","http://"+ HOST +"/jwglxt/");
        request.put("Upgrade-Insecure-Requests","1");
        request.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
       ResponData rd = HttpUtil.sendGet("http://" + HOST+ "/jwglxt/xtgl/login_slogin.html", request);
        if (rd.getRetCode() != 200)
            return null;
        return getToken(rd.getRetData());
    }

    public static  String loginPost(String username, String password, String token){
        Map<String,String> request = new HashMap<String, String>();
        request.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        request.put("Accept-Encoding","gzip, deflate");
        request.put("Accept-Language","zh-CN,zh;q=0.9");
        request.put("Cache-Control","max-age=0");
        request.put("Connection","keep-alive");
        request.put("Host",HOST);
        request.put("Origin","http://"+HOST);
        request.put("Referer","http://"+ HOST + "/jwglxt/xtgl/login_slogin.html?language=zh_CN&_t=1520830754441");
        request.put("Upgrade-Insecure-Requests","1");
        request.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");

        String cstoken = "csrftoken="+ HttpUtil.toURLEncoded(token);
        String yhm = "yhm=" + HttpUtil.toURLEncoded(username);
        String p = encryptPassword(password);
        if (p == null)
            return null;
        String mm = "mm=" + HttpUtil.toURLEncoded(p);
        String postData = cstoken + "&" + yhm + "&" + mm +"&" + mm;
        ResponData rd = HttpUtil.sendPost("http://" + HOST + "/jwglxt/xtgl/login_slogin.html", postData, request);
        while (rd.getRetCode()==302)
        {
            rd = HttpUtil.sendGet("http://" + HOST + rd.getRetData(),null);
        }
        if (rd.getRetCode()==200)
            return rd.getRetData();
        return null;
    }
    public static  String getClassTable(String username, int schoolYear, int term){
        Map<String,String> request = new HashMap<String, String>();
        request.put("Accept","*/*");
        request.put("Accept-Encoding","gzip, deflate");
        request.put("Accept-Language","zh-CN,zh;q=0.9");
        request.put("Connection","keep-alive");
        request.put("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        request.put("Host",HOST);
        request.put("Origin","http://" + HOST);
        request.put("Referer","http://" + HOST + "/jwglxt/kbcx/xskbcx_cxXskbcxIndex.html?gnmkdm=N253508&layout=default&su="+username);
        request.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        request.put("X-Requested-With","XMLHttpRequest");
        if (term==2)
            term=12;
        else
            term=3;
        String postData = "xnm="+schoolYear + "&xqm="+term;
        ResponData rd = HttpUtil.sendPost("http://"+ HOST +"/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N253508", postData ,request);
        if (rd.getRetCode() != 200)
            return null;
        return rd.getRetData();
    }





}
