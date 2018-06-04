package com.h3hz.thl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by Administrator on 2018/3/8.
 */

public class HttpUtil {
    private final static String GZIPCODING = "gzip";
    private  static String cookie = null;
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param params
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param request 头请求
     *
     * @return URL 所代表远程资源的响应结果
     */
    public static ResponData sendGet(String url,String params, Map<String,String> request) {
        return sendGet(url + "?" + params, request);
    }
    public static ResponData sendGet(String urlNameString, Map<String,String> request) {
        BufferedReader reader = null;
        String response="";
        int code = 0;
        try {
            URL httpUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            //关闭重定向
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(3000);

            conn.setRequestMethod("GET");
            if (cookie!=null)
                conn.setRequestProperty("Cookie", cookie);// 给服务器送登录后的cookie
            // 设置通用的请求属性
            if (request!=null){
                for (Map.Entry<String,String> r: request.entrySet())
                    conn.setRequestProperty(r.getKey(),r.getValue());
            }



            conn.connect();
            code = conn.getResponseCode();
            if (code == 302)
                return new ResponData(code, conn.getHeaderField("Location"));
            // 取到所用的Cookie并且保存
            Map<String, List<String>> reponseHeader =conn.getHeaderFields();
            List<String> cookiesList = reponseHeader.get("Set-Cookie");
            if (cookiesList!=null) {
                String WholeCookie = "";
                for (String setCookie:cookiesList)
                {
                    String cookies[] = setCookie.split(";");
                    if (cookies[0].contains("UqZBpD3n3iXPAw1X="))
                    {
                        WholeCookie += cookies[0];
                        WholeCookie += ";";
                    }
                    else  if (cookies[0].contains("JSESSIONID="))
                    {
                        WholeCookie += cookies[0];
                        WholeCookie += ";";
                    }
                }
                if (WholeCookie.endsWith(";"))
                    setCookie(WholeCookie.substring(0, WholeCookie.length()-1));
            }
            String encoding = conn.getContentEncoding();
            InputStream ism = conn.getInputStream();
            if (encoding != null && encoding.contains("gzip")) {//首先判断服务器返回的数据是否支持gzip压缩，
                //如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
                ism = new GZIPInputStream(conn.getInputStream());
            }

            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    ism));
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    ism));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try{
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return  new ResponData(code ,response);
    }
    /**
     * @作用 使用urlconnection
     * @param url
     * @param params
     * @param request 头请求
     * @return
     * @throws IOException
     */



   static public ResponData sendPost(String url,String params, Map<String, String> request){
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
       int code = 0;
       try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            //关闭重定向
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(3000);

            if (cookie!=null)
                conn.setRequestProperty("Cookie", cookie);// 给服务器送登录后的cookie

            if (request!=null){
                for (Map.Entry<String,String> r: request.entrySet())
                    conn.setRequestProperty(r.getKey(),r.getValue());
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(params);
            out.flush();
            code = conn.getResponseCode();
            if (code == 302)
               return new ResponData(code, conn.getHeaderField("Location"));
            // 取到所用的Cookie并且保存
            Map<String, List<String>> reponseHeader =conn.getHeaderFields();
            List<String> cookiesList = reponseHeader.get("Set-Cookie");
            if (cookiesList!=null) {
                String WholeCookie = "";
                for (String setCookie:cookiesList)
                {
                    String cookies[] = setCookie.split(";");
                    if (cookies[0].contains("UqZBpD3n3iXPAw1X="))
                    {
                        WholeCookie += cookies[0];
                        WholeCookie += ";";
                    }
                    else  if (cookies[0].contains("JSESSIONID="))
                    {
                        WholeCookie += cookies[0];
                        WholeCookie += ";";
                    }
                }
                if (WholeCookie.endsWith(";"))
                    setCookie(WholeCookie.substring(0, WholeCookie.length()-1));
            }
            String encoding = conn.getContentEncoding();
            InputStream ism = conn.getInputStream();
            if (encoding != null && encoding.contains("gzip")) {//首先判断服务器返回的数据是否支持gzip压缩，
                //如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
                ism = new GZIPInputStream(conn.getInputStream());
            }

            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    ism));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

       return  new ResponData(code ,response);
   }

    static private void setCookie(String c){
       cookie = c;
    }
    static public String getCookie(){
       return cookie;
    }
    static public void clearCookie(){
        cookie = null;
    }

    public static String toURLEncoded(String paramString) {
        if (paramString == null || "".equals(paramString)) {
            return null;
        }

        try
        {
            String str = URLEncoder.encode(paramString, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
            return null;
        }

    }
    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return null;
        }

        try
        {
            String str = URLDecoder.decode(paramString, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }

        return null;
    }

}