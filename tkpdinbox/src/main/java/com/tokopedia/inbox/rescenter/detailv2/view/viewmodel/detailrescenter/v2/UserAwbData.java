package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class UserAwbData implements Parcelable {

    private int resConvId;
    private String awb;
    private ShippingData shipping;
    private ByData by;
    private int trackable;
    private String createTime;
    private String createTimeStr;
    private String createTimeFullStr;
    private List<AttachmentDataDomain> attachments;

    public UserAwbData(int resConvId, String awb, ShippingData shipping, ByData by, int trackable, String createTime, String createTimeStr, String createTimeFullStr, List<AttachmentDataDomain> attachments) {
        this.resConvId = resConvId;
        this.awb = awb;
        this.shipping = shipping;
        this.by = by;
        this.trackable = trackable;
        this.createTime = createTime;
        this.createTimeStr = createTimeStr;
        this.createTimeFullStr = createTimeFullStr;
        this.attachments = attachments;
    }

    public String getCreateTimeFullStr() {
        return createTimeFullStr;
    }

    public void setCreateTimeFullStr(String createTimeFullStr) {
        this.createTimeFullStr = createTimeFullStr;
    }

    public List<AttachmentDataDomain> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDataDomain> attachments) {
        this.attachments = attachments;
    }

    public int getResConvId() {
        return resConvId;
    }

    public void setResConvId(int resConvId) {
        this.resConvId = resConvId;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public ShippingData getShipping() {
        return shipping;
    }

    public void setShipping(ShippingData shipping) {
        this.shipping = shipping;
    }

    public ByData getBy() {
        return by;
    }

    public void setBy(ByData by) {
        this.by = by;
    }

    public int getTrackable() {
        return trackable;
    }

    public void setTrackable(int trackable) {
        this.trackable = trackable;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resConvId);
        dest.writeString(this.awb);
        dest.writeParcelable(this.shipping, flags);
        dest.writeParcelable(this.by, flags);
        dest.writeInt(this.trackable);
        dest.writeString(this.createTime);
        dest.writeString(this.createTimeStr);
        dest.writeString(this.createTimeFullStr);
        dest.writeTypedList(this.attachments);
    }

    protected UserAwbData(Parcel in) {
        this.resConvId = in.readInt();
        this.awb = in.readString();
        this.shipping = in.readParcelable(ShippingData.class.getClassLoader());
        this.by = in.readParcelable(ByData.class.getClassLoader());
        this.trackable = in.readInt();
        this.createTime = in.readString();
        this.createTimeStr = in.readString();
        this.createTimeFullStr = in.readString();
        this.attachments = in.createTypedArrayList(AttachmentDataDomain.CREATOR);
    }

    public static final Creator<UserAwbData> CREATOR = new Creator<UserAwbData>() {
        @Override
        public UserAwbData createFromParcel(Parcel source) {
            return new UserAwbData(source);
        }

        @Override
        public UserAwbData[] newArray(int size) {
            return new UserAwbData[size];
        }
    };
}
