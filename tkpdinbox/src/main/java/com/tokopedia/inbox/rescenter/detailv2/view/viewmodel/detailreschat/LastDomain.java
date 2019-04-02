package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class LastDomain implements Parcelable {
    public static final Parcelable.Creator<LastDomain> CREATOR = new Parcelable.Creator<LastDomain>() {
        @Override
        public LastDomain createFromParcel(Parcel source) {
            return new LastDomain(source);
        }

        @Override
        public LastDomain[] newArray(int size) {
            return new LastDomain[size];
        }
    };
    private LastSolutionDomain solution;
    private String problem;

    public LastDomain(LastSolutionDomain solution, String problem) {
        this.solution = solution;
        this.problem = problem;
    }

    protected LastDomain(Parcel in) {
        this.solution = in.readParcelable(LastSolutionDomain.class.getClassLoader());
        this.problem = in.readString();
    }

    public LastSolutionDomain getSolution() {
        return solution;
    }

    public void setSolution(LastSolutionDomain solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.solution, flags);
        dest.writeString(this.problem);
    }
}
