package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class OrderDomain {
    private int openAmount;
    private int shippingPrices;

    public OrderDomain(int openAmount, int shippingPrices) {
        this.openAmount = openAmount;
        this.shippingPrices = shippingPrices;
    }

    public int getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(int openAmount) {
        this.openAmount = openAmount;
    }

    public int getShippingPrices() {
        return shippingPrices;
    }

    public void setShippingPrices(int shippingPrices) {
        this.shippingPrices = shippingPrices;
    }
}
