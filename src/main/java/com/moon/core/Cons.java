package com.moon.core;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Cons {

    public static final int default_thread_num = 5;


    /**
     * 阻塞队列的最大长度，默认 10 * 1000
     */
    public static int queueMaxLength = 1000000;

    public static volatile BlockingQueue<DiscoveryTask> taskQueue = new LinkedBlockingQueue<>(Cons.queueMaxLength);

    public static String task_duplicate = "discovery";


}
