package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;
import com.tokopedia.ride.common.place.domain.PlaceRepository;

import rx.Observable;

/**
 * Created by Vishal on 10th Nov, 2017.
 */

public class GetLocationAddressUseCase extends UseCase<ReverseGeoCodeAddress> {
    private final PlaceRepository placeRepository;
    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_LONGITUDE = "longitude";
    public static final String PARAM_KEY = "key";

    public GetLocationAddressUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     PlaceRepository placeRepository) {
        super(threadExecutor, postExecutionThread);
        this.placeRepository = placeRepository;
    }


    @Override
    public Observable<ReverseGeoCodeAddress> createObservable(RequestParams requestParams) {
        String address = requestParams.getString(PARAM_LATITUDE, "")
                + "," + requestParams.getString(PARAM_LONGITUDE, "");

        String key = requestParams.getString(PARAM_KEY, "");

        return placeRepository.getAddressFromGoogleAPI(key, address);
    }
}
