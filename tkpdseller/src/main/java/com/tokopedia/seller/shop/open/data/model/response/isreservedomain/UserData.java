
package com.tokopedia.seller.shop.open.data.model.response.isreservedomain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {

    @SerializedName("loc_complete")
    @Expose
    private String locComplete;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("photo_obj")
    @Expose
    private String photoObj;
    @SerializedName("reserve_time")
    @Expose
    private String reserveTime;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("shipment")
    @Expose
    private String shipment;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("tag_line")
    @Expose
    private String tagLine;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getLocComplete() {
        return locComplete;
    }

    public void setLocComplete(String locComplete) {
        this.locComplete = locComplete;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhotoObj() {
        return photoObj;
    }

    public void setPhotoObj(String photoObj) {
        this.photoObj = photoObj;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locComplete);
        dest.writeString(this.location);
        dest.writeString(this.logo);
        dest.writeString(this.photoObj);
        dest.writeString(this.reserveTime);
        dest.writeString(this.serverId);
        dest.writeString(this.shipment);
        dest.writeString(this.shopDomain);
        dest.writeString(this.shopName);
        dest.writeString(this.shortDesc);
        dest.writeString(this.step);
        dest.writeString(this.tagLine);
        dest.writeString(this.userId);
    }

    public UserData() {
    }

    protected UserData(Parcel in) {
        this.locComplete = in.readString();
        this.location = in.readString();
        this.logo = in.readString();
        this.photoObj = in.readString();
        this.reserveTime = in.readString();
        this.serverId = in.readString();
        this.shipment = in.readString();
        this.shopDomain = in.readString();
        this.shopName = in.readString();
        this.shortDesc = in.readString();
        this.step = in.readString();
        this.tagLine = in.readString();
        this.userId = in.readString();
    }

    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
