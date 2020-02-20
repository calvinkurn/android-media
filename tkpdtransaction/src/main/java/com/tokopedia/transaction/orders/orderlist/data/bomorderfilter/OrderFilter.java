package com.tokopedia.transaction.orders.orderlist.data.bomorderfilter;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class OrderFilter{

	@SerializedName("get_bom_order_filter")
	private GetBomOrderFilter getBomOrderFilter;

	public void setGetBomOrderFilter(GetBomOrderFilter getBomOrderFilter){
		this.getBomOrderFilter = getBomOrderFilter;
	}

	public GetBomOrderFilter getGetBomOrderFilter(){
		return getBomOrderFilter;
	}

	@Override
 	public String toString(){
		return 
			"OrderFilter{" + 
			"get_bom_order_filter = '" + getBomOrderFilter + '\'' + 
			"}";
		}
}