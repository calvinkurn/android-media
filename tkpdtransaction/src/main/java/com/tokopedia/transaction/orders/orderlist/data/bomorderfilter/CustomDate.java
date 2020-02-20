package com.tokopedia.transaction.orders.orderlist.data.bomorderfilter;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CustomDate{

	@SerializedName("max_day")
	private int maxDay;

	@SerializedName("start_range_date")
	private String startRangeDate;

	@SerializedName("title")
	private String title;

	@SerializedName("end_range_date")
	private String endRangeDate;

	public void setMaxDay(int maxDay){
		this.maxDay = maxDay;
	}

	public int getMaxDay(){
		return maxDay;
	}

	public void setStartRangeDate(String startRangeDate){
		this.startRangeDate = startRangeDate;
	}

	public String getStartRangeDate(){
		return startRangeDate;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setEndRangeDate(String endRangeDate){
		this.endRangeDate = endRangeDate;
	}

	public String getEndRangeDate(){
		return endRangeDate;
	}

	@Override
 	public String toString(){
		return 
			"CustomDate{" + 
			"max_day = '" + maxDay + '\'' + 
			",start_range_date = '" + startRangeDate + '\'' + 
			",title = '" + title + '\'' + 
			",end_range_date = '" + endRangeDate + '\'' + 
			"}";
		}
}