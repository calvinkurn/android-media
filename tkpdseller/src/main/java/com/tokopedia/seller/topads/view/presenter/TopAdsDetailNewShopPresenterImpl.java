package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewShopPresenterImpl extends TopAdsDetailEditShopPresenterImpl implements TopAdsDetailNewShopPresenter {

    private TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase;

    public TopAdsDetailNewShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase, TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                            TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase) {
        super(topAdsGetDetailShopUseCase, topAdsSaveDetailShopUseCase);
        this.topAdsCreateDetailShopUseCase = topAdsCreateDetailShopUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailShopViewModel viewModel) {
        topAdsCreateDetailShopUseCase.execute(TopAdsCreateDetailShopUseCase.createRequestParams(
                TopAdDetailProductMapper.convertViewToDomain(viewModel)), getSubscriberSaveShop());
    }
}