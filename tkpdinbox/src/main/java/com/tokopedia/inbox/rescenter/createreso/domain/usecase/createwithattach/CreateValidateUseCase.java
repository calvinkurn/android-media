package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

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

    private CreateResolutionSource createResolutionSource;

    @Inject
    public CreateValidateUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<CreateValidateDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.createValidate(requestParams);
    }

    public static RequestParams createResoValidateParams(ResultViewModel resultViewModel) {
        RequestParams params = RequestParams.create();
        params.putString(ORDER_ID, resultViewModel.orderId);
        params.putObject(PARAM_RESULT, resultViewModel.writeToJson());
        params.putObject(PARAM_LIST_ATTACHMENT, resultViewModel.attachmentList);
        if (resultViewModel.resolutionId != null) {
            params.putString(PARAM_RESOLUTION_ID, resultViewModel.resolutionId);
        }
        return params;
    }
}
