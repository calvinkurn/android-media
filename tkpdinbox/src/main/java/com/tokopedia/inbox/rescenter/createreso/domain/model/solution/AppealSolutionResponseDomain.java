package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;


import java.util.List;

/**
 * Created by yoasfs on 24/08/17.
 */

public class AppealSolutionResponseDomain extends EditSolutionResponseDomain {

    public AppealSolutionResponseDomain(CurrentSolutionDomain currentSolution,
                                        List<EditSolutionDomain> solutions,
                                        FreeReturnDomain freeReturn,
                                        List<SolutionComplaintDomain> complaints,
                                        SolutionMessageDomain message) {
        super(currentSolution, solutions, freeReturn, complaints, message);
    }
}
