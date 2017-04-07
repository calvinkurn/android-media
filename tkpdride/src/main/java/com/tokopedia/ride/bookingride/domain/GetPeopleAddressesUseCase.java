package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddressWrapper;

import rx.Observable;

/**
 * Created by alvarisi on 4/6/17.
 */

public class GetPeopleAddressesUseCase extends UseCase<PeopleAddressWrapper> {
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_ORDER_BY = "order_by";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String PARAM_TIMESTAMP = "device_time";
    private final PeopleAddressRepository repository;

    public GetPeopleAddressesUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, PeopleAddressRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<PeopleAddressWrapper> createObservable(RequestParams requestParams) {
        if (requestParams.getInt(PARAM_PAGE, 0) == 1){
            return repository.getAddresses(true, requestParams.getParameters());
        }else {
            return repository.getAddresses(false, requestParams.getParameters());
        }
    }
}
