package com.tokopedia.topads.dashboard.view.fragment;

import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostNewGroupFragment extends TopAdsNewCostFragment<TopAdsCreatePromoNewGroupModel, TopAdsDetailGroupViewModel> {

    @Override
    protected void initView(View view) {
        super.initView(view);
        if(stepperModel != null){
            loadAd(stepperModel.getDetailAd());
        }
    }

    @Override
    protected void onClickedNext() {
        if(!isError()) {
            super.onClickedNext();
            if (stepperListener != null) {
                trackingNewCostTopads();
                if (stepperModel == null) {
                    stepperModel = new TopAdsCreatePromoNewGroupModel();
                }
                stepperModel.setDetailGroupCostViewModel(detailAd);
                stepperListener.goToNextPage(stepperModel);
                hideLoading();
            }
        }
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    private void trackingNewCostTopads() {
        if(detailAd != null && detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductAddPromoStep2(AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsProductAddPromoStep2(AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }
}
