package com.moon.core;

import com.moon.util.OkHttpUtil;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.moon.core.Cons.default_thread_num;


/**
 * how to use it ?
 * <p>
 * 1，wrapper实现 TaskWrapper接口
 * <p>
 * 2，实现方法，生成不同类型的Task
 * <p>
 * 3，实现TaskProcess，page解析（如果有），list解析，detail解析
 * <p>
 * 4，如果有page解析，希望实现 PageWrapper，传入ListTask，则会返回分页大小，生成任务即可（此处可以从第二页开始生成任务了，因为第一页的详情任务已经加入队列中）
 */
public class Start {

    private static Logger log = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {

        try {
            Response er = OkHttpUtil.get("http://ip.tool.chinaz.com/");
            String content = er.body().string();
            Document document = Jsoup.parse(content);
            log.info(document.select("dl.IpMRig-tit").select("dd").text());
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("begin...");


        ExecutorService executor = Executors.newFixedThreadPool(default_thread_num + 1);
        Producer producer = new Producer();
        executor.execute(producer);
        for (int i = 0; i < default_thread_num; i++) {
            Consumer consumer = new Consumer();
            executor.execute(consumer);
        }


    }


}
