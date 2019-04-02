package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDetailDomain implements Parcelable {

    public static final Creator<NextActionDetailDomain> CREATOR = new Creator<NextActionDetailDomain>() {
        @Override
        public NextActionDetailDomain createFromParcel(Parcel source) {
            return new NextActionDetailDomain(source);
        }

        @Override
        public NextActionDetailDomain[] newArray(int size) {
            return new NextActionDetailDomain[size];
        }
    };
    private String solution;
    private LastDomain last;
    private List<NextActionDetailStepDomain> step;

    public NextActionDetailDomain(String solution, LastDomain last, List<NextActionDetailStepDomain> step) {
        this.solution = solution;
        this.last = last;
        this.step = step;
    }

    protected NextActionDetailDomain(Parcel in) {
        this.solution = in.readString();
        this.last = in.readParcelable(LastDomain.class.getClassLoader());
        this.step = in.createTypedArrayList(NextActionDetailStepDomain.CREATOR);
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<NextActionDetailStepDomain> getStep() {
        return step;
    }

    public void setStep(List<NextActionDetailStepDomain> step) {
        this.step = step;
    }

    public LastDomain getLast() {
        return last;
    }

    public void setLast(LastDomain last) {
        this.last = last;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.solution);
        dest.writeParcelable(this.last, flags);
        dest.writeTypedList(this.step);
    }
}
