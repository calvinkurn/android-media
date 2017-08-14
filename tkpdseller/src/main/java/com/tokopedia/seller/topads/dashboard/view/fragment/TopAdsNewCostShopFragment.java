package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostShopFragment extends TopAdsNewCostFragment {
    private StepperListener stepperListener;
    private TopAdsCreatePromoShopModel stepperModel;

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = new TopAdsDetailShopViewModel();
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if(context instanceof StepperListener){
            this.stepperListener = (StepperListener)context;
        }
    }

    @Override
    protected void onClickedNext() {
        super.onClickedNext();
        if(stepperListener != null) {
            if(stepperModel == null){
                stepperModel = new TopAdsCreatePromoShopModel();
            }
            stepperModel.setTopAdsDetailShopViewModel((TopAdsDetailShopViewModel) detailAd);
            stepperListener.goToNextPage(stepperModel);
        }
    }
}
