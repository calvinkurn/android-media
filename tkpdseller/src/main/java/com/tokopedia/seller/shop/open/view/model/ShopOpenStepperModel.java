package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;

import com.tokopedia.seller.base.view.model.StepperModel;

/**
 * Created by nakama on 19/12/17.
 */

public class ShopOpenStepperModel implements StepperModel {
    private final int DEFAULT_UNSELECTED_DISTRICT_ID = -1;
    private int districtID = DEFAULT_UNSELECTED_DISTRICT_ID;
    private String shopName;
    private String shopDomain;

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public ShopOpenStepperModel() {
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.districtID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopDomain);
    }

    protected ShopOpenStepperModel(Parcel in) {
        this.districtID = in.readInt();
        this.shopName = in.readString();
        this.shopDomain = in.readString();
    }

    public static final Creator<ShopOpenStepperModel> CREATOR = new Creator<ShopOpenStepperModel>() {
        @Override
        public ShopOpenStepperModel createFromParcel(Parcel source) {
            return new ShopOpenStepperModel(source);
        }

        @Override
        public ShopOpenStepperModel[] newArray(int size) {
            return new ShopOpenStepperModel[size];
        }
    };
}
