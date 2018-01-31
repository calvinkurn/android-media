package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class GetHomeCategoryUseCase extends UseCase<HomeCategoryResponseModel> {

    private final HomeRepository homeRepository;


    public GetHomeCategoryUseCase(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<HomeCategoryResponseModel> createObservable(RequestParams requestParams) {
        return homeRepository.getHomeCategorys();
    }
}
