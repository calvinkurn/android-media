package com.tokopedia.session.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 10/18/17.
 */

public class SecurityDomain implements Parcelable{
    private final int allowLogin;
    private final int userCheckSecurity1;
    private final int userCheckSecurity2;

    public SecurityDomain(int allowLogin, int userCheckSecurity1, int userCheckSecurity2) {
        this.allowLogin = allowLogin;
        this.userCheckSecurity1 = userCheckSecurity1;
        this.userCheckSecurity2 = userCheckSecurity2;
    }

    protected SecurityDomain(Parcel in) {
        allowLogin = in.readInt();
        userCheckSecurity1 = in.readInt();
        userCheckSecurity2 = in.readInt();
    }

    public static final Creator<SecurityDomain> CREATOR = new Creator<SecurityDomain>() {
        @Override
        public SecurityDomain createFromParcel(Parcel in) {
            return new SecurityDomain(in);
        }

        @Override
        public SecurityDomain[] newArray(int size) {
            return new SecurityDomain[size];
        }
    };

    public int getAllowLogin() {
        return allowLogin;
    }

    public int getUserCheckSecurity1() {
        return userCheckSecurity1;
    }

    public int getUserCheckSecurity2() {
        return userCheckSecurity2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(allowLogin);
        dest.writeInt(userCheckSecurity1);
        dest.writeInt(userCheckSecurity2);
    }
}
