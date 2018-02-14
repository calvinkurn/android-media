package com.tokopedia.session.activation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 4/18/17.
 */

public class LoginTokenViewModel implements Parcelable{
    String accessToken;
    String expiresIn;
    String tokenType;

    public LoginTokenViewModel() {
    }

    protected LoginTokenViewModel(Parcel in) {
        accessToken = in.readString();
        expiresIn = in.readString();
        tokenType = in.readString();
    }

    public static final Creator<LoginTokenViewModel> CREATOR = new Creator<LoginTokenViewModel>() {
        @Override
        public LoginTokenViewModel createFromParcel(Parcel in) {
            return new LoginTokenViewModel(in);
        }

        @Override
        public LoginTokenViewModel[] newArray(int size) {
            return new LoginTokenViewModel[size];
        }
    };

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(expiresIn);
        dest.writeString(tokenType);
    }

    public String getConcatedToken() {
        return tokenType + " " + accessToken;
    }
}
