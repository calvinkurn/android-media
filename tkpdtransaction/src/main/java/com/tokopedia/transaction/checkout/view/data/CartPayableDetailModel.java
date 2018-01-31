package com.tokopedia.transaction.checkout.view.data;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class CartPayableDetailModel {

    private String totalItem;
    private String totalItemPrice;
    private String shippingWeight;
    private String shippingFee;
    private String insuranceFee;
    private String promoPrice;
    private String payablePrice;
    private String promoFreeShipping;

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    public String getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(String totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(String promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(String payablePrice) {
        this.payablePrice = payablePrice;
    }

    public String getPromoFreeShipping() {
        return promoFreeShipping;
    }

    public void setPromoFreeShipping(String promoFreeShipping) {
        this.promoFreeShipping = promoFreeShipping;
    }
}
