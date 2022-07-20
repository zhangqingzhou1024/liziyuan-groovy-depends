package com.liziyuan.groovy.utils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @description: https接口访问客户端
 * @author: zqz
 * @create: 2022-06-29 10:50
 */
public class HttpsClient {
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String GET = "GET";
    public static final String DELETE = "DELETE";
    /**
     * 默认 GET 请求
     */
    public static String sendHtpps(String url, Map<String, String> headerParams, String method, String jsonBodyStr) {
        return sendHtpps(url, headerParams, method, jsonBodyStr, null);
    }

    public static String sendHtpps(String url, Map<String, String> headerParams, String method, String jsonBodyStr, String charset) {
        trustAllHosts();
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        OutputStream os = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            String currentMethod = (method == null || "".equals(method.trim())) ? GET : method;
            conn.setRequestMethod(currentMethod); // POST GET PUT DELETE
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (headerParams != null && headerParams.size() > 0) {
                for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            // body体传递
            conn.setDoOutput(false);
            if (POST.equals(method) || PUT.equals(method)) {
                conn.setDoOutput(true);
                if (jsonBodyStr != null && !"".equals(jsonBodyStr.trim())){
                    os = conn.getOutputStream();
                    byte[] input = jsonBodyStr.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            // 定义 BufferedReader输入流来读取URL的响应
            InputStreamReader inputStreamReader;
            if (charset != null && !charset.trim().equals("")) {
                inputStreamReader = new InputStreamReader(conn.getInputStream(), Charset.forName(charset));
            } else {
                inputStreamReader = new InputStreamReader(conn.getInputStream());
            }
            reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null){
                    reader.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    private static void trustAllHosts() {
        X509TrustManager trustManager = createX509TrustManager();
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = trustManager;
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static X509TrustManager createX509TrustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        };
    }
}
