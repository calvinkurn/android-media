package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GetSolutionUseCase extends UseCase<SolutionResponseDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";
    public static final String PARAM_PROBLEM = "problem";

    private SolutionRepository solutionRepository;

    public GetSolutionUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              SolutionRepository solutionRepository) {
        super(threadExecutor, postExecutionThread);
        this.solutionRepository = solutionRepository;
    }

    @Override
    public Observable<SolutionResponseDomain> createObservable(RequestParams requestParams) {
        return solutionRepository.getSolutionFromCloud(requestParams);
    }

    public RequestParams getSolutionUseCaseParams(ResultViewModel resultViewModel) {
        JSONObject problemObject = new JSONObject();
        try {
            problemObject.put(PARAM_PROBLEM, resultViewModel.getProblemArray());
            if (resultViewModel.resolutionId != null) {
                problemObject.put(PARAM_RESOLUTION_ID, Integer.valueOf(resultViewModel.resolutionId));
            }
            RequestParams params = RequestParams.create();
            params.putString(ORDER_ID, resultViewModel.orderId);
            params.putString(PARAM_PROBLEM, problemObject.toString());

            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
