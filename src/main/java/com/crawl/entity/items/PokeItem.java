package com.crawl.entity.items;

import java.util.Date;

import com.crawl.entity.Item;

/**
 * 赞同了回答/文章
 * @author Sawyer
 *
 */
public class PokeItem extends Item {
	
	/**
	 * 答主
	 */
	private String answerUserId;
	
	/**
	 * 答主昵称
	 */
	private String answerUserName;
	
	
	/**
	 * 答主昵称
	 */
	private String answerUserHref;
	
	/**
	 * 答主简介
	 */
	private String answerUserIntro;
	
	/**
	 * 答案简介
	 */
	private String answerBrief;
	
	/**
	 * 答案简介
	 */
	private String answerActions;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public String getAnswerUserId() {
		return answerUserId;
	}

	public void setAnswerUserId(String answerUserId) {
		this.answerUserId = answerUserId;
	}

	public String getAnswerUserName() {
		return answerUserName;
	}

	public void setAnswerUserName(String answerUserName) {
		this.answerUserName = answerUserName;
	}

	public String getAnswerUserIntro() {
		return answerUserIntro;
	}

	public void setAnswerUserIntro(String answerUserIntro) {
		this.answerUserIntro = answerUserIntro;
	}

	public String getAnswerBrief() {
		return answerBrief;
	}

	public void setAnswerBrief(String answerBrief) {
		this.answerBrief = answerBrief;
	}

	@Override
	public String toString() {
		return "PokeQuestionItem [answerUserId=" + answerUserId + ", answerUserName=" + answerUserName
				+ ", answerUserIntro=" + answerUserIntro + ", answerBrief=" + answerBrief + "]";
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAnswerUserHref() {
		return answerUserHref;
	}

	public void setAnswerUserHref(String answerUserHref) {
		this.answerUserHref = answerUserHref;
	}

	public String getAnswerActions() {
		return answerActions;
	}

	public void setAnswerActions(String answerActions) {
		this.answerActions = answerActions;
	}
	
}
