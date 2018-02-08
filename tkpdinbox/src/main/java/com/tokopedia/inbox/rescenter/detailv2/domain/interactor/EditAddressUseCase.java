package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;

import rx.Observable;

/**
 * Created by hangnadi on 4/10/17.
 */

public class EditAddressUseCase extends UseCase<ResolutionActionDomainData> {

    public static final String PARAM_ADDRESS_ID = "address_id";
    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_CONVERSATION_ID = "conversation_id";
    public static final String PARAM_OLD_DATA = "old_data";

    private final ResCenterRepository repository;

    public EditAddressUseCase(ThreadExecutor jobExecutor,
                              PostExecutionThread uiThread,
                              ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return repository.editAddressV2(requestParams);
    }

    public static RequestParams getEditAddressParam(String addressId, String oldAddressId, String conversationId, String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(EditAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putString(EditAddressUseCase.PARAM_RESOLUTION_ID, resolutionId);
        params.putString(EditAddressUseCase.PARAM_CONVERSATION_ID, conversationId);
        params.putString(EditAddressUseCase.PARAM_OLD_DATA, oldAddressId + "-" + conversationId );
        return params;
    }
}
