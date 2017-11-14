
package com.tokopedia.transaction.purchase.detail.model.history.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("order_status_code")
    @Expose
    private Integer orderStatusCode;
    @SerializedName("order_status_color")
    @Expose
    private String orderStatusColor;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("history_img")
    @Expose
    private String historyImg;
    @SerializedName("history_title")
    @Expose
    private String historyTitle;
    @SerializedName("histories")
    @Expose
    private List<History> histories = null;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(Integer orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderStatusColor() {
        return orderStatusColor;
    }

    public void setOrderStatusColor(String orderStatusColor) {
        this.orderStatusColor = orderStatusColor;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getHistoryImg() {
        return historyImg;
    }

    public void setHistoryImg(String historyImg) {
        this.historyImg = historyImg;
    }

    public String getHistoryTitle() {
        return historyTitle;
    }

    public void setHistoryTitle(String historyTitle) {
        this.historyTitle = historyTitle;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

}
