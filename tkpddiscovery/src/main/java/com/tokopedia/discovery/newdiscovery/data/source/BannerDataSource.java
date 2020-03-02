package com.tokopedia.discovery.newdiscovery.data.source;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.apiservices.search.apis.HotListApi;
import com.tokopedia.discovery.newdiscovery.data.mapper.OfficialStoreBannerMapper;

import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/6/17.
 */

public class BannerDataSource {

    private final HotListApi hotlistApi;
    private final MojitoApi mojitoApi;
    private final OfficialStoreBannerMapper officialStoreBannerMapper;

    public BannerDataSource(HotListApi hotListApi, MojitoApi mojitoApi,
                            OfficialStoreBannerMapper officialStoreBannerMapper) {
        this.hotlistApi = hotListApi;
        this.mojitoApi = mojitoApi;
        this.officialStoreBannerMapper = officialStoreBannerMapper;
    }

    public Observable<OfficialStoreBannerModel> getOfficialStoreBanner(String keyword) {
        return mojitoApi.getOfficialStoreBanner(keyword).map(officialStoreBannerMapper);
    }
}
