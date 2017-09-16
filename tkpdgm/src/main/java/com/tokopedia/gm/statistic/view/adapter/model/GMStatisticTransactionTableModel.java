package com.tokopedia.gm.statistic.view.adapter.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableModel implements ItemType {
    public static final int TYPE = 199349;
    public String productName;
    private int deliveredAmount;
    private int transSum;
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

    public int getTransSum() {
        return transSum;
    }

    public void setTransSum(int transSum) {
        this.transSum = transSum;
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
