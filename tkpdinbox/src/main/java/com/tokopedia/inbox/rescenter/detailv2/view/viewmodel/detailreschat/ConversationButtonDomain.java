package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationButtonDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationButtonDomain> CREATOR = new Parcelable.Creator<ConversationButtonDomain>() {
        @Override
        public ConversationButtonDomain createFromParcel(Parcel source) {
            return new ConversationButtonDomain(source);
        }

        @Override
        public ConversationButtonDomain[] newArray(int size) {
            return new ConversationButtonDomain[size];
        }
    };
    private int trackAwb;
    private int editAwb;
    private int editAddress;

    public ConversationButtonDomain(int trackAwb, int editAwb, int editAddress) {
        this.trackAwb = trackAwb;
        this.editAwb = editAwb;
        this.editAddress = editAddress;
    }

    protected ConversationButtonDomain(Parcel in) {
        this.trackAwb = in.readInt();
        this.editAwb = in.readInt();
        this.editAddress = in.readInt();
    }

    public int getTrackAwb() {
        return trackAwb;
    }

    public void setTrackAwb(int trackAwb) {
        this.trackAwb = trackAwb;
    }

    public int getEditAwb() {
        return editAwb;
    }

    public void setEditAwb(int editAwb) {
        this.editAwb = editAwb;
    }

    public int getEditAddress() {
        return editAddress;
    }

    public void setEditAddress(int editAddress) {
        this.editAddress = editAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.trackAwb);
        dest.writeInt(this.editAwb);
        dest.writeInt(this.editAddress);
    }
}
