package com.crawl.zhihu.task;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.crawl.cst.PageType;
import com.crawl.entity.Page;
import com.crawl.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;

/**
 * 下载网页任务
 * 并将下载成功的Page放到解析任务队列
 */
public class DownloadTask implements Runnable{
	private static Logger logger = SimpleLogger.getSimpleLogger(DownloadTask.class);
	private String url;
	private PageType pageType;
	private static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();
	public DownloadTask(String url,PageType pageType){
		this.url = url;
		this.pageType = pageType;
	}
	public void run(){
		CloseableHttpResponse response = null;
		CloseableHttpClient hc = zhiHuHttpClient.getCloseableHttpClient();
		try {
			Page page = zhiHuHttpClient.getWebPage(url);
			page.setPageType(pageType);
			int status = page.getStatusCode();

			logger.info(Thread.currentThread().getName() + " executing request " + page.getUrl() + "   status:" + status);
			if(status == HttpStatus.SC_OK){
				zhiHuHttpClient.getParseThreadExecutor().execute(new ParseTask(page));
			}
			else if(status == 502 || status == 504 || status == 500 || status == 429){
				/**
				 * 将请求继续加入线程池
                 */
				Thread.sleep(100);
				zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url,pageType));
				return ;
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException",e);
		} catch (NullPointerException e){
			logger.error("NullPointerException",e);
		}
	}
}
