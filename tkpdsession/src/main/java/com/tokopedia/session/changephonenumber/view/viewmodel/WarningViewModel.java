package com.tokopedia.session.changephonenumber.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningViewModel implements Parcelable {
    public static final String EMPTY_BALANCE = "Rp  0";
    public static final String ACTION_EMAIL = "send_email";
    public static final String ACTION_OTP = "send_otp";

    private String tokopediaBalance;
    private String tokocash;
    private String action;
    private List<String> warningList;
    private boolean hasBankAccount;

    public WarningViewModel() {
    }

    public String getTokopediaBalance() {
        return tokopediaBalance;
    }

    public void setTokopediaBalance(String tokopediaBalance) {
        this.tokopediaBalance = tokopediaBalance;
    }

    public String getTokocash() {
        return tokocash;
    }

    public void setTokocash(String tokocash) {
        this.tokocash = tokocash;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<String> warningList) {
        this.warningList = warningList;
    }

    public boolean isHasBankAccount() {
        return hasBankAccount;
    }

    public void setHasBankAccount(boolean hasBankAccount) {
        this.hasBankAccount = hasBankAccount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokopediaBalance);
        dest.writeString(this.tokocash);
        dest.writeString(this.action);
        dest.writeStringList(this.warningList);
        dest.writeByte(this.hasBankAccount ? (byte) 1 : (byte) 0);
    }

    protected WarningViewModel(Parcel in) {
        this.tokopediaBalance = in.readString();
        this.tokocash = in.readString();
        this.action = in.readString();
        this.warningList = in.createStringArrayList();
        this.hasBankAccount = in.readByte() != 0;
    }

    public static final Creator<WarningViewModel> CREATOR = new Creator<WarningViewModel>() {
        @Override
        public WarningViewModel createFromParcel(Parcel source) {
            return new WarningViewModel(source);
        }

        @Override
        public WarningViewModel[] newArray(int size) {
            return new WarningViewModel[size];
        }
    };
}
