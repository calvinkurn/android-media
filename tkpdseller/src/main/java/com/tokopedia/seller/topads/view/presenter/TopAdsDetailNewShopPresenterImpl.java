package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewShopPresenterImpl extends TopAdsDetailEditShopPresenterImpl implements TopAdsDetailNewShopPresenter {

    private TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase;

    public TopAdsDetailNewShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase, TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                            TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase,
                                            TopAdsProductListUseCase topAdsProductListUseCase) {
        super(topAdsGetDetailShopUseCase, topAdsSaveDetailShopUseCase, topAdsProductListUseCase);
        this.topAdsCreateDetailShopUseCase = topAdsCreateDetailShopUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailShopViewModel viewModel) {
        topAdsCreateDetailShopUseCase.execute(TopAdsCreateDetailShopUseCase.createRequestParams(
                TopAdDetailProductMapper.convertViewToDomain(viewModel)), getSubscriberSaveShop());
    }
}