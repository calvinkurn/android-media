package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class CartPayableDetailModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalItem);
        dest.writeString(this.totalItemPrice);
        dest.writeString(this.shippingWeight);
        dest.writeString(this.shippingFee);
        dest.writeString(this.insuranceFee);
        dest.writeString(this.promoPrice);
        dest.writeString(this.payablePrice);
        dest.writeString(this.promoFreeShipping);
    }

    public CartPayableDetailModel() {
    }

    protected CartPayableDetailModel(Parcel in) {
        this.totalItem = in.readString();
        this.totalItemPrice = in.readString();
        this.shippingWeight = in.readString();
        this.shippingFee = in.readString();
        this.insuranceFee = in.readString();
        this.promoPrice = in.readString();
        this.payablePrice = in.readString();
        this.promoFreeShipping = in.readString();
    }

    public static final Parcelable.Creator<CartPayableDetailModel> CREATOR = new Parcelable.Creator<CartPayableDetailModel>() {
        @Override
        public CartPayableDetailModel createFromParcel(Parcel source) {
            return new CartPayableDetailModel(source);
        }

        @Override
        public CartPayableDetailModel[] newArray(int size) {
            return new CartPayableDetailModel[size];
        }
    };
}
