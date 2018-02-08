package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSellerItemModel implements Parcelable {

    private String senderName;
    private List<CartItemModel> cartItemModels;
    private String shipmentOption;
    private String totalPrice;

    public CartSellerItemModel() {

    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<CartItemModel> getCartItemModels() {
        return cartItemModels;
    }

    public void setCartItemModels(List<CartItemModel> cartItemModels) {
        this.cartItemModels = cartItemModels;
    }

    public String getShipmentOption() {
        return shipmentOption;
    }

    public void setShipmentOption(String shipmentOption) {
        this.shipmentOption = shipmentOption;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.senderName);
        dest.writeTypedList(this.cartItemModels);
        dest.writeString(this.shipmentOption);
        dest.writeString(this.totalPrice);
    }

    public CartSellerItemModel(Parcel in) {
        this.senderName = in.readString();
        this.cartItemModels = in.createTypedArrayList(CartItemModel.CREATOR);
        this.shipmentOption = in.readString();
        this.totalPrice = in.readString();
    }

    public static final Creator<CartSellerItemModel> CREATOR = new Creator<CartSellerItemModel>() {
        @Override
        public CartSellerItemModel createFromParcel(Parcel source) {
            return new CartSellerItemModel(source);
        }

        @Override
        public CartSellerItemModel[] newArray(int size) {
            return new CartSellerItemModel[size];
        }
    };
}
