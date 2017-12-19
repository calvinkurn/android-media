package com.tokopedia.session.changephonenumber.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningViewModel implements Parcelable {
    private String tokopediaBalance;
    private String tokocash;
    private List<WarningItemViewModel> warningList;

    public WarningViewModel(String tokopediaBalance, String tokocash, List<WarningItemViewModel> warningList) {
        this.tokopediaBalance = tokopediaBalance;
        this.tokocash = tokocash;
        this.warningList = warningList;
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

    public List<WarningItemViewModel> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<WarningItemViewModel> warningList) {
        this.warningList = warningList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokopediaBalance);
        dest.writeString(this.tokocash);
        dest.writeList(this.warningList);
    }

    public WarningViewModel() {
    }

    protected WarningViewModel(Parcel in) {
        this.tokopediaBalance = in.readString();
        this.tokocash = in.readString();
        this.warningList = new ArrayList<WarningItemViewModel>();
        in.readList(this.warningList, WarningItemViewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<WarningViewModel> CREATOR = new Parcelable.Creator<WarningViewModel>() {
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
