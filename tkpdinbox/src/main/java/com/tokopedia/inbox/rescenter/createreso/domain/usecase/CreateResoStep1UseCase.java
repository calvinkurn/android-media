package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep1Repository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoStep1UseCase extends UseCase<CreateResoStep1Domain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESULT = "result";

    private CreateResoStep1Repository createResoStep1Repository;

    public CreateResoStep1UseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  CreateResoStep1Repository createResoStep1Repository) {
        super(threadExecutor, postExecutionThread);
        this.createResoStep1Repository = createResoStep1Repository;
    }

    @Override
    public Observable<CreateResoStep1Domain> createObservable(RequestParams requestParams) {
        return createResoStep1Repository.createResoStep1(requestParams);
    }

    public RequestParams createResoStep1Params(ResultViewModel resultViewModel) {
        JSONObject problemObject = new JSONObject();
        try {
            problemObject.put("problem", resultViewModel.getProblemArray());
            RequestParams params = RequestParams.create();
            params.putString(ORDER_ID, resultViewModel.orderId);
            params.putString(PARAM_RESULT, resultViewModel.writeToJson().toString());
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
