package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditSolutionResponseDomain {

    @Nullable
    private CurrentSolutionDomain currentSolution;

    @Nullable
    private List<EditSolutionDomain> solutions = new ArrayList<>();

    @Nullable
    private FreeReturnDomain freeReturn;

    @Nullable
    private List<SolutionComplaintDomain> complaints = new ArrayList<>();

    @Nullable
    private SolutionMessageDomain message;

    @Nullable
    private boolean isSuccess;


    public EditSolutionResponseDomain(CurrentSolutionDomain currentSolution,
                                      List<EditSolutionDomain> solutions,
                                      FreeReturnDomain freeReturn,
                                      List<SolutionComplaintDomain> complaints,
                                      SolutionMessageDomain message) {
        this.currentSolution = currentSolution;
        this.solutions = solutions;
        this.freeReturn = freeReturn;
        this.complaints = complaints;
        this.message = message;
    }

    @Nullable
    public CurrentSolutionDomain getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(@Nullable CurrentSolutionDomain currentSolution) {
        this.currentSolution = currentSolution;
    }

    @Nullable
    public List<SolutionComplaintDomain> getComplaints() {
        return complaints;
    }

    public void setComplaints(@Nullable List<SolutionComplaintDomain> complaints) {
        this.complaints = complaints;
    }

    @Nullable
    public List<EditSolutionDomain> getSolutions() {
        return solutions;
    }

    public void setSolutions(@Nullable List<EditSolutionDomain> solutions) {
        this.solutions = solutions;
    }

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }

    @Nullable
    public FreeReturnDomain getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(@Nullable FreeReturnDomain freeReturn) {
        this.freeReturn = freeReturn;
    }

    @Nullable
    public SolutionMessageDomain getMessage() {
        return message;
    }

    public void setMessage(@Nullable SolutionMessageDomain message) {
        this.message = message;
    }
}
