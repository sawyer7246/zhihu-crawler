package com.crawl.parser.zhihu;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.crawl.entity.items.ItemContent;

public class ZhiHuItemPageParserTest {

	/**
	 * 关注问题字符串
	 */
	String questItemHtml = "<div class=\"List-item\">"
			+ "<div class=\"List-itemMeta\">"
			+ "<div class=\"ActivityItem-meta\">"
			+ "<span class=\"ActivityItem-metaTitle\">"
			+ "<!-- react-text: 173 --><!-- /react-text -->"
			+ "<!-- react-text: 174 -->关注了问题<!-- /react-text -->"
			+ "</span><span>20 小时前</span></div></div>"
			+ "<div class=\"ContentItem\" data-za-module=\"QuestionItem\""
			+ " data-za-module-info="
			+ "\"{&quot;card&quot;:{&quot;content&quot;:{&quot;type&quot;"
			+ ":&quot;Question&quot;,&quot;token&quot;:&quot;24354668&quot;"
			+ ",&quot;item_num&quot;:44,&quot;comment_num&quot;:1,&quot;follower_"
			+ "num&quot;:773,&quot;publish_timestamp&quot;:1404317096000,&quot;author_"
			+ "member_hash_id&quot;:&quot;0&quot;}}}\"><h2 class=\"ContentItem-title\""
			+ "><a  target=\"_blank\" href=\"/question/24354668\">在香港城市大学"
			+ " (City University of Hong Kong) 就读是怎样一番体验？</a></h2></div>"
			+ "</div>";
	
	
	@Test
	public void testParseTitle() {
		Document ele = Jsoup.parse(questItemHtml);
		Elements eleTag =  ele.select("div [class$=Item-meta]");
		Elements eleBody = ele.select("div [data-za-module$=Item]");
		Elements eleBodyTitle = eleBody.select("h2 > a");
		//项
		System.out.println("============");
		System.out.println(ele.text());
		System.out.println(ele.toString());
		System.out.println("============");
		System.out.println(eleTag.text());
		System.out.println(eleTag.toString());
		System.out.println("============");
		System.out.println(eleBody.text());
		System.out.println(eleBody.toString());
		System.out.println("============");
		System.out.println(eleBodyTitle.text());
		System.out.println(eleBodyTitle.html());
		System.out.println(eleBodyTitle.toString());
		System.out.println("============");
		System.out.println(ZhiHuItemPageParser.getAttrDataInfo(eleBodyTitle.toString(),1,"href=\"(.*)\"(\\s+|>)"));
		
		//解析出标签上的Json
		String jsonData = ZhiHuItemPageParser.getAttrDataInfo(eleBody.toString(),1,"data-za-module-info=\"(.*)\"(\\s+|>)");
		System.out.println(jsonData=jsonData.replaceAll("&quot;", "\""));
		System.out.println(ItemContent.getContentFromJsonStr(jsonData));
	}
	
	@Test
	public void testGetHrefInfo() {
		String url = "";
//		ZhiHuItemPageParser.getUserHrefInfo(questItemHtml,);
	}

}
