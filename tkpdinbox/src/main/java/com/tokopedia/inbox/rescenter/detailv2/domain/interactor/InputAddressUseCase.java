package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
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

    public InputAddressUseCase(JobExecutor jobExecutor,
                               UIThread uiThread,
                               ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<ResolutionActionDomainData> createObservable(RequestParams requestParams) {
        return repository.inputAddress(requestParams.getParameters());
    }
}
