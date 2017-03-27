package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.GetRecomendationLocationDistrictUseCase;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;
import com.tokopedia.seller.shop.setting.view.mapper.RecommendationDistrictViewMapper;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopSettingLocationPresenterImpl extends ShopSettingLocationPresenter {

    private final FetchDistrictDataUseCase fetchDistrictDataUseCase;
    private final GetRecomendationLocationDistrictUseCase getRecomendationLocationDistrictUseCase;

    public ShopSettingLocationPresenterImpl(
            FetchDistrictDataUseCase fetchDistrictDataUseCase,
            GetRecomendationLocationDistrictUseCase getRecomendationLocationDistrictUseCase) {
        this.fetchDistrictDataUseCase = fetchDistrictDataUseCase;
        this.getRecomendationLocationDistrictUseCase = getRecomendationLocationDistrictUseCase;
    }

    @Override
    public void getDistrictData() {
        checkViewAttached();
        getView().showProgressDialog();
        fetchDistrictDataUseCase.execute(RequestParams.EMPTY, new GetDistrictDataSubscriber());
    }

    @Override
    public void getRecomendationLocationDistrict(String stringTyped) {
        if (!stringTyped.isEmpty()) {
            RequestParams recomendationLocationDistrictParam =
                    GetRecomendationLocationDistrictUseCase.generateParams(stringTyped);
            getRecomendationLocationDistrictUseCase.execute(
                    recomendationLocationDistrictParam,
                    new GetRecomendationLocationDistrictSubscriber()
            );
        }
    }

    @Override
    protected void unsubscribeOnDestroy() {
        fetchDistrictDataUseCase.unsubscribe();

    }

    private class GetDistrictDataSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissProgressDialog();
            getView().showRetryGetDistrictData();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            checkViewAttached();
            getView().dismissProgressDialog();

        }
    }

    private class GetRecomendationLocationDistrictSubscriber
            extends Subscriber<RecomendationDistrictDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().showGenericError();

        }

        @Override
        public void onNext(RecomendationDistrictDomainModel domainModels) {

            RecommendationDistrictViewModel viewModel
                    = RecommendationDistrictViewMapper.map(domainModels);
            checkViewAttached();
            getView().renderRecomendationDistrictModel(viewModel);

        }
    }
}
