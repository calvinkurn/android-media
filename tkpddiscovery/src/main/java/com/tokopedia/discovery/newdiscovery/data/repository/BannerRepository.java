package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/6/17.
 */

public interface BannerRepository {


    Observable<OfficialStoreBannerModel> getOfficialStoreBanner(String keyword);

}
