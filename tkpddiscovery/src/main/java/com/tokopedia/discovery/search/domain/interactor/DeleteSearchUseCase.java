package com.tokopedia.discovery.search.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.UseCaseWithParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.search.domain.DeleteParam;

import retrofit2.Response;
import rx.Observable;

/**
 * @author erry on 01/03/17.
 */

public class DeleteSearchUseCase extends UseCaseWithParams<DeleteParam, Response<Void>> {

    private SearchDataFactory dataFactory;

    public DeleteSearchUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               SearchDataFactory dataFactory) {
        super(threadExecutor, postExecutionThread);
        this.dataFactory = dataFactory;
    }

    @Override
    public Observable<Response<Void>> createObservable(DeleteParam requestParams) {
        return dataFactory.deleteRecentSearch(requestParams.getParam());
    }
}
