package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.domain.model.EventsCategoryDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetEventsListRequestUseCase extends UseCase<List<EventsCategoryDomain>> {
    private final EventRepository eventRepository;
    public GetEventsListRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<EventsCategoryDomain>> createObservable(RequestParams requestParams) {
       return eventRepository.getEvents(requestParams.getParameters());

    }
}
