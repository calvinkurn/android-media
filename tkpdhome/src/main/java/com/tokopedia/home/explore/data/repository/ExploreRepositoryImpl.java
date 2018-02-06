package com.tokopedia.home.explore.data.repository;

import com.tokopedia.home.explore.data.source.ExploreDataSource;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;

import java.util.List;

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
    public Observable<List<ExploreSectionViewModel>> getExploreData() {
        return dataSource.getExploreData();
    }

    public Observable<List<ExploreSectionViewModel>> getExploreDataCache() {
        return dataSource.getDataCache();
    }
}
