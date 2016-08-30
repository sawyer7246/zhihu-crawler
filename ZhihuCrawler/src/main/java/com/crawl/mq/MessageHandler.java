package com.crawl.mq;

import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.task.DownloadTask;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by yangwang on 16-8-30.
 */
public class MessageHandler {
    public static void handler(String content){
        ThreadPoolExecutor tpe = ZhiHuHttpClient.getInstance().getDownloadThreadExecutor();
        while (true){
            /**
             * 添加下载任务到线程池
             */
            tpe.execute(new DownloadTask(content));
            if(tpe.getQueue().size() > 50){
                /**
                 * 下载线程池任务队列size大于50
                 * 暂停接收消息
                 */
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
        }
    }
}
