package com.tokopedia.events.data.entity.response.verifyresponse;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Message{

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Message{" + 
			"title = '" + title + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}