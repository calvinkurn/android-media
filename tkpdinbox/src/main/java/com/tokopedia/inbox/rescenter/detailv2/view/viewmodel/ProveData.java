package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 10/11/17.
 */

public class ProveData implements Parcelable {

    public static final Parcelable.Creator<ProveData> CREATOR = new Parcelable.Creator<ProveData>() {
        @Override
        public ProveData createFromParcel(Parcel source) {
            return new ProveData(source);
        }

        @Override
        public ProveData[] newArray(int size) {
            return new ProveData[size];
        }
    };
    private String remark;
    private List<AttachmentData> attachment;

    public ProveData() {
    }

    protected ProveData(Parcel in) {
        this.remark = in.readString();
        this.attachment = in.createTypedArrayList(AttachmentData.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remark);
        dest.writeTypedList(this.attachment);
    }
}
