package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class RequireViewModel implements Parcelable {

    private boolean attachment;

    public RequireViewModel(boolean attachment) {
        this.attachment = attachment;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.attachment ? (byte) 1 : (byte) 0);
    }

    protected RequireViewModel(Parcel in) {
        this.attachment = in.readByte() != 0;
    }

    public static final Creator<RequireViewModel> CREATOR = new Creator<RequireViewModel>() {
        @Override
        public RequireViewModel createFromParcel(Parcel source) {
            return new RequireViewModel(source);
        }

        @Override
        public RequireViewModel[] newArray(int size) {
            return new RequireViewModel[size];
        }
    };
}
