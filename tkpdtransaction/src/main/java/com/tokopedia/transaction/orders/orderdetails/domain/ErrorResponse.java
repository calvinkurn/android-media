package com.tokopedia.transaction.orders.orderdetails.domain;


import java.io.IOException;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;

public class ErrorResponse extends BaseResponseError {

	@SerializedName("error_message")
	private List<String> errorMessage;

	public List<String> getMessage_error() {
		return message_error;
	}

	public void setMessage_error(List<String> message_error) {
		this.message_error = message_error;
	}

	@SerializedName("message_error")
	private List<String> message_error;

	@SerializedName("data")
	private Object data;

	@SerializedName("server_process_time")
	private double serverProcessTime;

	@SerializedName("config")
	private Object config;

	@SerializedName("status")
	private String status;

	public void setErrorMessage(List<String> errorMessage){
		this.errorMessage = errorMessage;
	}

	public List<String> getErrorMessage(){
		return errorMessage;
	}

	public void setData(Object data){
		this.data = data;
	}

	public Object getData(){
		return data;
	}

	public void setServerProcessTime(double serverProcessTime){
		this.serverProcessTime = serverProcessTime;
	}

	public double getServerProcessTime(){
		return serverProcessTime;
	}

	public void setConfig(Object config){
		this.config = config;
	}

	public Object getConfig(){
		return config;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ErrorResponse{" + 
			"error_message = '" + errorMessage + '\'' + 
			",data = '" + data + '\'' + 
			",server_process_time = '" + serverProcessTime + '\'' + 
			",config = '" + config + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
	@Override
	public String getErrorKey() {
		if(getErrorMessage().size() >0) {
			return getErrorMessage().get(0);
		}else if(getMessage_error().size() > 0 ){
			return getMessage_error().get(0);
		}else {
			return "";
		}
	}

	@Override
	public boolean hasBody() {
		if(getErrorMessage()  != null) {
			return getErrorMessage().size() > 0;
		} else if(getMessage_error()  != null) {
			return getMessage_error().size() > 0;
		}else {
			return false;
		}
	}

	@Override
	public IOException createException() {
		if (getErrorMessage() != null) {
			return new MessageErrorException(getErrorMessage().get(0));
		} else if (getMessage_error() != null) {
			return new MessageErrorException(getMessage_error().get(0));
		}else {
			return new MessageErrorException("");
		}
	}


}