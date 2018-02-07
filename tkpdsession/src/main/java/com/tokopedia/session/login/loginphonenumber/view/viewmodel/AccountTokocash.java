package com.tokopedia.session.login.loginphonenumber.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 12/4/17.
 */

public class AccountTokocash implements Parcelable{

    int userId;
    String name;
    String email;
    String avatarUrl;

    public AccountTokocash(int userId, String name, String email, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    protected AccountTokocash(Parcel in) {
        userId = in.readInt();
        name = in.readString();
        email = in.readString();
        avatarUrl = in.readString();
    }

    public static final Creator<AccountTokocash> CREATOR = new Creator<AccountTokocash>() {
        @Override
        public AccountTokocash createFromParcel(Parcel in) {
            return new AccountTokocash(in);
        }

        @Override
        public AccountTokocash[] newArray(int size) {
            return new AccountTokocash[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(avatarUrl);
    }
}
