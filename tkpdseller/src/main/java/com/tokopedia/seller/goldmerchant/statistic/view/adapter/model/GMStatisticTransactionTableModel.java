package com.tokopedia.seller.goldmerchant.statistic.view.adapter.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableModel implements ItemType {
    public static final int TYPE = 199349;

    private int deliveredAmount;
    private int deliveredSum;
    private int orderSum;

    public int getDeliveredAmount() {
        return deliveredAmount;
    }

    public void setDeliveredAmount(int deliveredAmount) {
        this.deliveredAmount = deliveredAmount;
    }

    public int getDeliveredSum() {
        return deliveredSum;
    }

    public void setDeliveredSum(int deliveredSum) {
        this.deliveredSum = deliveredSum;
    }

    public int getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(int orderSum) {
        this.orderSum = orderSum;
    }

    public String productName;

    @Override
    public int getType() {
        return TYPE;
    }
}
