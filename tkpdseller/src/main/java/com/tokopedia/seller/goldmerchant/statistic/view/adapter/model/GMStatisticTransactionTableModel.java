package com.tokopedia.seller.goldmerchant.statistic.view.adapter.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableModel implements ItemType {
    public static final int TYPE = 199349;
    public String productName;
    private int deliveredAmount;
    private int deliveredSum;
    private int orderSum;
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

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

    @Override
    public int getType() {
        return TYPE;
    }
}
