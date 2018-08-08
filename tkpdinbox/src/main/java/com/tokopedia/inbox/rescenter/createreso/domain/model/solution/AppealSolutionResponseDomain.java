package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class AppealSolutionResponseDomain {

    @Nullable
    private List<AppealSolutionDomain> solutions = new ArrayList<>();

    @Nullable
    private FreeReturnDomain freeReturn;

    @Nullable
    private boolean isSuccess;

    public AppealSolutionResponseDomain(@Nullable List<AppealSolutionDomain> solutions, @Nullable FreeReturnDomain freeReturn) {
        this.solutions = solutions;
        this.freeReturn = freeReturn;
    }

    @Nullable
    public List<AppealSolutionDomain> getSolutions() {
        return solutions;
    }

    public void setSolutions(@Nullable List<AppealSolutionDomain> solutions) {
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
}
