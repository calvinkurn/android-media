package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 12/12/17.
 */

public class AttachmentUserData implements Parcelable {

    private int actionBy;
    private List<AttachmentDataDomain> attachments;

    public AttachmentUserData(int actionBy, List<AttachmentDataDomain> attachments) {
        this.actionBy = actionBy;
        this.attachments = attachments;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
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
        dest.writeInt(this.actionBy);
        dest.writeTypedList(this.attachments);
    }

    protected AttachmentUserData(Parcel in) {
        this.actionBy = in.readInt();
        this.attachments = in.createTypedArrayList(AttachmentDataDomain.CREATOR);
    }

    public static final Parcelable.Creator<AttachmentUserData> CREATOR = new Parcelable.Creator<AttachmentUserData>() {
        @Override
        public AttachmentUserData createFromParcel(Parcel source) {
            return new AttachmentUserData(source);
        }

        @Override
        public AttachmentUserData[] newArray(int size) {
            return new AttachmentUserData[size];
        }
    };
}
