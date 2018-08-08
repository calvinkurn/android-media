package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionViewModel implements Parcelable {

    private int id;
    private String name;
    private String solutionName;
    private AmountViewModel amount;

    public SolutionViewModel(int id, String name, String solutionName, AmountViewModel amount) {
        this.id = id;
        this.name = name;
        this.solutionName = solutionName;
        this.amount = amount;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmountViewModel getAmount() {
        return amount;
    }

    public void setAmount(AmountViewModel amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.solutionName);
        dest.writeParcelable(this.amount, flags);
    }

    protected SolutionViewModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.solutionName = in.readString();
        this.amount = in.readParcelable(AmountViewModel.class.getClassLoader());
    }

    public static final Creator<SolutionViewModel> CREATOR = new Creator<SolutionViewModel>() {
        @Override
        public SolutionViewModel createFromParcel(Parcel source) {
            return new SolutionViewModel(source);
        }

        @Override
        public SolutionViewModel[] newArray(int size) {
            return new SolutionViewModel[size];
        }
    };
}
