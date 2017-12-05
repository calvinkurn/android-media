package com.tokopedia.session.login.loginphonenumber.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author by nisie on 12/5/17.
 */

public class ChooseTokoCashAccountViewModel implements Parcelable{

    ArrayList<AccountTokocash> listAccount;
    String phoneNumber;

    public ChooseTokoCashAccountViewModel(ArrayList<AccountTokocash> listAccount, String phoneNumber) {
        this.listAccount = listAccount;
        this.phoneNumber = phoneNumber;
    }

    protected ChooseTokoCashAccountViewModel(Parcel in) {
        listAccount = in.createTypedArrayList(AccountTokocash.CREATOR);
        phoneNumber = in.readString();
    }

    public static final Creator<ChooseTokoCashAccountViewModel> CREATOR = new Creator<ChooseTokoCashAccountViewModel>() {
        @Override
        public ChooseTokoCashAccountViewModel createFromParcel(Parcel in) {
            return new ChooseTokoCashAccountViewModel(in);
        }

        @Override
        public ChooseTokoCashAccountViewModel[] newArray(int size) {
            return new ChooseTokoCashAccountViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listAccount);
        dest.writeString(phoneNumber);
    }

    public ArrayList<AccountTokocash> getListAccount() {
        return listAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
