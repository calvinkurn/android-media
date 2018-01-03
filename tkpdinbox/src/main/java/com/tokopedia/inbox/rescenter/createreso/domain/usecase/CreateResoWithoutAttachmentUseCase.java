package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoWithoutAttachmentRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoWithoutAttachmentUseCase extends UseCase<CreateResoWithoutAttachmentDomain> {
    public static final String ORDER_ID = "order_id";
    public static final String PARAM_RESULT = "result";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";

    private CreateResoWithoutAttachmentRepository createResoWithoutAttachmentRepository;

    public CreateResoWithoutAttachmentUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              CreateResoWithoutAttachmentRepository createResoWithoutAttachmentRepository) {
        super(threadExecutor, postExecutionThread);
        this.createResoWithoutAttachmentRepository = createResoWithoutAttachmentRepository;
    }

    @Override
    public Observable<CreateResoWithoutAttachmentDomain> createObservable(RequestParams requestParams) {
        return createResoWithoutAttachmentRepository.createResoWithoutAttachment(requestParams);
    }

    public RequestParams createResoStep1Params(ResultViewModel resultViewModel) {
        try {
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
