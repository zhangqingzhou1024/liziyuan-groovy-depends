package com.liziyuan.groovy.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @description: http请求客户端
 * @author: zqz
 * @create: 2022-07-06 09:52
 */
public class HttpClient {

    /**
     * post请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param bodyParam    请求体参数
     * @return 原始返回结果
     */
    public static String sendPostRequest(String url, Map<String, String> headerParams, String bodyParam) {
        return sendPostRequest(url, headerParams, bodyParam, null);
    }

    /**
     * post请求
     *
     * @param url          请求地址
     * @param headerParams 请求头参数
     * @param bodyParam    请求体参数
     * @param charset      返回结果字符集
     * @return 原始返回结果
     */
    public static String sendPostRequest(String url, Map<String, String> headerParams, String bodyParam, String charset) {
        URI uri = changeUrlToUri(url);
        HttpPost httpPost = new HttpPost(uri);
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
//        HttpPost httpPost = new HttpPost(url);
        if (bodyParam != null && !"".equals(bodyParam.trim())) {
            StringEntity entity = new StringEntity(bodyParam, "UTF-8");
            httpPost.setEntity(entity);
        }
        fillHttpHeaderParam(httpPost, headerParams);
        // 请求
        return executeRequest(httpPost, charset);
    }

    /**
     * 标准的post请求，获取内层实际的数据（不含code码）
     *
     * @param url          地址
     * @param headerParams 头参数
     * @param bodyParam    body参数
     * @return 实际内层返回数据
     */
    public static String sendPostRequestReturnInnerData(String url, Map<String, String> headerParams, String bodyParam) {
        return sendPostRequestReturnInnerData(url, headerParams, bodyParam, "code", "200", "data");
    }

    /**
     * 标准的post请求，获取内层实际的数据（不含code码）
     *
     * @param url          地址
     * @param headerParams 头参数
     * @param bodyParam    body参数
     * @return 实际内层返回数据
     */
    public static String sendPostRequestReturnInnerData(String url, Map<String, String> headerParams, String bodyParam, String charset) {
        return sendPostRequestReturnInnerData(url, headerParams, bodyParam, charset, "code", "200", "data");
    }

    /**
     * 自定义post请求
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @param bodyParam    body参数
     * @param successKey   成功标识的key
     * @param successValue 成功标识的值
     * @param dataKey      内层实际数据的key
     * @return 实际内层返回数据
     */
    public static String sendPostRequestReturnInnerData(String url, Map<String, String> headerParams, String bodyParam, String successKey, String successValue, String dataKey) {
        return sendPostRequestReturnInnerData(url, headerParams, bodyParam, null, successKey, successValue, dataKey);
    }

    /**
     * 自定义post请求
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @param bodyParam    body参数
     * @param charset      返回结果的字符集
     * @param successKey   成功标识的key
     * @param successValue 成功标识的值
     * @param dataKey      内层实际数据的key
     * @return 实际内层返回数据
     */
    public static String sendPostRequestReturnInnerData(String url, Map<String, String> headerParams, String bodyParam, String charset, String successKey, String successValue, String dataKey) {
        String outData = sendPostRequest(url, headerParams, bodyParam, charset);
        return getInnerData(outData, successKey, successValue, dataKey);
    }

    /**
     * get请求
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @return 原始返回结果
     */
    public static String sendGetRequest(String url, Map<String, String> headerParams) {
        return sendGetRequest(url, headerParams, null);
    }

    /**
     * get请求（自定义返回数据集）
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @param charset      返回结果的字符集编码
     * @return 原始返回结果
     */
    public static String sendGetRequest(String url, Map<String, String> headerParams, String charset) {
        URI uri = changeUrlToUri(url);
        HttpGet httpGet = new HttpGet(uri);
        fillHttpHeaderParam(httpGet, headerParams);
        return executeRequest(httpGet, charset);
    }

    /**
     * 标准的get请求
     * 注：标准是指外层结构为{"code": 200, "data": "***", "message": "***"}的返回结果，正常情况下只将data返回
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @return 内层返回结果
     */
    public static String sendGetRequestReturnInnerData(String url, Map<String, String> headerParams) {
        return sendGetRequestReturnInnerData(url, headerParams, "code", "200", "data");
    }

    /**
     * 标准的get请求
     * 注：标准是指外层结构为{"code": 200, "data": "***", "message": "***"}的返回结果，正常情况下只将data返回
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @param charset      返回结果的字符集编码
     * @return 内层返回结果
     */
    public static String sendGetRequestReturnInnerData(String url, Map<String, String> headerParams, String charset) {
        return sendGetRequestReturnInnerData(url, headerParams, charset, "code", "200", "data");
    }

    /**
     * 自定义get请求
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @param successKey   成功标识的key
     * @param successValue 成功标识的值
     * @param dataKey      内层实际数据的key
     * @return 内层返回结果
     */
    public static String sendGetRequestReturnInnerData(String url, Map<String, String> headerParams, String successKey, String successValue, String dataKey) {
        return sendGetRequestReturnInnerData(url, headerParams, null, successKey, successValue, dataKey);
    }

    /**
     * 自定义get请求
     *
     * @param url          请求地址
     * @param headerParams 头部参数
     * @param charset      返回结果的字符集编码
     * @param successKey   成功标识的key
     * @param successValue 成功标识的值
     * @param dataKey      内层实际数据的key
     * @return 内层返回结果
     */
    public static String sendGetRequestReturnInnerData(String url, Map<String, String> headerParams, String charset, String successKey, String successValue, String dataKey) {
        String outData = sendGetRequest(url, headerParams, charset);
        return getInnerData(outData, successKey, successValue, dataKey);
    }

    /**
     * 获取返回结果的内层实际数据
     *
     * @param outResult    外层结果
     * @param successKey   成功标识的key
     * @param successValue 成功标识的值
     * @param dataKey      内层实际数据的key
     * @return 内层实际数据
     */
    private static String getInnerData(String outResult, String successKey, String successValue, String dataKey) {
        if (outResult == null || "".equals(outResult.trim())) {
            return null;
        }
        JSONObject result;
        try {
            result = JSONObject.parseObject(outResult);
        } catch (Exception e) {
            throw new RuntimeException("HttpClient getInnerData error: " + outResult, e);
        }
        if (!successValue.equals(result.getString(successKey))) {
            throw new RuntimeException("HttpClient getInnerData return error code: " + outResult);
        }
        return result.getString(dataKey);
    }

    /**
     * 填充头部参数
     *
     * @param httpRequest  请求客户端
     * @param headerParams 头部参数
     */
    private static void fillHttpHeaderParam(HttpRequestBase httpRequest, Map<String, String> headerParams) {
        httpRequest.setHeader("Content-Type", "application/json;charset=utf8");
        httpRequest.setHeader("Accept", "application/json");
        if (headerParams != null && headerParams.size() > 0) {
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 执行请求
     *
     * @param request     请求客户端
     * @param charsetName 数据集名称
     * @return 返回结果
     */
    private static String executeRequest(HttpUriRequest request, String charsetName) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null) {
                return null;
            }
            Charset charset;
            if (charsetName != null && !charsetName.trim().equals("")) {
                charset = Charset.forName(charsetName);
            } else {
                ContentType contentType = ContentType.get(responseEntity);
                if (contentType != null && contentType.getCharset() != null) {
                    charset = contentType.getCharset();
                } else {
                    charset = Charset.forName("utf-8");
                }
            }
            return EntityUtils.toString(responseEntity, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static URI changeUrlToUri(String url) {
        String schema = "http";
        if (url.startsWith("https")) {
            schema = "https";
        }
        URI uri;
        try {
            URL urlNew = new URL(url);
            uri = new URI(schema, urlNew.getUserInfo(), urlNew.getHost(), urlNew.getPort(), urlNew.getPath(), urlNew.getQuery(), null);
        } catch (Exception e) {
            throw new RuntimeException("build get request URI error: " + e.getMessage());
        }
        return uri;
    }
}
