package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 10/11/17.
 */

public class ProveData implements Parcelable {

    private String remark;
    private List<AttachmentData> attachment;
    private List<AttachmentData> buyerAttachmentList;
    private List<AttachmentData> sellerAttachmentList;
    private List<AttachmentData> adminAttachmentList;
    private boolean canShowProveData;

    public ProveData() {
    }

    public boolean isCanShowProveData() {
        return canShowProveData;
    }

    public void setCanShowProveData(boolean canShowProveData) {
        this.canShowProveData = canShowProveData;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AttachmentData> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentData> attachment) {
        this.attachment = attachment;
    }

    public List<AttachmentData> getBuyerAttachmentList() {
        return buyerAttachmentList;
    }

    public void setBuyerAttachmentList(List<AttachmentData> buyerAttachmentList) {
        this.buyerAttachmentList = buyerAttachmentList;
    }

    public List<AttachmentData> getSellerAttachmentList() {
        return sellerAttachmentList;
    }

    public void setSellerAttachmentList(List<AttachmentData> sellerAttachmentList) {
        this.sellerAttachmentList = sellerAttachmentList;
    }

    public List<AttachmentData> getAdminAttachmentList() {
        return adminAttachmentList;
    }

    public void setAdminAttachmentList(List<AttachmentData> adminAttachmentList) {
        this.adminAttachmentList = adminAttachmentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remark);
        dest.writeTypedList(this.attachment);
        dest.writeTypedList(this.buyerAttachmentList);
        dest.writeTypedList(this.sellerAttachmentList);
        dest.writeTypedList(this.adminAttachmentList);
        dest.writeByte(this.canShowProveData ? (byte) 1 : (byte) 0);
    }

    protected ProveData(Parcel in) {
        this.remark = in.readString();
        this.attachment = in.createTypedArrayList(AttachmentData.CREATOR);
        this.buyerAttachmentList = in.createTypedArrayList(AttachmentData.CREATOR);
        this.sellerAttachmentList = in.createTypedArrayList(AttachmentData.CREATOR);
        this.adminAttachmentList = in.createTypedArrayList(AttachmentData.CREATOR);
        this.canShowProveData = in.readByte() != 0;
    }

    public static final Creator<ProveData> CREATOR = new Creator<ProveData>() {
        @Override
        public ProveData createFromParcel(Parcel source) {
            return new ProveData(source);
        }

        @Override
        public ProveData[] newArray(int size) {
            return new ProveData[size];
        }
    };
}
