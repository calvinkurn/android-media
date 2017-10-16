package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationActionDomain implements Parcelable {

    private String type;
    private int by;

    public ConversationActionDomain(String type, int by) {
        this.type = type;
        this.by = by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBy() {
        return by;
    }

    public void setBy(int by) {
        this.by = by;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeInt(this.by);
    }

    protected ConversationActionDomain(Parcel in) {
        this.type = in.readString();
        this.by = in.readInt();
    }

    public static final Parcelable.Creator<ConversationActionDomain> CREATOR = new Parcelable.Creator<ConversationActionDomain>() {
        @Override
        public ConversationActionDomain createFromParcel(Parcel source) {
            return new ConversationActionDomain(source);
        }

        @Override
        public ConversationActionDomain[] newArray(int size) {
            return new ConversationActionDomain[size];
        }
    };
}
