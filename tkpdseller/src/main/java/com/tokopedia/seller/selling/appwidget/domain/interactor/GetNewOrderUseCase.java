package com.tokopedia.seller.selling.appwidget.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.selling.appwidget.domain.GetNewOrderRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public class GetNewOrderUseCase extends UseCase<List<DataOrder>> {
    private final GetNewOrderRepository getNewOrderRepository;

    @Inject
    public GetNewOrderUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                              GetNewOrderRepository getNewOrderRepository) {
        super(threadExecutor, postExecutionThread);
        this.getNewOrderRepository = getNewOrderRepository;
    }

    @Override
    public Observable<List<DataOrder>> createObservable(RequestParams requestParams) {
        return getNewOrderRepository.getNewOrderList();
    }

    public static RequestParams createRequestParams() {
        return RequestParams.create();
    }
}
