package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/6/17.
 */

public interface BannerRepository {

    Observable<HotlistBannerModel> getHotlistBanner(TKPDMapParam<String, Object> parameters);

    Observable<OfficialStoreBannerModel> getOfficialStoreBanner(String keyword);

}
