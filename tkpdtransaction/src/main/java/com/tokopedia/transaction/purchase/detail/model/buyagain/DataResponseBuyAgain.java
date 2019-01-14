
package com.tokopedia.transaction.purchase.detail.model.buyagain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponseBuyAgain {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("messages")
    @Expose
    private List<String> message = null;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
