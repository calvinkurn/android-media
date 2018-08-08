package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusInfoResponse {
    @SerializedName("show")
    @Expose
    private boolean show;
    @SerializedName("date")
    @Expose
    private String date;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StatusInfoResponse{" +
                "show='" + show + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}