package com.moon.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer implements Runnable {

    private static Logger log = LoggerFactory.getLogger(Producer.class);

    public static void add(DiscoveryTask task) {
        try {
            Cons.taskQueue.put(task);

        } catch (Exception e) {
            log.error("add task error", e);
        }


    }


    @Override
    public void run() {

        try {

            log.debug("task add finished");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("product error", e);
        }


    }
}
