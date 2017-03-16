package me.j360.dubbo.manager.helper;

import org.springframework.beans.factory.annotation.Autowired;

public class ItemPubHelperFactory {
	
	@Autowired
	private ItemNormalPubHelper itemNormalPubHelper;
	@Autowired
	private ItemDnaPubHelper itemDnaPubHelper;
	@Autowired
	private ItemCardPubHelper itemCardPubHelper;
	
	public ItemPubHelper create(ItemPublishType publishType){
		switch(publishType) {
		case DNA: 
			return itemDnaPubHelper;

		case CARD: 
			return itemCardPubHelper;
		
		case NARMAL: 
			return itemNormalPubHelper;
		}
		return null;
	}
}
