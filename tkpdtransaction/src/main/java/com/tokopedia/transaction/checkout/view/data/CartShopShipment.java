package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 22/02/18.
 */

public class CartShopShipment implements Parcelable {
    private int shipmentId;
    private boolean dropshipEnable;
    private List<CartShipmentProduct> cartShipmentProducts;

    protected CartShopShipment(Parcel in) {
        shipmentId = in.readInt();
        dropshipEnable = in.readByte() != 0;
        cartShipmentProducts = in.createTypedArrayList(CartShipmentProduct.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shipmentId);
        dest.writeByte((byte) (dropshipEnable ? 1 : 0));
        dest.writeTypedList(cartShipmentProducts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartShopShipment> CREATOR = new Creator<CartShopShipment>() {
        @Override
        public CartShopShipment createFromParcel(Parcel in) {
            return new CartShopShipment(in);
        }

        @Override
        public CartShopShipment[] newArray(int size) {
            return new CartShopShipment[size];
        }
    };

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public boolean isDropshipEnable() {
        return dropshipEnable;
    }

    public void setDropshipEnable(boolean dropshipEnable) {
        this.dropshipEnable = dropshipEnable;
    }

    public List<CartShipmentProduct> getCartShipmentProducts() {
        return cartShipmentProducts;
    }

    public void setCartShipmentProducts(List<CartShipmentProduct> cartShipmentProducts) {
        this.cartShipmentProducts = cartShipmentProducts;
    }
}
