package com.moon.core;

import com.alibaba.fastjson.JSON;
import com.moon.util.OkHttpUtil;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 任务执行器
 */
public class TaskExe {

    private static Logger log = LoggerFactory.getLogger(TaskExe.class);

    public static String exe(DiscoveryTask task) {

        if (task == null) {
            log.warn("task is null ");
            return null;
        }

        String content = null;
        try {
            if (task.getHttpMethod() == DiscoveryTask.HttpMethod.GET) {

                Response response = null;
                String params = "";
                if (task.getParams() != null) {
                    params = convertParams(task.getParams());
                }

                if (task.getHeaders() != null) {
                    response = OkHttpUtil.get(task.getUrl() + params, task.getHeaders());
                } else {
                    response = OkHttpUtil.get(task.getUrl() + params);
                }
                if (response == null) {
                    return null;
                }
                content = response.body().string();

            } else if (task.getHttpMethod() == DiscoveryTask.HttpMethod.POST) {
                Response response = null;
                if (task.getHeaders() != null) {
                    response = OkHttpUtil.post(task.getUrl(), task.getParams(), task.getHeaders());
                } else {
                    response = OkHttpUtil.post(task.getUrl(), task.getParams());
                }
                if (response == null) {
                    return null;
                }
                content = response.body().string();
            }

        } catch (Exception e) {
            log.error("download error , task is {}", JSON.toJSONString(task), e);
            return null;
        }

        TaskProcessor processor = task.getTaskProcessor();

        if (processor == null) {
            throw new RuntimeException("task extend method is null");
        }

        if (processor instanceof ListProcessor) {
            ((ListProcessor) processor).process(content);
            log.info("list task process , queue size is {}", Cons.taskQueue.size());
        }

        if (processor instanceof DetailProcessor) {
            Object o = ((DetailProcessor) processor).process(content);
            log.info("detail task process , queue size is {}", Cons.taskQueue.size());
            boolean isInsert = ((DetailProcessor) processor).upload(o);
            if (!isInsert) {
                log.debug("upload failed , task is {}", task.getTaskId());
                return null;
            } else {
                log.info("insert success  , file is {}", task.getExtra());
            }
            if (task.isUseDuplicate()) {
                //RedisUtil.setADD(Cons.task_duplicate, task.getMd5());
            }
        }
        log.info("queue size is {}", Cons.taskQueue.size());
        return content;
    }

    /**
     * 用于将map类型的请求参数转换成字符串类型的请求参数
     * <p>
     * 转换结果为 ?key=value&key=value&key=value
     */

    private static String convertParams(Map<String, String> paramsMap) {

        if (paramsMap == null) {
            return "";
        }

        String params = "?";

        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params = params + entry.getKey() + "=" + entry.getValue() + "&";
        }
        return params.substring(0, params.length() - 1);
    }
}
