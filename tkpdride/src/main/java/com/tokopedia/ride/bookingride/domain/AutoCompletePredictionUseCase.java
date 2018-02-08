package com.tokopedia.ride.bookingride.domain;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.geolocation.domain.MapsRepository;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.AutoCompleteViewModel;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.PredictionResult;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 9/20/17.
 */

public class AutoCompletePredictionUseCase extends UseCase<List<PlaceAutoCompeleteViewModel>> {
    public static final String PARAM_KEYWORD = "input";
    private MapsRepository mapsRepository;
    private MapService mapService;

    public AutoCompletePredictionUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                         MapsRepository mapsRepository, MapService mapService) {
        super(threadExecutor, postExecutionThread);
        this.mapsRepository = mapsRepository;
        this.mapService = mapService;
    }

    @Override
    public Observable<List<PlaceAutoCompeleteViewModel>> createObservable(RequestParams requestParams) {
        String keyword = requestParams.getString(PARAM_KEYWORD, "");
        return mapsRepository.getAutoCompleteList(mapService, requestParams.getParameters(), keyword)
                .map(new Func1<AutoCompleteViewModel, List<PlaceAutoCompeleteViewModel>>() {
                    @Override
                    public List<PlaceAutoCompeleteViewModel> call(AutoCompleteViewModel autoCompleteViewModel) {
                        ArrayList<PlaceAutoCompeleteViewModel> addresses = new ArrayList<>();
                        for (PredictionResult autocompletePrediction : autoCompleteViewModel.getListOfPredictionResults()) {
                            PlaceAutoCompeleteViewModel address = new PlaceAutoCompeleteViewModel();
                            address.setAddress(String.valueOf(autocompletePrediction.getSecondaryTextFormatted()));
                            address.setTitle(String.valueOf(autocompletePrediction.getMainTextFormatted()));
                            address.setAddressId(autocompletePrediction.getPlaceId());
                            address.setType(PlaceAutoCompeleteViewModel.TYPE.GOOGLE_PLACE);
                            addresses.add(address);
                        }
                        return addresses;
                    }
                });
    }
}
