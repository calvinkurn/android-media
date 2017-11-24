package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionResponseDomain {

    @Nullable
    private List<SolutionDomain> solutions = new ArrayList<>();

    @Nullable
    private RequireDomain require;

    @Nullable
    private FreeReturnDomain freeReturn;

    @Nullable
    private boolean isSuccess;

    public SolutionResponseDomain(List<SolutionDomain> solutions, RequireDomain require, FreeReturnDomain freeReturn) {
        this.solutions = solutions;
        this.require = require;
        this.freeReturn = freeReturn;
    }

    @Nullable
    public List<SolutionDomain> getSolutions() {
        return solutions;
    }

    public void setSolutions(@Nullable List<SolutionDomain> solutions) {
        this.solutions = solutions;
    }

    @Nullable
    public RequireDomain getRequire() {
        return require;
    }

    public void setRequire(@Nullable RequireDomain require) {
        this.require = require;
    }

    @Nullable
    public FreeReturnDomain getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(@Nullable FreeReturnDomain freeReturn) {
        this.freeReturn = freeReturn;
    }

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }
}
