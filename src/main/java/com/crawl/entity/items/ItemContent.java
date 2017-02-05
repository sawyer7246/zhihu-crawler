package com.crawl.entity.items;

import java.util.Date;

import org.json.JSONObject;

public class ItemContent {
	
	/**
	 * 明细类型
	 */
	private String type;
	private String token;
	private String item_num;
	private Long comment_num;
	private Long follower_num;
	/**
	 * 发布时间
	 */
	private Date publish_timestamp;
	private String author_member_hash_id;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getItem_num() {
		return item_num;
	}
	public void setItem_num(String item_num) {
		this.item_num = item_num;
	}
	public Long getComment_num() {
		return comment_num;
	}
	public void setComment_num(Long comment_num) {
		this.comment_num = comment_num;
	}
	public Long getFollower_num() {
		return follower_num;
	}
	public void setFollower_num(Long follower_num) {
		this.follower_num = follower_num;
	}
	public Date getPublish_timestamp() {
		return publish_timestamp;
	}
	public void setPublish_timestamp(Date publish_timestamp) {
		this.publish_timestamp = publish_timestamp;
	}
	public String getAuthor_member_hash_id() {
		return author_member_hash_id;
	}
	public void setAuthor_member_hash_id(String author_member_hash_id) {
		this.author_member_hash_id = author_member_hash_id;
	}
	
	public static ItemContent getContentFromJsonStr(String str){
		System.out.println(str=str.replaceAll("&quot;", "\""));
		JSONObject jsonObj = new JSONObject(str);
		JSONObject jsonCard = jsonObj.getJSONObject("card");
		JSONObject jsonContent = jsonCard.getJSONObject("content");
		ItemContent content = new ItemContent();
		content.setType(jsonContent.getString("type"));
		content.setComment_num(jsonContent.getLong("comment_num"));
		content.setFollower_num(jsonContent.getLong("follower_num"));
		Long timeMils = new Long(jsonContent.getLong("publish_timestamp"));
		content.setPublish_timestamp(new Date(timeMils));
		return content;
	}
	
	@Override
	public String toString() {
		return "ItemContent [type=" + type + ", token=" + token + ", item_num=" + item_num + ", comment_num="
				+ comment_num + ", follower_num=" + follower_num + ", publish_timestamp=" + publish_timestamp
				+ ", author_member_hash_id=" + author_member_hash_id + "]";
	}
	
	
}
