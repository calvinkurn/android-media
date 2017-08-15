package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostShopFragment extends TopAdsNewCostFragment<TopAdsCreatePromoShopModel, TopAdsDetailShopViewModel> {
    @Override
    protected void initView(View view) {
        super.initView(view);
        if(stepperModel != null){
            loadAd(stepperModel.getTopAdsDetailShopViewModel());
        }
    }

    @Override
    protected void onClickedNext() {
        super.onClickedNext();
        if(stepperListener != null) {
            if(stepperModel == null){
                stepperModel = new TopAdsCreatePromoShopModel();
            }
            stepperModel.setDetailShopCostViewModel(detailAd);
            stepperListener.goToNextPage(stepperModel);
            hideLoading();
        }
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }
}
