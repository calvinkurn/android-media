package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupShop implements Parcelable {

    private List<String> errors = new ArrayList<>();
    private Shop shop;
    private List<ShopShipment> shopShipments = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.errors);
        dest.writeParcelable(this.shop, flags);
        dest.writeTypedList(this.shopShipments);
        dest.writeTypedList(this.products);
    }

    public GroupShop() {
    }

    protected GroupShop(Parcel in) {
        this.errors = in.createStringArrayList();
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.shopShipments = in.createTypedArrayList(ShopShipment.CREATOR);
        this.products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Parcelable.Creator<GroupShop> CREATOR = new Parcelable.Creator<GroupShop>() {
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
