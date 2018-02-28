package com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class CartPayableDetailModel implements Parcelable {

    private int totalItem;
    private double totalPrice;
    private double totalWeight;
    private double shippingFee;
    private double insuranceFee;
    private double promoPrice;

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.promoPrice = promoPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalItem);
        dest.writeDouble(this.totalPrice);
        dest.writeDouble(this.totalWeight);
        dest.writeDouble(this.shippingFee);
        dest.writeDouble(this.insuranceFee);
        dest.writeDouble(this.promoPrice);
    }

    public CartPayableDetailModel() {
    }

    protected CartPayableDetailModel(Parcel in) {
        this.totalItem = in.readInt();
        this.totalPrice = in.readDouble();
        this.totalWeight = in.readDouble();
        this.shippingFee = in.readDouble();
        this.insuranceFee = in.readDouble();
        this.promoPrice = in.readDouble();
    }

    public static final Creator<CartPayableDetailModel> CREATOR = new Creator<CartPayableDetailModel>() {
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
