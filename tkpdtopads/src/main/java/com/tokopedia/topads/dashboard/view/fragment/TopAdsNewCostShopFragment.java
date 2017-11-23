package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

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
    protected void loadSuggestionBid() {

    }

    @Override
    protected void onSuggestionTitleUseClick() {
        // TODO what to do with this
    }

    @Override
    protected void onClickedNext() {
        if(!isError()) {
            super.onClickedNext();
            trackerBudgetShop();
            if (stepperListener != null) {
                if (stepperModel == null) {
                    stepperModel = new TopAdsCreatePromoShopModel();
                }
                stepperModel.setDetailShopCostViewModel(detailAd);
                stepperListener.goToNextPage(stepperModel);
                hideLoading();
            }
        }
    }

    private void trackerBudgetShop() {
        if(detailAd.isBudget()) {
            UnifyTracking.eventTopAdsShopAddPromoBudget(AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsShopAddPromoBudget(AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }
}
