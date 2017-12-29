package com.tokopedia.flight.banner.data.source;

import com.tokopedia.flight.banner.data.source.cloud.BannerDataCloudSource;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

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
        return bannerDataCloudSource.getBannerData(params);
    }
}