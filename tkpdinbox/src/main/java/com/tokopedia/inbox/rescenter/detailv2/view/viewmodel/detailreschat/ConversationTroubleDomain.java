package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationTroubleDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationTroubleDomain> CREATOR = new Parcelable.Creator<ConversationTroubleDomain>() {
        @Override
        public ConversationTroubleDomain createFromParcel(Parcel source) {
            return new ConversationTroubleDomain(source);
        }

        @Override
        public ConversationTroubleDomain[] newArray(int size) {
            return new ConversationTroubleDomain[size];
        }
    };
    private String string;

    public ConversationTroubleDomain(String string) {
        this.string = string;
    }

    protected ConversationTroubleDomain(Parcel in) {
        this.string = in.readString();
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
        dest.writeString(this.string);
    }
}
