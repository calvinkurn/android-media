package com.tokopedia.session.session.model;

import android.os.Parcelable;

/**
 * Created by m.normansyah on 18/11/2015.
 */
public class ForgotPasswordViewModel implements Parcelable {
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.email);
    }

    public ForgotPasswordViewModel() {
    }

    protected ForgotPasswordViewModel(android.os.Parcel in) {
        this.email = in.readString();
    }

    public static final Parcelable.Creator<ForgotPasswordViewModel> CREATOR = new Parcelable.Creator<ForgotPasswordViewModel>() {
        @Override
        public ForgotPasswordViewModel createFromParcel(android.os.Parcel source) {
            return new ForgotPasswordViewModel(source);
        }

        @Override
        public ForgotPasswordViewModel[] newArray(int size) {
            return new ForgotPasswordViewModel[size];
        }
    };
}
