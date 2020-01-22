package com.moon.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分页工具
 * <p>
 * 使用此工具后，第一页的列表数据会被加入队列中，所以只需要从第二页开始即可
 */
public class PageWrapper {

    private static Logger log = LoggerFactory.getLogger(PageWrapper.class);


    /**
     * 通过列表任务返回一个分页
     *
     * @param task 列表任务，需要包含任务处理器
     */
    public static int getPageNum(DiscoveryTask task, int pageSize) {
        TaskProcessor processor = task.getTaskProcessor();
        if (processor == null) {
            log.error("processor is null");
            throw new RuntimeException("processor is null");
        }
        if (processor instanceof ListProcessor) {

            String content = TaskExe.exe(task);
            String totalRaw = ((ListProcessor) processor).page(content);
            return getPaging(totalRaw, pageSize);

        } else {

            log.warn("processor error .");
            throw new RuntimeException("processor is not list processor");
        }

    }


    /**
     * 获取一个分页
     *
     * @param totalRow 总条数
     * @return 总页数
     */
    private static int getPaging(String totalRow, int pageSize) {

        int totalRowInt = 0;

        try {
            totalRowInt = Integer.valueOf(totalRow);
        } catch (Exception e) {
            log.error("convert total raw error , total raw is {}", totalRow);
            return 0;
        }

        if ((totalRowInt % pageSize) == 0) {
            return totalRowInt / pageSize;
        } else {
            return (totalRowInt / pageSize) + 1;
        }
    }
}
