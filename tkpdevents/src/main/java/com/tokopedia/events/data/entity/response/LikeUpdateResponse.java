package com.tokopedia.events.data.entity.response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class LikeUpdateResponse{

	@SerializedName("code")
	private String code;

	@SerializedName("message_error")
	private String messageError;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setMessageError(String messageError){
		this.messageError = messageError;
	}

	public String getMessageError(){
		return messageError;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"LikeUpdateResponse{" + 
			"code = '" + code + '\'' + 
			",message_error = '" + messageError + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}