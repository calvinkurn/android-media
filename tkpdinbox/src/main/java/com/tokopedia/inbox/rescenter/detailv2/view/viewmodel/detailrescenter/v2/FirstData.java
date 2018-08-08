package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class FirstData implements Parcelable {

    public static final Creator<FirstData> CREATOR = new Creator<FirstData>() {
        @Override
        public FirstData createFromParcel(Parcel source) {
            return new FirstData(source);
        }

        @Override
        public FirstData[] newArray(int size) {
            return new FirstData[size];
        }
    };
    private String buyerRemark;
    private List<AttachmentDataDomain> attachments;

    public FirstData(String buyerRemark, List<AttachmentDataDomain> attachments) {
        this.buyerRemark = buyerRemark;
        this.attachments = attachments;
    }

    protected FirstData(Parcel in) {
        this.buyerRemark = in.readString();
        this.attachments = in.createTypedArrayList(AttachmentDataDomain.CREATOR);
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    public List<AttachmentDataDomain> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDataDomain> attachments) {
        this.attachments = attachments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.buyerRemark);
        dest.writeTypedList(this.attachments);
    }
}
