package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/18/17.
 */

public class GetOverviewPolylineUseCase extends UseCase<List<String>> {
    private final PlaceRepository placeRepository;

    public GetOverviewPolylineUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread, PlaceRepository placeRepository) {
        super(threadExecutor, postExecutionThread);
        this.placeRepository = placeRepository;
    }

    @Override
    public Observable<List<String>> createObservable(RequestParams requestParams) {
        return this.placeRepository.getOveriewPolyline(requestParams.getParameters())
                .map(new Func1<OverviewPolyline, List<String>>() {
                    @Override
                    public List<String> call(OverviewPolyline overviewPolyline) {
                        return overviewPolyline.getOverviewPolyline();
                    }
                });
    }
}
