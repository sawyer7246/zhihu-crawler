package com.crawl.mq.handler;

import com.crawl.mq.QueueName;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.task.DownloadTask;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by yangwang on 16-8-30.
 */
public interface MessageHandler{
    public void handler(String messageContent);
}
