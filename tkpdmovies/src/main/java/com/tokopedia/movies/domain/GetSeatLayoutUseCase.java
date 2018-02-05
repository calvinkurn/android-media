package com.tokopedia.movies.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.view.viewmodel.PackageViewModel;

import java.util.List;

import rx.Observable;

/**
 * Created by pranaymohapatra on 20/12/17.
 */

public class GetSeatLayoutUseCase extends UseCase<List<SeatLayoutItem>> {

    private final EventRepository eventRepository;
    String url;

    public GetSeatLayoutUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<SeatLayoutItem>> createObservable(RequestParams requestParams) {
        return eventRepository.getSeatLayout(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
