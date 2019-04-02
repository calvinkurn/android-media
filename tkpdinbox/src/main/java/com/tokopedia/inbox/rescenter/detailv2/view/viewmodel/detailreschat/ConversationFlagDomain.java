package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationFlagDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationFlagDomain> CREATOR = new Parcelable.Creator<ConversationFlagDomain>() {
        @Override
        public ConversationFlagDomain createFromParcel(Parcel source) {
            return new ConversationFlagDomain(source);
        }

        @Override
        public ConversationFlagDomain[] newArray(int size) {
            return new ConversationFlagDomain[size];
        }
    };
    private int system;
    private int solution;

    public ConversationFlagDomain(int system, int solution) {
        this.system = system;
        this.solution = solution;
    }

    protected ConversationFlagDomain(Parcel in) {
        this.system = in.readInt();
        this.solution = in.readInt();
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int systrem) {
        this.system = systrem;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.system);
        dest.writeInt(this.solution);
    }
}
