package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class GetBrandsOfficialStoreUseCase extends UseCase<BrandsOfficialStoreResponseModel> {

    private final HomeRepository homeRepository;

    public GetBrandsOfficialStoreUseCase(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<BrandsOfficialStoreResponseModel> createObservable(RequestParams requestParams) {
        return homeRepository.getBrandsOfficialStore();
    }
}
