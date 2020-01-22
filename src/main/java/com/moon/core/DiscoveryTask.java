package com.moon.core;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DiscoveryTask implements Serializable {

    private String url;                     //请求链接
    private HttpMethod httpMethod;              //请求方法
    private Map<String, String> params;     //请求参数
    private Map<String, String> headers;    //请求头
    private Map<String, String> extra;       //额外参数

    private SaveMethod saveMethod;          //保存方式

    private boolean isUseDuplicate = false;

    private String taskId = UUID.randomUUID().toString();       //任务的唯一id


    private TaskProcessor taskProcessor;

    private Object processResult;   //解析结果


    /**
     * 去重的key
     */
   /* public String getMd5() {
        return DigestUtils.md5Hex(JSON.toJSONString(this));
    }*/

    /**
     * 请求方式
     */
    public enum HttpMethod {

        GET("get"), POST("post"), PUT("put"), DELETE("delete");

        private final String text;

        HttpMethod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * 存储参数
     */
    public enum SaveMethod {

        FILE("file"), MYSQL("mysql"), MONGO("mongo"), ES("es");

        private final String text;

        SaveMethod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    public DiscoveryTask() {
        httpMethod = HttpMethod.GET;
    }


    public DiscoveryTask(Builder builder) {

        if (StringUtils.isBlank(builder.url)) {
            throw new IllegalStateException("url is null");
        }
        this.url = builder.url;


        if (builder.httpMethod == null) {
            this.httpMethod = HttpMethod.GET;
        } else {
            this.httpMethod = builder.httpMethod;
        }


        this.headers = builder.headers;
        this.params = builder.params;
        this.extra = builder.extra;
        this.isUseDuplicate = builder.isUseDuplicate;

    }

    public static class Builder {

        String url;                     //请求链接
        HttpMethod httpMethod;              //请求方法
        Map<String, String> params = new HashMap<>();     //请求参数
        Map<String, String> headers = new HashMap<>();    //请求头
        Map<String, String> extra;       //额外参数


        private boolean isUseDuplicate = false;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder param(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder params(HashMap<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder addHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder extra(String key, String value) {
            extra.put(key, value);
            return this;
        }

        public Builder addExtra(HashMap<String, String> extra) {
            this.extra = extra;
            return this;
        }


        public Builder duplicate(boolean isUse) {
            this.isUseDuplicate = isUse;
            return this;
        }

        public DiscoveryTask build() {

            return new DiscoveryTask(this);
        }

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public SaveMethod getSaveMethod() {
        return saveMethod;
    }

    public void setSaveMethod(SaveMethod saveMethod) {
        this.saveMethod = saveMethod;
    }

    public boolean isUseDuplicate() {
        return isUseDuplicate;
    }

    public void setUseDuplicate(boolean useDuplicate) {
        isUseDuplicate = useDuplicate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskProcessor getTaskProcessor() {
        return taskProcessor;
    }

    public void setTaskProcessor(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
    }

    public Object getProcessResult() {
        return processResult;
    }

    public void setProcessResult(Object processResult) {
        this.processResult = processResult;
    }
}
