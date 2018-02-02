package com.tokopedia.home.explore.data.repository;

import com.tokopedia.home.explore.domain.model.ExploreDataModel;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public interface ExploreRepository {

    Observable<ExploreDataModel> getExploreData();
}
