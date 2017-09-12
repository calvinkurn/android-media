package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep1Repository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep2Repository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep2Domain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoStep2UseCase extends UseCase<CreateResoStep2Domain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_STEP_2 = "step_2";

    private CreateResoStep2Repository createResoStep2Repository;

    public CreateResoStep2UseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  CreateResoStep2Repository createResoStep1Repository) {
        super(threadExecutor, postExecutionThread);
        this.createResoStep2Repository = createResoStep1Repository;
    }

    @Override
    public Observable<CreateResoStep2Domain> createObservable(RequestParams requestParams) {
        return createResoStep2Repository.createResoStep2(requestParams);
    }

    public RequestParams createResoStep2Params(ResultViewModel resultViewModel) {
        JSONObject problemObject = new JSONObject();
        try {
            problemObject.put("problem", resultViewModel.getProblemArray());
            RequestParams params = RequestParams.create();
            params.putString(ORDER_ID, resultViewModel.orderId);
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
