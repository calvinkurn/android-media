package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemAmountModel implements Parcelable {

    private String idr;
    private int integer;

    public SolutionProblemAmountModel(String idr, int integer) {
        this.idr = idr;
        this.integer = integer;
    }

    public String getIdr() {
        return idr;
    }

    public void setIdr(String idr) {
        this.idr = idr;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idr);
        dest.writeInt(this.integer);
    }

    protected SolutionProblemAmountModel(Parcel in) {
        this.idr = in.readString();
        this.integer = in.readInt();
    }

    public static final Parcelable.Creator<SolutionProblemAmountModel> CREATOR = new Parcelable.Creator<SolutionProblemAmountModel>() {
        @Override
        public SolutionProblemAmountModel createFromParcel(Parcel source) {
            return new SolutionProblemAmountModel(source);
        }

        @Override
        public SolutionProblemAmountModel[] newArray(int size) {
            return new SolutionProblemAmountModel[size];
        }
    };
}
