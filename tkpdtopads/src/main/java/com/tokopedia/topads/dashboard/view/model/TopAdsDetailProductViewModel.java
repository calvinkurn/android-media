package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public class TopAdsDetailProductViewModel implements TopAdsDetailAdViewModel, Parcelable {

    private long id;
    private int type;
    private long groupId;
    private long shopId;
    private long itemId;
    private int status;
    private float priceBid;
    private boolean budget;
    private float priceDaily;
    private int stickerId;
    private boolean scheduled;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String image;
    private String title;
    private boolean toggled;
    private long suggestionBidValue;
    private String suggestionBidButton;
    private boolean enoughDeposit;

    @Override
    public int getStickerId() {
        return stickerId;
    }

    @Override
    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public float getPriceBid() {
        return priceBid;
    }

    @Override
    public void setPriceBid(float priceBid) {
        this.priceBid = priceBid;
    }

    @Override
    public boolean isBudget() {
        return budget;
    }

    @Override
    public void setBudget(boolean budget) {
        this.budget = budget;
    }

    @Override
    public float getPriceDaily() {
        return priceDaily;
    }

    @Override
    public void setPriceDaily(float priceDaily) {
        this.priceDaily = priceDaily;
    }

    @Override
    public boolean isScheduled() {
        return scheduled;
    }

    @Override
    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuggestionBidValue(long suggestionBidValue) {
        this.suggestionBidValue = suggestionBidValue;
    }

    public long getSuggestionBidValue() {
        return suggestionBidValue;
    }

    public void setSuggestionBidButton(String suggestionBidButton) {
        this.suggestionBidButton = suggestionBidButton;
    }

    public String getSuggestionBidButton() {
        return suggestionBidButton;
    }

    public TopAdsDetailProductViewModel() {
    }

    public boolean isEnoughDeposit() {
        return enoughDeposit;
    }

    public void setEnoughDeposit(boolean enoughDeposit) {
        this.enoughDeposit = enoughDeposit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.type);
        dest.writeLong(this.groupId);
        dest.writeLong(this.shopId);
        dest.writeLong(this.itemId);
        dest.writeInt(this.status);
        dest.writeFloat(this.priceBid);
        dest.writeByte(this.budget ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.priceDaily);
        dest.writeInt(this.stickerId);
        dest.writeByte(this.scheduled ? (byte) 1 : (byte) 0);
        dest.writeString(this.startDate);
        dest.writeString(this.startTime);
        dest.writeString(this.endDate);
        dest.writeString(this.endTime);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeByte(this.toggled ? (byte) 1 : (byte) 0);
        dest.writeLong(this.suggestionBidValue);
        dest.writeString(this.suggestionBidButton);
        dest.writeByte(this.enoughDeposit ? (byte) 1 : (byte) 0);
    }

    protected TopAdsDetailProductViewModel(Parcel in) {
        this.id = in.readLong();
        this.type = in.readInt();
        this.groupId = in.readLong();
        this.shopId = in.readLong();
        this.itemId = in.readLong();
        this.status = in.readInt();
        this.priceBid = in.readFloat();
        this.budget = in.readByte() != 0;
        this.priceDaily = in.readFloat();
        this.stickerId = in.readInt();
        this.scheduled = in.readByte() != 0;
        this.startDate = in.readString();
        this.startTime = in.readString();
        this.endDate = in.readString();
        this.endTime = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.toggled = in.readByte() != 0;
        this.suggestionBidValue = in.readLong();
        this.suggestionBidButton = in.readString();
        this.enoughDeposit = in.readByte() != 0;
    }

    public static final Creator<TopAdsDetailProductViewModel> CREATOR = new Creator<TopAdsDetailProductViewModel>() {
        @Override
        public TopAdsDetailProductViewModel createFromParcel(Parcel source) {
            return new TopAdsDetailProductViewModel(source);
        }

        @Override
        public TopAdsDetailProductViewModel[] newArray(int size) {
            return new TopAdsDetailProductViewModel[size];
        }
    };
}
