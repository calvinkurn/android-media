package com.tokopedia.transaction.addtocart.model.kero;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogisticsError {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("path")
    @Expose
    private List<String> pathList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
