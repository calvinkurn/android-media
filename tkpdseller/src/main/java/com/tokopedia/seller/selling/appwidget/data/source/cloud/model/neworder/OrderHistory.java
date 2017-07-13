
package com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistory {

    @SerializedName("history_status_date")
    @Expose
    private String historyStatusDate;
    @SerializedName("history_status_date_full")
    @Expose
    private String historyStatusDateFull;
    @SerializedName("history_order_status")
    @Expose
    private String historyOrderStatus;
    @SerializedName("history_comments")
    @Expose
    private int historyComments;
    @SerializedName("history_action_by")
    @Expose
    private String historyActionBy;
    @SerializedName("history_buyer_status")
    @Expose
    private String historyBuyerStatus;
    @SerializedName("history_seller_status")
    @Expose
    private String historySellerStatus;

    public String getHistoryStatusDate() {
        return historyStatusDate;
    }

    public void setHistoryStatusDate(String historyStatusDate) {
        this.historyStatusDate = historyStatusDate;
    }

    public String getHistoryStatusDateFull() {
        return historyStatusDateFull;
    }

    public void setHistoryStatusDateFull(String historyStatusDateFull) {
        this.historyStatusDateFull = historyStatusDateFull;
    }

    public String getHistoryOrderStatus() {
        return historyOrderStatus;
    }

    public void setHistoryOrderStatus(String historyOrderStatus) {
        this.historyOrderStatus = historyOrderStatus;
    }

    public int getHistoryComments() {
        return historyComments;
    }

    public void setHistoryComments(int historyComments) {
        this.historyComments = historyComments;
    }

    public String getHistoryActionBy() {
        return historyActionBy;
    }

    public void setHistoryActionBy(String historyActionBy) {
        this.historyActionBy = historyActionBy;
    }

    public String getHistoryBuyerStatus() {
        return historyBuyerStatus;
    }

    public void setHistoryBuyerStatus(String historyBuyerStatus) {
        this.historyBuyerStatus = historyBuyerStatus;
    }

    public String getHistorySellerStatus() {
        return historySellerStatus;
    }

    public void setHistorySellerStatus(String historySellerStatus) {
        this.historySellerStatus = historySellerStatus;
    }

}
