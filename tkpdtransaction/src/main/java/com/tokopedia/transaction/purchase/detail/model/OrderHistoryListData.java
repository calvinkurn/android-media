package com.tokopedia.transaction.purchase.detail.model;

/**
 * Created by kris on 11/8/17. Tokopedia
 */

public class OrderHistoryListData {

    private String orderHistoryTitle;

    private String orderHistoryDescription;

    private String orderHistoryTime;

    private String orderHistoryDate;

    private String actionBy;

    private String color;

    public String getOrderHistoryTitle() {
        return orderHistoryTitle;
    }

    public void setOrderHistoryTitle(String orderHistoryTitle) {
        this.orderHistoryTitle = orderHistoryTitle;
    }

    public String getOrderHistoryDescription() {
        return orderHistoryDescription;
    }

    public void setOrderHistoryDescription(String orderHistoryDescription) {
        this.orderHistoryDescription = orderHistoryDescription;
    }

    public String getOrderHistoryTime() {
        return orderHistoryTime;
    }

    public void setOrderHistoryTime(String orderHistoryTime) {
        this.orderHistoryTime = orderHistoryTime;
    }

    public String getOrderHistoryDate() {
        return orderHistoryDate;
    }

    public void setOrderHistoryDate(String orderHistoryDate) {
        this.orderHistoryDate = orderHistoryDate;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
