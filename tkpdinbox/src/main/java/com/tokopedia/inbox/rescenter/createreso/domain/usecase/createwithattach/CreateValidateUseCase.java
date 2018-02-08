package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

import rx.Observable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class CreateValidateUseCase extends UseCase<CreateValidateDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESULT = "result";
    public static final String PARAM_PROBLEM = "problem";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";

    private static final String PARAM_LIST_ATTACHMENT = "LIST_ATTACHMENT";

    private CreateValidateSubmitRepository createValidateSubmitRepository;

    public CreateValidateUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 CreateValidateSubmitRepository createValidateSubmitRepository) {
        super(threadExecutor, postExecutionThread);
        this.createValidateSubmitRepository = createValidateSubmitRepository;
    }

    @Override
    public Observable<CreateValidateDomain> createObservable(RequestParams requestParams) {
        return createValidateSubmitRepository.validate(requestParams);
    }

    public static RequestParams createResoValidateParams(ResultViewModel resultViewModel) {
        try {
            RequestParams params = RequestParams.create();
            params.putString(ORDER_ID, resultViewModel.orderId);
            params.putString(PARAM_RESULT, resultViewModel.writeToJson().toString());
            params.putObject(PARAM_LIST_ATTACHMENT, resultViewModel.attachmentList);
            if (resultViewModel.resolutionId != null) {
                params.putString(PARAM_RESOLUTION_ID, resultViewModel.resolutionId);
            }
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
