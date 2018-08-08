package com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 10/12/17. Tokopedia
 */

public class AuthenticatorPageModel implements Parcelable{

    private int state;

    private String userEmail;

    public AuthenticatorPageModel() {
    }

    protected AuthenticatorPageModel(Parcel in) {
        state = in.readInt();
        userEmail = in.readString();
    }

    public static final Creator<AuthenticatorPageModel> CREATOR = new Creator<AuthenticatorPageModel>() {
        @Override
        public AuthenticatorPageModel createFromParcel(Parcel in) {
            return new AuthenticatorPageModel(in);
        }

        @Override
        public AuthenticatorPageModel[] newArray(int size) {
            return new AuthenticatorPageModel[size];
        }
    };

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(userEmail);
    }
}
