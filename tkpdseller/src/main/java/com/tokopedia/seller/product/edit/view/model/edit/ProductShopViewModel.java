
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductShopViewModel implements Parcelable{

    @SerializedName("shop_id")
    @Expose
    private long shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;

    public long getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopDomain);
        dest.writeString(this.shopUrl);
    }

    public ProductShopViewModel() {
    }

    protected ProductShopViewModel(Parcel in) {
        this.shopId = in.readLong();
        this.shopName = in.readString();
        this.shopDomain = in.readString();
        this.shopUrl = in.readString();
    }

    public static final Creator<ProductShopViewModel> CREATOR = new Creator<ProductShopViewModel>() {
        @Override
        public ProductShopViewModel createFromParcel(Parcel source) {
            return new ProductShopViewModel(source);
        }

        @Override
        public ProductShopViewModel[] newArray(int size) {
            return new ProductShopViewModel[size];
        }
    };
}
