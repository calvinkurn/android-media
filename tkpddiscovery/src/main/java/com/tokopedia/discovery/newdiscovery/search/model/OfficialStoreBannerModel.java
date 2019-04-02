package com.tokopedia.discovery.newdiscovery.search.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class OfficialStoreBannerModel implements Parcelable {

    @SerializedName("banner_url")
    private String bannerUrl = "";
    private String keyword = "";
    @SerializedName("process-time")
    private String processtime = "";
    @SerializedName("shop_url")
    private String shopUrl = "";
    @SerializedName("status")
    private String status = "";

    public OfficialStoreBannerModel() {
    }

    protected OfficialStoreBannerModel(Parcel in) {
        bannerUrl = in.readString();
        keyword = in.readString();
        processtime = in.readString();
        shopUrl = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bannerUrl);
        dest.writeString(keyword);
        dest.writeString(processtime);
        dest.writeString(shopUrl);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfficialStoreBannerModel> CREATOR = new Creator<OfficialStoreBannerModel>() {
        @Override
        public OfficialStoreBannerModel createFromParcel(Parcel in) {
            return new OfficialStoreBannerModel(in);
        }

        @Override
        public OfficialStoreBannerModel[] newArray(int size) {
            return new OfficialStoreBannerModel[size];
        }
    };

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProcesstime() {
        return processtime;
    }

    public void setProcesstime(String processtime) {
        this.processtime = processtime;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
