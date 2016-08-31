package com.crawl.mq;

import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.task.DownloadTask;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by yangwang on 16-8-30.
 */
public class MessageHandler {
    public static void handler(String content){
        ThreadPoolExecutor downloadThreadExecutor = ZhiHuHttpClient.getInstance().getDownloadThreadExecutor();
        ThreadPoolExecutor parseThreadExecutor = ZhiHuHttpClient.getInstance().getParseThreadExecutor();
        while (true){
            if(downloadThreadExecutor.getQueue().size() > 50 ||
                    parseThreadExecutor.getQueue().size() > 50){
                /**
                 * 下载线程池任务队列或解析线程池任务队列size大于50
                 * 暂时不处理消息
                 */
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                /**
                 * 添加下载任务到线程池
                 */
                downloadThreadExecutor.execute(new DownloadTask(content));
                break;
            }
        }
    }
}
