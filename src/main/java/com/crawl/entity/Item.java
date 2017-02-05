package com.crawl.entity;

import java.util.Date;

/**
 * 动态条目
 * @author Sawyer
 *
 */
public class Item {
	
	/**
	 * 条目类型
	 */
	private String type;
	
	/**
	 * 条目时间
	 */
	private Date timestamp;
	
	/**
	 * 条目标题
	 */
	private String title;
	
	/**
	 * 条目链接
	 */
	private String href;
	
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
 
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "Item [type=" + type + ", timestamp=" + timestamp + ", title=" + title + ", href=" + href + "]";
	}
	
}
