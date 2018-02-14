package com.tokopedia.events.data.entity.response.searchresponse;

import com.google.gson.annotations.SerializedName;

public class ValuesItem{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("priority")
	private int priority;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPriority(int priority){
		this.priority = priority;
	}

	public int getPriority(){
		return priority;
	}

	@Override
 	public String toString(){
		return 
			"ValuesItemDomain{" +
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",priority = '" + priority + '\'' + 
			"}";
		}
}