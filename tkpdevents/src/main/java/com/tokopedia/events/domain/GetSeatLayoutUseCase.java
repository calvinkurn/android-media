package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import rx.Observable;

/**
 * Created by pranaymohapatra on 20/12/17.
 */

public class GetSeatLayoutUseCase extends UseCase<SeatLayoutResponse> {

    private final EventRepository eventRepository;
    int category_id;
    int product_id;
    int schedule_id;
    int group_id;
    int package_id;

    public GetSeatLayoutUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<SeatLayoutResponse> createObservable(RequestParams requestParams) {
        return eventRepository.getSeatLayout(category_id, product_id, schedule_id, group_id, package_id);
    }

    public void setUrl(PackageViewModel viewModel) {
        category_id = viewModel.getCategoryId();
        product_id = viewModel.getProductId();
        schedule_id = viewModel.getProductScheduleId();
        group_id = viewModel.getProductGroupId();
        package_id = viewModel.getId();
    }
}
