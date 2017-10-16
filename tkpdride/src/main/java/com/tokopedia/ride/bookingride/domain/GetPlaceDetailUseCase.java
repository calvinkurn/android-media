package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.geolocation.domain.MapsRepository;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 9/20/17.
 */

public class GetPlaceDetailUseCase extends UseCase<PlacePassViewModel> {
    public static final String PARAM_KEYWORD = "input";
    private MapsRepository mapsRepository;
    private MapService mapService;

    public GetPlaceDetailUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                         MapsRepository mapsRepository, MapService mapService) {
        super(threadExecutor, postExecutionThread);
        this.mapsRepository = mapsRepository;
        this.mapService = mapService;
    }
    @Override
    public Observable<PlacePassViewModel> createObservable(RequestParams requestParams) {
        return mapsRepository.getLatLng(mapService, requestParams.getParameters())
                .map(new Func1<CoordinateViewModel, PlacePassViewModel>() {
                    @Override
                    public PlacePassViewModel call(CoordinateViewModel coordinateViewModel) {
                        PlacePassViewModel placePassViewModel = new PlacePassViewModel();
                        placePassViewModel.setPlaceId(coordinateViewModel.getPlaceId());
                        placePassViewModel.setTitle(coordinateViewModel.getTitle());
                        placePassViewModel.setAndFormatLatitude(coordinateViewModel.getCoordinate().latitude);
                        placePassViewModel.setAndFormatLongitude(coordinateViewModel.getCoordinate().longitude);
                        placePassViewModel.setAddress(coordinateViewModel.getAddress());
                        return placePassViewModel;
                    }
                });
    }
}
