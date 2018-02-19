package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import rx.Observable;

/**
 * Created by pranaymohapatra on 30/01/18.
 */

public class GetSearchNextUseCase extends UseCase<SearchDomainModel> {

    private final EventRepository eventRepository;
    private String nextUrl;

    public GetSearchNextUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    public void setNextUrl(String Url) {
        nextUrl = Url;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        return eventRepository.getSearchNext(nextUrl);
    }
}
