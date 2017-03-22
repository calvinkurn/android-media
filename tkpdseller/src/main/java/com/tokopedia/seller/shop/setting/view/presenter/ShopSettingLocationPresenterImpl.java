package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.GetRecomendationLocationDistrictUseCase;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;
import com.tokopedia.seller.shop.setting.view.mapper.RecommendationDistrictViewMapper;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopSettingLocationPresenterImpl extends ShopSettingLocationPresenter {

    private final FetchDistrictDataUseCase fetchDistrictDataUseCase;
    private final GetRecomendationLocationDistrictUseCase getRecomendationLocationDistrictUseCase;

    public ShopSettingLocationPresenterImpl(
            ShopSettingLocationView view,
            FetchDistrictDataUseCase fetchDistrictDataUseCase,
            GetRecomendationLocationDistrictUseCase getRecomendationLocationDistrictUseCase) {
        super(view);
        this.fetchDistrictDataUseCase = fetchDistrictDataUseCase;
        this.getRecomendationLocationDistrictUseCase = getRecomendationLocationDistrictUseCase;
    }

    @Override
    public void getDistrictData() {
        fetchDistrictDataUseCase.execute(RequestParams.EMPTY, new GetDistrictDataSubscriber());
    }

    @Override
    public void getRecomendationLocationDistrict(String stringTyped) {
        RequestParams recomendationLocationDistrictParam =
                GetRecomendationLocationDistrictUseCase.generateParams(stringTyped);
        getRecomendationLocationDistrictUseCase.execute(
                recomendationLocationDistrictParam,
                new GetRecomendationLocationDistrictSubscriber()
        );
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

        }

        @Override
        public void onNext(Boolean aBoolean) {

        }
    }

    private class GetRecomendationLocationDistrictSubscriber
            extends Subscriber<List<RecomendationDistrictDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

            view.showGenericError();

        }

        @Override
        public void onNext(List<RecomendationDistrictDomainModel> domainModels) {

            List<RecommendationDistrictViewModel> viewModels
                    = RecommendationDistrictViewMapper.map(domainModels);
            view.renderRecomendationDistrictModel(viewModels);

        }
    }
}
