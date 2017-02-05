package com.crawl.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.StringUtils;

public class DateUtil {

	public static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	public static String regEx="[^0-9]";   
	public static Pattern p = Pattern.compile(regEx);   
	
	public static Date convertText2Date(String str){
		Long dateTime = new Date().getTime();
		if(StringUtils.isEmptyOrWhitespaceOnly(str)){
			return null;
		}else if(str.contains("刚")){
			//刚刚
			return new Date();
		}else {
			Matcher m = p.matcher(str);  
			Integer offset = new Integer(m.replaceAll("").trim());
			if(str.contains("秒")){
				//秒
				dateTime = dateTime - offset*1000;
				return new Date(dateTime);
			}else if(str.contains("分")){
				//分钟
				dateTime = dateTime - offset*1000*60;
				return new Date(dateTime);
			}else if(str.contains("时")){
				//时
				dateTime = dateTime - offset*1000*60*60;
				return new Date(dateTime);
			}else if(str.contains("天")){
				//天
				dateTime = dateTime - offset*1000*60*60*24;
				return new Date(dateTime);
			}else if(str.contains("月")){
				//月
				dateTime = dateTime - offset*1000*60*60*24*30;
				return new Date(dateTime);
			}else if(str.contains("年")){
				//年
				dateTime = dateTime - offset*1000*60*60*24*30*365;
				return new Date(dateTime);
			}
		}
		return null;
	}
}
