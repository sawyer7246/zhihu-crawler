package com.crawl.zhihu.task;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawl.cache.UserCache;
import com.crawl.config.Config;
import com.crawl.dao.ConnectionManage;
import com.crawl.dao.ZhiHuDAO;
import com.crawl.entity.Item;
import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.ZhiHuItemPageParser;
import com.crawl.parser.zhihu.ZhiHuNewUserIndexDetailPageParser;
import com.crawl.parser.zhihu.ZhiHuUserFollowingListPageParser;
import com.crawl.parser.zhihu.ZhiHuUserIndexDetailPageParser;
import com.crawl.util.Md5Util;
import com.crawl.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;

/**
 * Created by Administrator on 2016/8/24 0024.
 * 解析网页任务
 */
public class ParseTask implements Runnable {
    private static Logger logger = SimpleLogger.getSimpleLogger(ParseTask.class);
    private Page page; 
    private static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();
    /**
     * 统计用户数量
     */
    public static AtomicInteger parseUserCount = new AtomicInteger(0);
    public static volatile boolean isStopDownload = false;
    public ParseTask(Page page){
        this.page = page;
    }
    @Override
    public void run() {
    	//根据不同page类型来解析
    	switch(page.getPageType()){
    	case Activities:
    		parseAct();
    		break;
    	case following:
    		parse();
    		break;
    	default:break;
    	}
    }
    
    /**
     * 爬取用户的Parse
     */
    private void parseAct(){ 
    	ZhiHuItemPageParser parser = ZhiHuItemPageParser.getInstance();
    	parser.parse(page);
    	//取下一个User
    	User u = null;
		try {
			u = UserCache.consume();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	String url = u.getUrl()+"/activities";
    	handleUrl(url);
    }
    
    //Unicode转中文
    public String convert(String utfString){  
        StringBuilder sb = new StringBuilder();  
        logger.info("+++++++++++++++++++++++++++");
        logger.info(utfString);
        logger.info("+++++++++++++++++++++++++++");
        int i = -1;  
        int pos = 0;  
          
        while((i=utfString.indexOf("\\u", pos)) != -1){  
            sb.append(utfString.substring(pos, i));  
            if(i+5 < utfString.length()){  
                pos = i+6;  
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));  
            }  
        }  
        sb.append(utfString.substring(pos));
        return sb.toString();  
    }  
    /**
     * 爬取用户的Parse
     */
    private void parse(){ 
//    	Page followingContent = zhiHuHttpClient.getWebPage(url);

    	//解析关注列表
    	String followingContentStr = convert(page.getHtml());
    	logger.info("===============");
    	logger.info(followingContentStr);
    	logger.info("===============");
    	JSONObject jsonObj = new JSONObject(followingContentStr);
    	
    	//获取pageing 入栈 并跳转url
    	JSONObject pagingJson = jsonObj.getJSONObject("paging");
    	Boolean is_end = pagingJson.getBoolean("is_end");
    	Boolean is_start = pagingJson.getBoolean("is_start");
    	String nextURL = pagingJson.getString("next");
    	String previousURL = pagingJson.getString("previous");
    	int totals = pagingJson.getInt("totals");
    	
    	List<User> list = parseFollowerJson(jsonObj); 
		for(User u : list){
			ZhiHuDAO.insetToDB(u);
			try {
				UserCache.produce(u);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(totals!=UserCache.set.size()){
				logger.warn("关注人数发生变化:原先"+UserCache.set.size()+"，现在:"+totals);
	    		if(totals<UserCache.set.size()){
	    			UserCache.setNew.add(u);
	    			//取关了谁
	    			if(is_end){
	    				Iterator<User> cacheIt = UserCache.set.iterator();
	    				while(cacheIt.hasNext()){
	    					User cacheUser = cacheIt.next();
	    					if(!UserCache.setNew.contains(cacheUser)){
	    						logger.warn("========================");
	    						logger.warn("取关了:"+cacheUser.getUsername()+",地址:"+cacheUser.getUrl());
	    						logger.warn("========================");
	    					}
	    				}
	    				UserCache.set = UserCache.setNew.clone();
	    			}
	    		}else{
	    			//关注了谁
		    		logger.warn("========================");
		    		logger.warn("关注人数发生变化:原先"+UserCache.set.size()+"，现在:"+totals);
		    		logger.warn("========================");
	    			
	    		}
	    	}
		}
    	
    	if(is_end){
    		handleUrl(previousURL);
    	}else {
    		handleUrl(nextURL);
    	}
    }
    
    public List<User> parseFollowerJson(JSONObject jsonObj){
    	List<User> list = new ArrayList<User>();
    	JSONArray dataArray = jsonObj.getJSONArray("data");
    	Iterator<Object> it = dataArray.iterator();
    	while(it.hasNext()){
    		JSONObject dataObj = (JSONObject) it.next();
    		User u = new User();
    		////位置
    		u.setLocation("");
    		////行业
    		u.setBusiness("");
    		////性别
    		u.setSex("");
    		////企业
    		u.setEmployment("");
    		////企业职位
    		u.setPosition("");
    		////教育
    		u.setEducation("");
    		////用户名
    		u.setUsername(dataObj.getString("name"));
    		////用户首页url
    		u.setUrl("https://www.zhihu.com/people/"+dataObj.getString("url_token"));
    		////答案赞同数
    		u.setAgrees(0);
    		////感谢数
    		u.setThanks(0);
    		////提问数
    		u.setAsks(0);
    		////回答数
    		u.setAnswers(dataObj.getInt("answer_count"));
    		////文章数
    		u.setPosts(dataObj.getInt("articles_count"));
    		////关注人数
    		u.setFollowees(0);
    		////粉丝数量
    		u.setFollowers(dataObj.getInt("follower_count"));
    		//// hashId 用户唯一标识
    		u.setHashId(dataObj.getString("id"));
    		
    		list.add(u);
    	}
    	return list;
    }
    
    public String formatUserFolloweesUrl(int offset, String userHashId){
        String url = "https://www.zhihu.com/node/ProfileFolloweesListV2?params={%22offset%22:" + offset + ",%22order_by%22:%22created%22,%22hash_id%22:%22" + userHashId + "%22}";
        url = url.replaceAll("[{]", "%7B").replaceAll("[}]", "%7D").replaceAll(" ", "%20");
        return url;
    }
    
    private void handleUrl(String url){
        if(!Config.dbEnable){
            zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url,page.getPageType()));
            return ;
        }
//        String md5Url = Md5Util.Convert2Md5(url);
//        boolean isRepeat = ZhiHuDAO.insertHref(md5Url);
        if((!zhiHuHttpClient.getDownloadThreadExecutor().isShutdown() &&
                        zhiHuHttpClient.getDownloadThreadExecutor().getQueue().size() < 30)){
            /**
             * 防止互相等待，导致死锁
             */
            zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url,page.getPageType()));
        }
    }
}
