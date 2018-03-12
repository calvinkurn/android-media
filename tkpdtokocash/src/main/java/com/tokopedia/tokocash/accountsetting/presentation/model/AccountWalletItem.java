package com.tokopedia.tokocash.accountsetting.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 11/6/17.
 */

public class AccountWalletItem implements Parcelable {

    private String clientId;
    private String clientName;
    private String identifier;
    private String identifierType;
    private String imgUrl;
    private String authDateFmt;
    private String refreshToken;

    public AccountWalletItem() {
    }

    protected AccountWalletItem(Parcel in) {
        clientId = in.readString();
        clientName = in.readString();
        identifier = in.readString();
        identifierType = in.readString();
        imgUrl = in.readString();
        authDateFmt = in.readString();
        refreshToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientId);
        dest.writeString(clientName);
        dest.writeString(identifier);
        dest.writeString(identifierType);
        dest.writeString(imgUrl);
        dest.writeString(authDateFmt);
        dest.writeString(refreshToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountWalletItem> CREATOR = new Creator<AccountWalletItem>() {
        @Override
        public AccountWalletItem createFromParcel(Parcel in) {
            return new AccountWalletItem(in);
        }

        @Override
        public AccountWalletItem[] newArray(int size) {
            return new AccountWalletItem[size];
        }
    };

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAuthDateFmt() {
        return authDateFmt;
    }

    public void setAuthDateFmt(String authDateFmt) {
        this.authDateFmt = authDateFmt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}