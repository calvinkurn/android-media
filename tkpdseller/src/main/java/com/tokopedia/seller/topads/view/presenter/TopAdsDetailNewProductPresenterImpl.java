package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewProductPresenterImpl extends TopAdsDetailEditProductPresenterImpl implements TopAdsDetailNewProductPresenter {

    public TopAdsDetailNewProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase, TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase) {
        super(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase);
    }
}