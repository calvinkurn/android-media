package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.Header;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class DataResponseCommon<T> {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName(value = "data")
    @Expose
    private T data;
    @SerializedName(value = "message_error")
    @Expose
    private List<String> errorMessage;
    @SerializedName(value = "message_status")
    @Expose
    private List<String> messageStatus;

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(List<String> messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
