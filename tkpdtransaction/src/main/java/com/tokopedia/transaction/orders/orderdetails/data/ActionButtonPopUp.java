package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionButtonPopUp {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtonList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<ActionButton> getActionButtonList() {
        return actionButtonList;
    }

    public void setActionButtonList(List<ActionButton> actionButtonList) {
        this.actionButtonList = actionButtonList;
    }
}
