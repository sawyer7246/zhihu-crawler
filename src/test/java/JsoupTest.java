import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawl.parser.zhihu.ZhiHuItemPageParser;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class JsoupTest {
    public static void main(String[] args){
    	String str = new String("dsada\u4fdddadsa");
    	String jsonData = ZhiHuItemPageParser.getAttrDataInfo(str,1,"\\[u][0-9a-z]{4}");
    	System.out.println(new String("dsada\u4fdddadsa"));
    }
    
	
    
}
