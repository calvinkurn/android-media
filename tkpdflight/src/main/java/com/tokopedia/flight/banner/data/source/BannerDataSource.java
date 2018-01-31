package com.tokopedia.flight.banner.data.source;

import android.util.Log;

import com.tokopedia.flight.banner.data.source.cloud.BannerDataCloudSource;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by furqan on 28/12/17.
 */

public class BannerDataSource {

    private final BannerDataCloudSource bannerDataCloudSource;

    @Inject
    public BannerDataSource(BannerDataCloudSource bannerDataCloudSource) {
        this.bannerDataCloudSource = bannerDataCloudSource;
    }

    public Observable<List<BannerDetail>> getBannerData(Map<String, String> params) {
        return bannerDataCloudSource.getBannerData(params).doOnNext(new Action1<List<BannerDetail>>() {
            @Override
            public void call(List<BannerDetail> bannerDetailList) {
                Log.d("DAPAT DATA SOURCE", bannerDetailList.get(0).getAttributes().getImgUrl());
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("DAPAT DATA SOURCE", throwable.getMessage());
                Log.d("DAPAT DATA SOURCE", throwable.getLocalizedMessage());
            }
        });
    }
}