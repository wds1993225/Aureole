package com.moon.util;

import okhttp3.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wds on 2017/10/19.
 *
 * @author moon
 */
public class OkHttpUtil {

    public static final long DEFAULT_READ_TIMEOUT_MILLIS = 1 * 1000;          //默认 读 超时时间
    public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 1 * 1000;         //默认 写 超时时间
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 1 * 1000;       //默认 连接 超时时间


    private static OkHttpClient okHttpClient;
    private static OkHttpClient.Builder clientBuilder;


    /**
     * 执行一个GET请求
     * 返回失败返回null
     */
    public static Response get(String url) {
        return get(url, null);
    }

    public static Response get(String url, Map<String, String> headers) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(getRequest(url, headers)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }

    /**
     * 获取一个POST请求的call
     *
     * @param url               请求的地址
     * @param requestParameters 请求参数
     */
    public static Response post(String url, Map<String, String> requestParameters) {
        return post(url, requestParameters, null);
    }

    /**
     * 获取一个POST请求的call
     *
     * @param headers 带有请求头的post请求
     */
    public static Response post(String url, Map<String, String> requestParameters, Map<String, String> headers) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(postRequest(url, requestParameters, headers)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }


    /**
     * 获取一个POST请求的call
     */
    public static Response postJson(String url, String jsonData) {
        return postJson(url, jsonData, null);
    }

    public static Response postJson(String url, String jsonData, Map<String, String> headers) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(postJsonRequest(url, jsonData, headers)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }

    /**
     * 获取一个put请求的call
     */
    public static Response put(String url, Map<String, String> requestParameters) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(putRequest(url, requestParameters)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }

    /**
     * 获取一个put请求的call
     */
    public static Response putJson(String url, String jsonData) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(putJsonRequest(url, jsonData)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }


    /**
     * 获取一个GET类型的请求
     *
     * @param url 请求的url
     */
    public static Request getRequest(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        setHeaderAndUA(builder, headers);
        return builder.build();
    }

    /**
     * 获取一个POST的请求
     *
     * @param url               请求的url
     * @param requestParameters 请求参数，Map类型
     * @param headers           添加请求头
     */
    public static Request postRequest(String url, Map<String, String> requestParameters, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(addParamToBuilder(requestParameters).build());
        setHeaderAndUA(builder, headers);
        return builder.build();
    }

    /**
     * 获取一个POST的请求
     *
     * @param url      请求的url
     * @param jsonData 请求参数，Json类型
     * @param headers  请求头
     */
    public static Request postJsonRequest(String url, String jsonData, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //builder.addHeader("content-type", "application/json");
        builder.post(RequestBody.create(MediaType.parse("application/json"), jsonData));
        setHeaderAndUA(builder, headers);
        return builder.build();
    }

    /**
     * 上传文件
     *
     * @param url      上传的路径
     * @param fileName 上传的文件名
     * @param filePath 上传的文件路径
     */
    public static Response postMedia(String url, String fileName, String filePath, String param) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(postMediaRequest(url, fileName, filePath, param)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }


    /**
     * 生成文件类型的request
     *
     * @param url      上传的路径
     * @param fileName 上传的文件名
     * @param filePath 上传的文件路径
     * @param param    上传的参数
     */
    public static Request postMediaRequest(String url, String fileName, String filePath, String param) {

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        File file = new File(filePath);
        RequestBody bodyFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = multipartBuilder
                .setType(MultipartBody.FORM)
                .addFormDataPart(param, fileName, bodyFile)
                .build();


        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(requestBody);
        return builder.build();
    }

    /**
     * 获取一个PUT的请求
     *
     * @param url               请求的url
     * @param requestParameters 请求参数，Map类型
     */
    public static Request putRequest(String url, Map<String, String> requestParameters) {
        return new Request.Builder()
                .url(url)
                .put(addParamToBuilder(requestParameters).build())
                .build();
    }

    /**
     * 获取一个PUT的请求
     *
     * @param url      请求的url
     * @param jsonData 请求参数，String类型
     */
    public static Request putJsonRequest(String url, String jsonData) {
        return new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json;charset:utf-8")
                .put(RequestBody.create(MediaType.parse("application/json;charset:utf-8"), jsonData))
                .build();
    }


    /**
     * 获取一个OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(null);
    }

    public static OkHttpClient getOkHttpClient(Map<String, String> cookies) {
        if (okHttpClient == null) {
            okHttpClient = getClientBuilder(cookies).build();
        }
        return okHttpClient;
    }

    /**
     * 获取client的builder
     *
     * @param cookies 要转入的cookies
     */
    public static OkHttpClient.Builder getClientBuilder(Map<String, String> cookies) {
        if (clientBuilder == null) {
            clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.SECONDS);
            if (cookies != null) {
                clientBuilder.cookieJar(setCookie(cookies));
            }
        }

        return clientBuilder;
    }

    /**
     * 将Map转换为OKHttp需要的FormBody
     *
     * @param requestParamMap 需要的请求参数 ，Map类型
     */
    private static FormBody.Builder addParamToBuilder(Map<String, String> requestParamMap) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (requestParamMap != null) {
            for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
                if (entry != null) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key != null && value != null) {
                        formBody.add(key, value);
                    }
                }
            }
        }
        return formBody;
    }

    /**
     * 将map转换为okhttp3需要的header
     */
    private static Headers setHeaders(Map<String, String> headersParams) {
        Headers.Builder builder = new Headers.Builder();
        if (headersParams != null) {
            for (Map.Entry<String, String> entry : headersParams.entrySet()) {
                if (entry != null) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        }
        return builder.build();
    }


    /**
     * 管理cookie
     */
    private static CookieJar setCookie(Map<String, String> cookies) {
        final List<Cookie> list = new ArrayList<Cookie>();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            list.add(new Cookie.Builder().name(entry.getKey()).value(entry.getValue()).domain("").build());
        }
        return new CookieJar() {

            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            }

            public List<Cookie> loadForRequest(HttpUrl url) {
                return list;
            }
        };
    }


    public static boolean isUseUA = false;       //是否使用UA，这里实现的不好，网络请求工具和爬虫配置耦合

    /**
     * 设置请求头和UA
     */
    private static void setHeaderAndUA(Request.Builder builder, Map<String, String> header) {

        if (header != null) {

            builder.headers(setHeaders(header));

        }

    }


    /**
     * 关闭response，防止内存泄漏
     */
    private static void closeResponse(Response response) {
        if (response != null) {
            response.close();
        }
    }

}
