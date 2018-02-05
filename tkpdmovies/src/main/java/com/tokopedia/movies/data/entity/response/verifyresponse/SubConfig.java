package com.tokopedia.movies.data.entity.response.verifyresponse;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class SubConfig{

	@SerializedName("name")
	private String name;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"SubConfig{" + 
			"name = '" + name + '\'' + 
			"}";
		}
}