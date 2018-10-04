package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 27/08/18.
 */
public class CurrentSolutionViewModel implements Parcelable {

    private int id;
    private String name;
    private String message;
    private String identifier;
    private SolutionProblemAmountModel amount;

    public CurrentSolutionViewModel(int id, String name, String message, String identifier, SolutionProblemAmountModel amount) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.identifier = identifier;
        this.amount = amount;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SolutionProblemAmountModel getAmount() {
        return amount;
    }

    public void setAmount(SolutionProblemAmountModel amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.message);
        dest.writeString(this.identifier);
        dest.writeParcelable(this.amount, flags);
    }

    protected CurrentSolutionViewModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.message = in.readString();
        this.identifier = in.readString();
        this.amount = in.readParcelable(SolutionProblemAmountModel.class.getClassLoader());
    }

    public static final Creator<CurrentSolutionViewModel> CREATOR = new Creator<CurrentSolutionViewModel>() {
        @Override
        public CurrentSolutionViewModel createFromParcel(Parcel source) {
            return new CurrentSolutionViewModel(source);
        }

        @Override
        public CurrentSolutionViewModel[] newArray(int size) {
            return new CurrentSolutionViewModel[size];
        }
    };
}
