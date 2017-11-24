package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;

import rx.Observable;

/**
 * Created by hangnadi on 3/23/17.
 */

public class InputAddressUseCase extends UseCase<ResolutionActionDomainData> {

    public static final String PARAM_ADDRESS_ID = "address_id";
    public static final String PARAM_BYPASS = "bypass";
    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_NEW_ADDRESS = "new_address";
    public static final int DEFAULT_BY_PASS = 1;
    public static final int ADMIN_BY_PASS = 2;

    private final ResCenterRepository repository;

    public InputAddressUseCase(ThreadExecutor jobExecutor,
                               PostExecutionThread uiThread,
                               ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return repository.inputAddressV2(requestParams);
    }

    public static RequestParams getInputAddressParam(String addressId, String resolutionId, int paramByPass) {
        RequestParams params = RequestParams.create();
        params.putString(InputAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putInt(InputAddressUseCase.PARAM_BYPASS, paramByPass);
        params.putString(InputAddressUseCase.PARAM_RESOLUTION_ID, resolutionId);
        params.putInt(InputAddressUseCase.PARAM_NEW_ADDRESS, 1);
        return params;
    }

    public static RequestParams getInputAddressMigrateVersionParam(String addressId, String resolutionId) {
        RequestParams params = RequestParams.create();
        params.putString(InputAddressUseCase.PARAM_ADDRESS_ID, addressId);
        params.putString(InputAddressUseCase.PARAM_RESOLUTION_ID, resolutionId);
        params.putInt(InputAddressUseCase.PARAM_NEW_ADDRESS, 1);
        return params;
    }
}
