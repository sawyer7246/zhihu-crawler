import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * 测试请求Ajax数据
 * @author Sawyer
 *
 */
public class JsoupAjaxTest { 
	
	private String followingURL = "https://www.zhihu.com/api/v4/members/sawyer-nick/followees?include=data%5B*%5D.answer_count%2Carticles_count%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=40&limit=20";
	
	
	@Test
	public void testAjaxGet() {
		Document document1 = null ;
		try {
			document1 = Jsoup.connect(followingURL)
			        .ignoreContentType(true)
			        .data("query", "Java")
			        .userAgent("Mozilla")
			        .cookie("auth", "token")
			        .timeout(3000)
			        .get();
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println(document1.text());
	}
}
