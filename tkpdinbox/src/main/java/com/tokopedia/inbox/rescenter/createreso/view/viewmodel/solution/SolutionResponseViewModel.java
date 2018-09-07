package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionResponseViewModel implements Parcelable {

    private CurrentSolutionViewModel currentSolutionViewModel;
    private List<SolutionViewModel> solutionViewModelList;
    private RequireViewModel require;
    private FreeReturnViewModel freeReturn;
    private List<SolutionComplaintModel> complaints;
    private SolutionMessageViewModel message;
    private SolutionViewModel selectedSolution;


    public SolutionResponseViewModel(
            CurrentSolutionViewModel currentSolutionViewModel,
            List<SolutionViewModel> solutionViewModelList,
            RequireViewModel require,
            FreeReturnViewModel freeReturn,
            List<SolutionComplaintModel> complaints,
            SolutionMessageViewModel message) {
        this.currentSolutionViewModel = currentSolutionViewModel;
        this.solutionViewModelList = solutionViewModelList;
        this.require = require;
        this.freeReturn = freeReturn;
        this.complaints = complaints;
        this.message = message;
        this.selectedSolution = new SolutionViewModel();
    }

    public CurrentSolutionViewModel getCurrentSolution() {
        return currentSolutionViewModel;
    }

    public void setCurrentSolutionViewModel(CurrentSolutionViewModel currentSolutionViewModel) {
        this.currentSolutionViewModel = currentSolutionViewModel;
    }

    public SolutionViewModel getSelectedSolution() {
        return selectedSolution;
    }

    public void setSelectedSolution(SolutionViewModel selectedSolution) {
        this.selectedSolution = selectedSolution;
    }

    public SolutionMessageViewModel getMessage() {
        return message;
    }

    public void setMessage(SolutionMessageViewModel message) {
        this.message = message;
    }

    public List<SolutionComplaintModel> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<SolutionComplaintModel> complaints) {
        this.complaints = complaints;
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
        dest.writeParcelable(this.currentSolutionViewModel, flags);
        dest.writeTypedList(this.solutionViewModelList);
        dest.writeParcelable(this.require, flags);
        dest.writeParcelable(this.freeReturn, flags);
        dest.writeTypedList(this.complaints);
        dest.writeParcelable(this.message, flags);
        dest.writeParcelable(this.selectedSolution, flags);
    }

    protected SolutionResponseViewModel(Parcel in) {
        this.currentSolutionViewModel = in.readParcelable(CurrentSolutionViewModel.class.getClassLoader());
        this.solutionViewModelList = in.createTypedArrayList(SolutionViewModel.CREATOR);
        this.require = in.readParcelable(RequireViewModel.class.getClassLoader());
        this.freeReturn = in.readParcelable(FreeReturnViewModel.class.getClassLoader());
        this.complaints = in.createTypedArrayList(SolutionComplaintModel.CREATOR);
        this.message = in.readParcelable(SolutionMessageViewModel.class.getClassLoader());
        this.selectedSolution = in.readParcelable(SolutionViewModel.class.getClassLoader());
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
