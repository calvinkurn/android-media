package com.tokopedia.transaction.purchase.detail.model.buyagain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToCartMulti {

    @SerializedName("error_message")
    @Expose
    private String errorMessage = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private DataResponseBuyAgain data;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataResponseBuyAgain getData() {
        return data;
    }

    public void setData(DataResponseBuyAgain data) {
        this.data = data;
    }

}