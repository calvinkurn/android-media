package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.place.data.entity.NearbyRoads;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.ride.domain.model.Location;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by sachinbansal on 1/22/18.
 */

public class GetNearbyRoadsUseCase extends UseCase<NearbyRoads> {
    private final PlaceRepository placeRepository;
    public static final String PARAM_KEY = "key";
    public static final String COORDINATES = "coordinates";

    public GetNearbyRoadsUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 PlaceRepository placeRepository) {
        super(threadExecutor, postExecutionThread);
        this.placeRepository = placeRepository;
    }


    @Override
    public Observable<NearbyRoads> createObservable(RequestParams requestParams) {

        ArrayList<Location> locationArrayList = (ArrayList<Location>) requestParams.getObject(COORDINATES);
        StringBuilder address = new StringBuilder();
        int size = locationArrayList.size();
        for (int i = 0; i < size; i++) {
            address.append(String.valueOf(locationArrayList.get(i).getLatitude())).append(",").append(String.valueOf(locationArrayList.get(i).getLongitude()));
            if (i != size - 1)
                address.append("|");
        }
        String key = requestParams.getString(PARAM_KEY, "");
        return placeRepository.getNearByRoadsFromGoogleAPI(address.toString(), key);
    }
}
