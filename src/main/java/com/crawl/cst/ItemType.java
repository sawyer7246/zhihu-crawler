package com.crawl.cst;

/**
 * 不同页面类型常量定义:
 * @author Sawyer
 */
public enum ItemType {
	/**
	 * 收藏
	 */
	Collect("1","收藏"),
	/**
	 * 关注
	 */
	Follow("2","关注"),
	/**
	 * 赞同
	 */
	Poke("3","赞"),
	/**
	 * 回答了
	 */
	Answer("4","回答"),
	/**
	 * 发表了文章
	 */
	Post("5","发表");
	
	private ItemType(String typeNo, String typeName){
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
	
	public static ItemType parse(String actiType){
		ItemType retType = null ;
		for(ItemType item:ItemType.values()){
			if(actiType!=null&&actiType.contains(item.getTypeName())){
				return item;
			}
		}
		return retType;
	}
	

}
