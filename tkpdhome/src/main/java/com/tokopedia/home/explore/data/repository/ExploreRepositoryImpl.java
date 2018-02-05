package com.tokopedia.home.explore.data.repository;

import com.tokopedia.home.explore.domain.model.ExploreDataModel;
import com.tokopedia.home.explore.data.source.ExploreDataSource;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreRepositoryImpl implements ExploreRepository {

    private ExploreDataSource dataSource;

    public ExploreRepositoryImpl(ExploreDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<ExploreDataModel> getExploreData() {
        return dataSource.getExploreData();
    }

    public Observable<ExploreDataModel> getExploreDataCache() {
        return dataSource.getDataCache();
    }
}
