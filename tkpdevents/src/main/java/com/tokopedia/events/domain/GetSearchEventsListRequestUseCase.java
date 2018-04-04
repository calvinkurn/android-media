package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetSearchEventsListRequestUseCase extends UseCase<SearchDomainModel> {
    private final EventRepository eventRepository;
    public final String TAG = "tags";

    public GetSearchEventsListRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        return eventRepository.getSearchEvents(requestParams.getParameters());

    }
}
