package org.epay.model.ext;

import java.util.ArrayList;
import java.util.List;

import org.epay.model.base.OrderGoods;
import org.epay.model.base.OrderRecord;

public class OrderRecordExt extends OrderRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<OrderGoods> orderGoodsArray = new ArrayList<OrderGoods>();

	public boolean addOrderGoods(OrderGoods orderGoods) {
		return this.orderGoodsArray.add(orderGoods);
	}

}
