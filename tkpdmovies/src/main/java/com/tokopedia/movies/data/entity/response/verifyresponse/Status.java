package com.tokopedia.movies.data.entity.response.verifyresponse;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Status{

	@SerializedName("result")
	private String result;

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private Message message;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Status{" + 
			"result = '" + result + '\'' + 
			",code = '" + code + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}