package com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupShop implements Parcelable {
    private boolean isError;
    private String errorMessage;
    private boolean isWarning;
    private String warningMessage;

    private Shop shop;
    private List<ShopShipment> shopShipments = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public void setShopShipments(List<ShopShipment> shopShipments) {
        this.shopShipments = shopShipments;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public GroupShop() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeByte(this.isWarning ? (byte) 1 : (byte) 0);
        dest.writeString(this.warningMessage);
        dest.writeParcelable(this.shop, flags);
        dest.writeTypedList(this.shopShipments);
        dest.writeTypedList(this.products);
    }

    protected GroupShop(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.isWarning = in.readByte() != 0;
        this.warningMessage = in.readString();
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.shopShipments = in.createTypedArrayList(ShopShipment.CREATOR);
        this.products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<GroupShop> CREATOR = new Creator<GroupShop>() {
        @Override
        public GroupShop createFromParcel(Parcel source) {
            return new GroupShop(source);
        }

        @Override
        public GroupShop[] newArray(int size) {
            return new GroupShop[size];
        }
    };
}
