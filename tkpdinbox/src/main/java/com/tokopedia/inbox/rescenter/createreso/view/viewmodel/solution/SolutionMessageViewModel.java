package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionMessageViewModel implements Parcelable {
    private String message;

    public SolutionMessageViewModel(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
    }

    protected SolutionMessageViewModel(Parcel in) {
        this.message = in.readString();
    }

    public static final Creator<SolutionMessageViewModel> CREATOR = new Creator<SolutionMessageViewModel>() {
        @Override
        public SolutionMessageViewModel createFromParcel(Parcel source) {
            return new SolutionMessageViewModel(source);
        }

        @Override
        public SolutionMessageViewModel[] newArray(int size) {
            return new SolutionMessageViewModel[size];
        }
    };
}
