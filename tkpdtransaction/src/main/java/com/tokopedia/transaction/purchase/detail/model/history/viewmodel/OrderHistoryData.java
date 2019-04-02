package com.tokopedia.transaction.purchase.detail.model.history.viewmodel;

import java.util.List;

/**
 * Created by kris on 11/8/17. Tokopedia
 */

public class OrderHistoryData {

    public static final int ORDER_FINISHED = 700;

    public static final int ORDER_ARRIVED = 600;

    public static final int ORDER_SENT = 500;

    public static final int ORDER_PROCESSED = 400;

    public static final int ORDER_VERIFIED = 220;

    public static final int ORDER_CHECKED_OUT = 100;

    private int stepperMode;

    private String stepperStatusTitle;

    private String historyImage;

    private List<OrderHistoryListData> orderListData;

    public int getStepperMode() {
        return stepperMode;
    }

    public void setStepperMode(int stepperMode) {
        this.stepperMode = stepperMode;
    }

    public String getStepperStatusTitle() {
        return stepperStatusTitle;
    }

    public void setStepperStatusTitle(String stepperStatusTitle) {
        this.stepperStatusTitle = stepperStatusTitle;
    }

    public List<OrderHistoryListData> getOrderListData() {
        return orderListData;
    }

    public void setOrderListData(List<OrderHistoryListData> orderListData) {
        this.orderListData = orderListData;
    }

    public String getHistoryImage() {
        return historyImage;
    }

    public void setHistoryImage(String historyImage) {
        this.historyImage = historyImage;
    }
}
