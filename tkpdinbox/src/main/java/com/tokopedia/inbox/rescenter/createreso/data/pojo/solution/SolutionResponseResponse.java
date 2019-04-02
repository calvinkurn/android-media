package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class SolutionResponseResponse {
    @SerializedName("solutions")
    @Expose
    private List<SolutionResponse> solution;
    @SerializedName("require")
    @Expose
    private RequireResponse require;
    @SerializedName("freeReturn")
    @Expose
    private FreeReturnResponse freeReturn;
    @SerializedName("complaints")
    private List<SolutionComplaintResponse> complaints;

    public List<SolutionResponse> getSolution() {
        return solution;
    }

    public void setSolution(List<SolutionResponse> solution) {
        this.solution = solution;
    }

    public RequireResponse getRequire() {
        return require;
    }

    public void setRequire(RequireResponse require) {
        this.require = require;
    }

    public FreeReturnResponse getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(FreeReturnResponse freeReturn) {
        this.freeReturn = freeReturn;
    }

    public List<SolutionComplaintResponse> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<SolutionComplaintResponse> complaints) {
        this.complaints = complaints;
    }

    @Override
    public String toString() {
        return "SolutionResponseResponse{" +
                "solution='" + solution.toString() + '\'' +
                ", require='" + require.toString() + '\'' +
                ", freeReturn='" + freeReturn.toString() + '\'' +
                '}';
    }
}
