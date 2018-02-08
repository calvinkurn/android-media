package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionResponseViewModel implements Parcelable {

    private List<SolutionViewModel> solutionViewModelList;
    private RequireViewModel require;
    private FreeReturnViewModel freeReturn;

    public SolutionResponseViewModel(List<SolutionViewModel> solutionViewModelList, RequireViewModel require, FreeReturnViewModel freeReturn) {
        this.solutionViewModelList = solutionViewModelList;
        this.require = require;
        this.freeReturn = freeReturn;
    }

    public FreeReturnViewModel getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(FreeReturnViewModel freeReturn) {
        this.freeReturn = freeReturn;
    }

    public List<SolutionViewModel> getSolutionViewModelList() {
        return solutionViewModelList;
    }

    public void setSolutionViewModelList(List<SolutionViewModel> solutionViewModelList) {
        this.solutionViewModelList = solutionViewModelList;
    }

    public RequireViewModel getRequire() {
        return require;
    }

    public void setRequire(RequireViewModel require) {
        this.require = require;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.solutionViewModelList);
        dest.writeParcelable(this.require, flags);
        dest.writeParcelable(this.freeReturn, flags);
    }

    protected SolutionResponseViewModel(Parcel in) {
        this.solutionViewModelList = in.createTypedArrayList(SolutionViewModel.CREATOR);
        this.require = in.readParcelable(RequireViewModel.class.getClassLoader());
        this.freeReturn = in.readParcelable(FreeReturnViewModel.class.getClassLoader());
    }

    public static final Creator<SolutionResponseViewModel> CREATOR = new Creator<SolutionResponseViewModel>() {
        @Override
        public SolutionResponseViewModel createFromParcel(Parcel source) {
            return new SolutionResponseViewModel(source);
        }

        @Override
        public SolutionResponseViewModel[] newArray(int size) {
            return new SolutionResponseViewModel[size];
        }
    };
}
