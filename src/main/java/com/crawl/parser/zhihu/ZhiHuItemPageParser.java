package com.crawl.parser.zhihu;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.crawl.cst.ItemType;
import com.crawl.entity.Item;
import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.entity.items.CollectItem;
import com.crawl.entity.items.PokeItem;
import com.crawl.parser.ItemPageParser;
import com.crawl.util.DateUtil;
import com.crawl.util.SimpleLogger;

/**
 * Created by wy on 11/28/2016.
 * https://www.zhihu.com/people/wo-yan-chen-mo/following
 * 新版followess页面解析出用户详细信息
 */
public class ZhiHuItemPageParser extends ItemPageParser{
	
	private static Logger logger = SimpleLogger.getSimpleLogger(ZhiHuItemPageParser.class);
	
    private static ZhiHuItemPageParser zhiHuItemPageParser;
    public static ZhiHuItemPageParser getInstance(){
        if(zhiHuItemPageParser == null){
            zhiHuItemPageParser = new ZhiHuItemPageParser();
        }
        return zhiHuItemPageParser;
    }
    
    /**
     * 解析正则值
     * @return
     */
    public static String getAttrDataInfo(String html,int group, String patternStr){
    	Pattern pattern = Pattern.compile(patternStr);
    	Matcher matcher = pattern.matcher(html);
    	if(matcher.find()){
    		String s = matcher.group(group);
    		return s;
    	}
    	return "";
    }

    
    @Override
    public void parse(Page page) {
        Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(page.getHtml()));
//        logger.info("+++++++++++++++++++++++++++++++++++++++++++");
//		logger.info(StringEscapeUtils.unescapeHtml(page.getHtml()));
//        logger.info("+++++++++++++++++++++++++++++++++++++++++++");
        String currenUrl = page.getUrl();
        Elements activities = doc.select("div.List-item");
        int listSize = activities.size();
        if (listSize> 0){
        	//获取动态列表
        	ListIterator<Element> it = activities.listIterator();
        	while(it.hasNext()){
        		Item item = new Item();
        		Element ele = it.next();
        		Element eleTag =  ele.select("div [class$=Item-meta]").first();//关注了问题20 小时前
        		Element  eleBody = ele.select("div.ContentItem").first();
        		Element eleBodyTitle = eleBody.select("h2 > a").first();
        		
        		//项
        		item.setTitle(eleBodyTitle.text());
        		item.setHref(currenUrl+getAttrDataInfo(ele.html(),1,"href=\"(.*)\" "));//提问数
        		item.setTimestamp(DateUtil.convertText2Date(eleTag.text()));
        		
        		String actiType = ele.select("span.ActivityItem-metaTitle").text();
        		ItemType itemType = ItemType.parse(actiType);
        		switch(itemType){
        			case Answer:
        			case Post:
	        		case Collect:{
	        			String respName = getAttrDataInfo(actiType, 1, "在(.*)中");
	        			String respHref = getAttrDataInfo(actiType, 1, "href=\"(.*)\"");
	        			((CollectItem)item).setRespName(respName);
	        			((CollectItem)item).setRespHref(respHref);
	        			PokeItem  pokeItem = getPokeItem(eleBody);
	        			((CollectItem)item).setPokeItem(pokeItem);
	        			System.out.println(((CollectItem)item).toString());
	        			break;
	        		}
	        		case Follow:;
		        		System.out.println(item.toString());
		        		break;
	        		case Poke:
	        			item =  getPokeItem(eleBody);
	        			System.out.println(((PokeItem)item).toString());
		        		break;
	        		default:break;
        		}
        	}
        	
        }else{
        	
        }
    }
    
    /**
     * 返回问题内容
     * @param html
     * @return
     */
    private PokeItem  getPokeItem(Element eleBody){
    	PokeItem item = new PokeItem();
    	Elements eleBodyMeta = eleBody.select("div [class$=AuthorInfo]");
		Elements eleBodyContent = eleBody.select("div.is-collapsed");
		Elements eleBodyActions = eleBody.select("div.ContentItem-actions");
		String answerUserHref = getAttrDataInfo(eleBodyMeta.select("a.UserLink-link").first().toString(), 1, "href=\"(.*)\">");
		String answerUserId = getAttrDataInfo(answerUserHref, 2, "(.*)/(.*)");
		String answerUserName = eleBodyMeta.select("div.AuthorInfo-name").first().text();
		String answerUserIntro = eleBodyMeta.select("div.AuthorInfo-badge").first().text();
		String answerBrief =eleBodyContent.first().text();
		String answerActions =eleBodyActions.first().text();
		item.setAnswerUserId(answerUserId);
		item.setAnswerUserHref(answerUserHref);
		item.setAnswerUserName(answerUserName);
		item.setAnswerUserIntro(answerUserIntro);
		item.setAnswerBrief(answerBrief);
		item.setAnswerActions(answerActions);
    	return item;
    }
    
    private void setUserInfo(Document doc, User u, String infoName){
        Element element = doc.select("[class=Icon Icon--" + infoName + "]").first();
        if(element == null){
            return ;
        }
        int i = element.parent().siblingIndex();
        Node node = element.parent().parent().childNode(i + 1);
        if(node instanceof TextNode){
            if (infoName.equals("location")){
                u.setLocation(node.toString());
                int childNodeSize = element.parent().parent().childNodeSize();
                if (childNodeSize <= (i + 1 + 2)){
                    return;
                }
                Node businessNode = element.parent().parent().childNode(i + 1 + 2);
                if(businessNode instanceof TextNode){
                    u.setBusiness(businessNode.toString());
                }
            }
            else if(infoName.equals("company")){
                u.setEmployment(node.toString());
                int childNodeSize = element.parent().parent().childNodeSize();
                if (childNodeSize <= (i + 1 + 2)){
                    return;
                }
                Node positionNode = element.parent().parent().childNode(i + 1 + 2);
                if(positionNode instanceof TextNode){
                    u.setPosition(positionNode.toString());
                }
            }
            else if(infoName.equals("education")){
                u.setEducation(node.toString());
            }

        }
    }
    //解析出当前用户的hashId
    private String getHashId(String userId, String dataState){

        Pattern pattern = Pattern.compile("&quot;" + userId + "&quot;.*&quot;id&quot;:&quot;([a-z0-9]{32}).*&quot;isActive&quot;:1");
        Matcher matcher = pattern.matcher(dataState);
//        System.out.println(matcher.start());
        if(matcher.find()){
            String hashId = matcher.group(1);
            return hashId;
        }
        throw new RuntimeException("not find HashId");
    }

    /**
     * 根据url解析出用户id
     * @param url
     * @return
     */
    private String getUserId(String url){
        Pattern pattern = Pattern.compile("https://www.zhihu.com/people/(.*)/(following|followees)");
        Matcher matcher = pattern.matcher(url);
        String userId = null;
        if(matcher.find()){
            userId = matcher.group(1);
            return userId;
        }
        throw new RuntimeException("not parse userId");
    }

    /**
     * 解析用户答案相关信息
     * @return
     */
    private int getAnswersInfo(String  html, String patternStr){
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(html);
        if(matcher.find()){
            String s = matcher.group(1);
            return Integer.valueOf(s);
        }
        return 0;
    }

}