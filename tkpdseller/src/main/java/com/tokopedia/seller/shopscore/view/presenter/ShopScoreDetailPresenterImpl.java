package com.tokopedia.seller.shopscore.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.view.fragment.ShopScoreDetailView;
import com.tokopedia.seller.shopscore.view.mapper.ShopScoreDetailItemsViewModelMapper;
import com.tokopedia.seller.shopscore.view.mapper.ShopScoreDetailStateMapper;
import com.tokopedia.seller.shopscore.view.mapper.ShopScoreDetailSummaryViewModelMapper;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailSummaryViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailPresenterImpl extends BaseDaggerPresenter<ShopScoreDetailView> implements ShopScoreDetailPresenter {
    private final GetShopScoreDetailUseCase getShopScoreDetailUseCase;

    public ShopScoreDetailPresenterImpl(GetShopScoreDetailUseCase getShopScoreDetailUseCase) {
        this.getShopScoreDetailUseCase = getShopScoreDetailUseCase;
    }

    @Override
    public void getShopScoreDetail() {
        getView().showProgressDialog();
        getShopScoreDetailUseCase.execute(
                RequestParams.EMPTY,
                new GetShopScoreDetailSubscriber()
        );
    }

    private void renderSummary(ShopScoreDetailDomainModel domainModels) {
        ShopScoreDetailSummaryViewModel viewModel = ShopScoreDetailSummaryViewModelMapper.map(domainModels);
        getView().renderShopScoreSummary(viewModel);
    }

    private void renderItemsDetail(ShopScoreDetailDomainModel domainModels) {
        List<ShopScoreDetailItemViewModel> viewModel = ShopScoreDetailItemsViewModelMapper.map(domainModels);
        getView().renderShopScoreDetail(viewModel);
    }

    private void renderState(ShopScoreDetailDomainModel domainModels) {
        ShopScoreDetailStateEnum shopScoreDetailStateEnum = ShopScoreDetailStateMapper.map(domainModels);
        getView().renderShopScoreState(shopScoreDetailStateEnum);
    }

    public void unsubscribe() {
        getShopScoreDetailUseCase.unsubscribe();
    }

    private class GetShopScoreDetailSubscriber extends Subscriber<ShopScoreDetailDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            getView().emptyState();
        }

        @Override
        public void onNext(ShopScoreDetailDomainModel domainModels) {
            getView().dismissProgressDialog();
            renderItemsDetail(domainModels);
            renderSummary(domainModels);
            renderState(domainModels);
        }
    }
}
