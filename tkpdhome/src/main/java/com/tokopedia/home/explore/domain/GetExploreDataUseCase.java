package com.tokopedia.home.explore.domain;

import com.tokopedia.home.explore.data.repository.ExploreRepositoryImpl;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class GetExploreDataUseCase extends UseCase<List<ExploreSectionViewModel>> {

    private final ExploreRepositoryImpl repository;

    public GetExploreDataUseCase(ExploreRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<ExploreSectionViewModel>> createObservable(RequestParams requestParams) {
        return repository.getExploreData();
    }
}
