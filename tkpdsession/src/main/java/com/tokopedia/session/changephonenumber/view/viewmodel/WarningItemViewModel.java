package com.tokopedia.session.changephonenumber.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningItemViewModel implements Parcelable {
    private String warning;
    private String suggestion;
    private String note;

    public WarningItemViewModel(String warning, String suggestion, String note) {
        this.warning = warning;
        this.suggestion = suggestion;
        this.note = note;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.warning);
        dest.writeString(this.suggestion);
        dest.writeString(this.note);
    }

    public WarningItemViewModel() {
    }

    protected WarningItemViewModel(Parcel in) {
        this.warning = in.readString();
        this.suggestion = in.readString();
        this.note = in.readString();
    }

    public static final Parcelable.Creator<WarningItemViewModel> CREATOR = new Parcelable.Creator<WarningItemViewModel>() {
        @Override
        public WarningItemViewModel createFromParcel(Parcel source) {
            return new WarningItemViewModel(source);
        }

        @Override
        public WarningItemViewModel[] newArray(int size) {
            return new WarningItemViewModel[size];
        }
    };
}
