package com.tokopedia.discovery.search.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.UseCaseWithParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.search.domain.SearchParam;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.List;

import rx.Observable;

/**
 * @author erry on 23/02/17.
 */

public class SearchUseCase extends UseCaseWithParams<SearchParam, List<SearchData>> {

    private final SearchDataFactory dataFactory;

    public SearchUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread
            , SearchDataFactory dataFactory) {
        super(threadExecutor, postExecutionThread);
        this.dataFactory = dataFactory;
    }

    @Override
    protected Observable<List<SearchData>> createObservable(SearchParam requestParams) {
        return null;
    }
}
