package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class EditAppealSolutionResponseResponse {

    @SerializedName("currentSolution")
    @Expose
    private CurrentSolutionResponse currentSolution;

    @SerializedName("solutions")
    @Expose
    private List<EditSolutionResponse> solution;

    @SerializedName("freeReturn")
    @Expose
    private EditFreeReturnResponse freeReturn;

    @SerializedName("complaints")
    private List<SolutionComplaintResponse> complaints;

    @SerializedName("message")
    private SolutionMessageResponse message;

    public SolutionMessageResponse getMessage() {
        return message;
    }

    public void setMessage(SolutionMessageResponse message) {
        this.message = message;
    }

    public CurrentSolutionResponse getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(CurrentSolutionResponse currentSolution) {
        this.currentSolution = currentSolution;
    }

    public List<EditSolutionResponse> getSolution() {
        return solution;
    }

    public EditFreeReturnResponse getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(EditFreeReturnResponse freeReturn) {
        this.freeReturn = freeReturn;
    }

    public void setSolution(List<EditSolutionResponse> solution) {
        this.solution = solution;
    }

    public List<SolutionComplaintResponse> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<SolutionComplaintResponse> complaints) {
        this.complaints = complaints;
    }

    @Override
    public String toString() {
        return "EditAppealSolutionResponseResponse{" +
                "solutionLists='" + solution.toString() + '\'' +
                "freeReturn='" + freeReturn.toString() + '\'' +
                '}';
    }
}
