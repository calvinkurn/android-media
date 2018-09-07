package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationCreateTimeDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationCreateTimeDomain> CREATOR = new Parcelable.Creator<ConversationCreateTimeDomain>() {
        @Override
        public ConversationCreateTimeDomain createFromParcel(Parcel source) {
            return new ConversationCreateTimeDomain(source);
        }

        @Override
        public ConversationCreateTimeDomain[] newArray(int size) {
            return new ConversationCreateTimeDomain[size];
        }
    };
    private String timestamp;
    private String string;

    public ConversationCreateTimeDomain(String timestamp, String string) {
        this.timestamp = timestamp;
        this.string = string;
    }

    protected ConversationCreateTimeDomain(Parcel in) {
        this.timestamp = in.readString();
        this.string = in.readString();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timestamp);
        dest.writeString(this.string);
    }
}
