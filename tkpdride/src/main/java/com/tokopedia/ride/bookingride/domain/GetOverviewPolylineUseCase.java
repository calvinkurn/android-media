package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public class GetOverviewPolylineUseCase extends UseCase<List<OverviewPolyline>> {
    public static final String PARAM_ORIGIN = "origin";
    public static final String PARAM_DESTINATION = "destination";
    public static final String PARAM_SENSOR = "sensor";
    public static final String PARAM_TRAFFIC_MODEL = "traffic_model";
    public static final String PARAM_MODE = "mode";
    public static final String PARAM_DEPARTURE_TIME = "departure_time";
    public static final String PARAM_WAYPOINTS = "waypoints";
    public static final String PARAM_KEY = "key";
    private final PlaceRepository placeRepository;

    public GetOverviewPolylineUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread, PlaceRepository placeRepository) {
        super(threadExecutor, postExecutionThread);
        this.placeRepository = placeRepository;
    }

    @Override
    public Observable<List<OverviewPolyline>> createObservable(RequestParams requestParams) {
        return this.placeRepository.getOveriewPolyline(requestParams.getParameters());
    }
}
