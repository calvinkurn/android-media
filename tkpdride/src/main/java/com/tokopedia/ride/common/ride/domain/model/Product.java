package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/14/17.
 */

public class Product implements Parcelable {
    boolean upfrontFareEnabled;
    int capacity;
    String productId;
    String image;
    boolean cashEnabled;
    boolean shared;
    String shortDescription;
    String displayName;
    String productGroup;
    String description;

    public Product() {
    }

    protected Product(Parcel in) {
        upfrontFareEnabled = in.readByte() != 0;
        capacity = in.readInt();
        productId = in.readString();
        image = in.readString();
        cashEnabled = in.readByte() != 0;
        shared = in.readByte() != 0;
        shortDescription = in.readString();
        displayName = in.readString();
        productGroup = in.readString();
        description = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public boolean isUpfrontFareEnabled() {
        return upfrontFareEnabled;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getProductId() {
        return productId;
    }

    public String getImage() {
        return image;
    }

    public boolean isCashEnabled() {
        return cashEnabled;
    }

    public boolean isShared() {
        return shared;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setUpfrontFareEnabled(boolean upfrontFareEnabled) {
        this.upfrontFareEnabled = upfrontFareEnabled;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCashEnabled(boolean cashEnabled) {
        this.cashEnabled = cashEnabled;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (upfrontFareEnabled ? 1 : 0));
        dest.writeInt(capacity);
        dest.writeString(productId);
        dest.writeString(image);
        dest.writeByte((byte) (cashEnabled ? 1 : 0));
        dest.writeByte((byte) (shared ? 1 : 0));
        dest.writeString(shortDescription);
        dest.writeString(displayName);
        dest.writeString(productGroup);
        dest.writeString(description);
    }
}
