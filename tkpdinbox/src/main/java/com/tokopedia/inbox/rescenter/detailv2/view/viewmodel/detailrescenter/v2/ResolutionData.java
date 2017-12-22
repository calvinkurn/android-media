package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class ResolutionData implements Parcelable {

    private int id;
    private StatusData status;
    private CreateByData createBy;
    private String createTime;
    private String createTimeStr;
    private String expireTime;
    private String expireTimeStr;
    private CreateByData updateBy;
    private String updateTime;
    private int freeReturn;
    private String freeReturnText;
    private String freeReturnLink;

    public ResolutionData(int id,
                          StatusData status,
                          CreateByData createBy,
                          String createTime,
                          String createTimeStr,
                          String expireTime,
                          String expireTimeStr,
                          CreateByData updateBy,
                          String updateTime,
                          int freeReturn,
                          String freeReturnText,
                          String freeReturnLink) {
        this.id = id;
        this.status = status;
        this.createBy = createBy;
        this.createTime = createTime;
        this.createTimeStr = createTimeStr;
        this.expireTime = expireTime;
        this.expireTimeStr = expireTimeStr;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.freeReturn = freeReturn;
        this.freeReturnText = freeReturnText;
        this.freeReturnLink = freeReturnLink;
    }

    public String getFreeReturnLink() {
        return freeReturnLink;
    }

    public void setFreeReturnLink(String freeReturnLink) {
        this.freeReturnLink = freeReturnLink;
    }

    public String getFreeReturnText() {
        return freeReturnText;
    }

    public void setFreeReturnText(String freeReturnText) {
        this.freeReturnText = freeReturnText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusData getStatus() {
        return status;
    }

    public void setStatus(StatusData status) {
        this.status = status;
    }

    public CreateByData getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateByData createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getExpireTimeStr() {
        return expireTimeStr;
    }

    public void setExpireTimeStr(String expireTimeStr) {
        this.expireTimeStr = expireTimeStr;
    }

    public CreateByData getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(CreateByData updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.createBy, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.createTimeStr);
        dest.writeString(this.expireTime);
        dest.writeString(this.expireTimeStr);
        dest.writeParcelable(this.updateBy, flags);
        dest.writeString(this.updateTime);
        dest.writeInt(this.freeReturn);
        dest.writeString(this.freeReturnText);
        dest.writeString(this.freeReturnLink);
    }

    protected ResolutionData(Parcel in) {
        this.id = in.readInt();
        this.status = in.readParcelable(StatusData.class.getClassLoader());
        this.createBy = in.readParcelable(CreateByData.class.getClassLoader());
        this.createTime = in.readString();
        this.createTimeStr = in.readString();
        this.expireTime = in.readString();
        this.expireTimeStr = in.readString();
        this.updateBy = in.readParcelable(CreateByData.class.getClassLoader());
        this.updateTime = in.readString();
        this.freeReturn = in.readInt();
        this.freeReturnText = in.readString();
        this.freeReturnLink = in.readString();
    }

    public static final Creator<ResolutionData> CREATOR = new Creator<ResolutionData>() {
        @Override
        public ResolutionData createFromParcel(Parcel source) {
            return new ResolutionData(source);
        }

        @Override
        public ResolutionData[] newArray(int size) {
            return new ResolutionData[size];
        }
    };
}