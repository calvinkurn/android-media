package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;

import java.util.List;

import rx.Observable;

/**
 * Created by naveengoyal on 1/25/18.
 */

public class GetEventSeatLayoutUseCase extends UseCase<List<SeatLayoutItem>> {

    private final EventRepository eventRepository;
    String url;

    public GetEventSeatLayoutUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<SeatLayoutItem>> createObservable(RequestParams requestParams) {
        return eventRepository.getEventSeatLayout(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
