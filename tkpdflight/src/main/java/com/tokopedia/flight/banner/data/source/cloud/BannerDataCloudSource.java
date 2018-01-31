package com.tokopedia.flight.banner.data.source.cloud;

import android.util.Log;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nakama on 28/12/17.
 */

public class BannerDataCloudSource {

    private FlightApi flightApi;

    @Inject
    public BannerDataCloudSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<List<BannerDetail>> getBannerData(Map<String, String> params) {
        return flightApi.getBanners(FlightUrl.BANNER_PATH, params)
                .flatMap(new Func1<Response<DataResponse<List<BannerDetail>>>, Observable<List<BannerDetail>>>() {
                    @Override
                    public Observable<List<BannerDetail>> call(Response<DataResponse<List<BannerDetail>>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                })
                .doOnNext(new Action1<List<BannerDetail>>() {
                    @Override
                    public void call(List<BannerDetail> bannerDetailList) {
                        Log.d("DAPAT DATA CLOUD", bannerDetailList.get(0).getAttributes().getImgUrl());
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d("DAPAT DATA CLOUD", throwable.getLocalizedMessage());
                        Log.d("DAPAT DATA CLOUD", throwable.getMessage());
                    }
                });
    }
}
