package com.tokopedia.transaction.orders.orderlist.data.bomorderfilter;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GetBomOrderFilter{

	@SerializedName("custom_date")
	private CustomDate customDate;

	@SerializedName("default_date")
	private DefaultDate defaultDate;

	public void setCustomDate(CustomDate customDate){
		this.customDate = customDate;
	}

	public CustomDate getCustomDate(){
		return customDate;
	}

	public void setDefaultDate(DefaultDate defaultDate){
		this.defaultDate = defaultDate;
	}

	public DefaultDate getDefaultDate(){
		return defaultDate;
	}

	@Override
 	public String toString(){
		return 
			"GetBomOrderFilter{" + 
			"custom_date = '" + customDate + '\'' + 
			",default_date = '" + defaultDate + '\'' + 
			"}";
		}
}