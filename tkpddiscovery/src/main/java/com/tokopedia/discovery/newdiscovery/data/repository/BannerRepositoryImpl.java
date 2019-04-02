package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.source.BannerDataSource;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class BannerRepositoryImpl implements BannerRepository {

    private final BannerDataSource bannerDataSource;

    public BannerRepositoryImpl(BannerDataSource bannerDataSource) {
        this.bannerDataSource = bannerDataSource;
    }

    @Override
    public Observable<HotlistBannerModel> getHotlistBanner(TKPDMapParam<String, Object> parameters) {
        return bannerDataSource.getHotlistBanner(parameters);
    }

    @Override
    public Observable<OfficialStoreBannerModel> getOfficialStoreBanner(String keyword) {
        return bannerDataSource.getOfficialStoreBanner(keyword);
    }
}
