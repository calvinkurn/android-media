package com.tokopedia.flight.banner.domain.interactor;

import android.util.Log;

import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nakama on 28/12/17.
 */

public class BannerGetDataUseCase extends UseCase<List<BannerDetail>> {

    public static final String DEVICE_ID = "device_id";
    public static final String CATEGORY_ID = "category_id";

    private final FlightRepository flightRepository;

    @Inject
    public BannerGetDataUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<BannerDetail>> createObservable(RequestParams requestParams) {
        return flightRepository.getBanners(requestParams.getParamsAllValueInString())
                .doOnNext(new Action1<List<BannerDetail>>() {
                    @Override
                    public void call(List<BannerDetail> bannerDetailList) {
                        Log.d("DAPAT USECASE", bannerDetailList.get(0).getAttributes().getImgUrl());
                        Log.d("DAPAT USECASE", bannerDetailList.size() + "");
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d("DAPAT USECASE", throwable.getLocalizedMessage());
                        Log.d("DAPAT USECASE", throwable.getMessage());
                    }
                });
    }

    public RequestParams createRequestParams(String deviceId, String categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DEVICE_ID, deviceId);
        requestParams.putString(CATEGORY_ID, categoryId);
        return requestParams;
    }
}
