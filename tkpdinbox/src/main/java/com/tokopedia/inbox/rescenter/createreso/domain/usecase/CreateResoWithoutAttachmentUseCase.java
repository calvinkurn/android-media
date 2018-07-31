package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.inbox.rescenter.createreso.data.source.CreateResolutionSource;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoWithoutAttachmentUseCase extends UseCase<CreateResoWithoutAttachmentDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESULT = "result";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";

    private CreateResolutionSource createResolutionSource;

    @Inject
    public CreateResoWithoutAttachmentUseCase(CreateResolutionSource createResolutionSource) {
        this.createResolutionSource = createResolutionSource;
    }

    @Override
    public Observable<CreateResoWithoutAttachmentDomain> createObservable(RequestParams requestParams) {
        return createResolutionSource.createResoWithoutAttachmentResponse(requestParams);
    }

    public RequestParams createResoStep1Params(ResultViewModel resultViewModel) {
        RequestParams params = RequestParams.create();
        params.putString(ORDER_ID, resultViewModel.orderId);
        params.putObject(PARAM_RESULT, resultViewModel.writeToJson());
        return params;
    }

}
