package com.crawl.entity.items;

import com.crawl.entity.Item;

/**
 * 在XX中收藏了YY
 * @author Sawyer
 *
 */
public class CollectItem extends Item {
	
	/**
	 * XX收藏夹
	 */
	private String respName;
	
	/**
	 * XX收藏夹链接
	 */
	private String respHref;
	
	/**
	 * 收藏内容
	 */
	private PokeItem pokeItem;
	

	public String getRespName() {
		return respName;
	}

	public void setRespName(String respName) {
		this.respName = respName;
	}

	public String getRespHref() {
		return respHref;
	}

	public void setRespHref(String respHref) {
		this.respHref = respHref;
	}

	public PokeItem getPokeItem() {
		return pokeItem;
	}

	public void setPokeItem(PokeItem pokeItem) {
		this.pokeItem = pokeItem;
	}

	@Override
	public String toString() {
		return "CollectItem [respName=" + respName + ", respHref=" + respHref + ", pokeItem=" + pokeItem + "]";
	}
		
}
