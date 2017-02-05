package com.crawl.cst;

/**
 * 不同页面类型常量定义:
 * @author Sawyer
 */
public enum PageType {
	
	Activities("1","动态"),
	Answers("2","回答"),
	Pins("3","分享"),
	Asks("4","提问"),
	Collections("5","收藏"),
	following("6","关注");
	
	private PageType(String typeNo, String typeName){
		this.typeNo = typeNo;
		this.typeName = typeName;
	}
	
	private String typeNo;
	private String typeName;
	
	public String getTypeNo() {
		return typeNo;
	}
	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
	

}
