package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostNewGroupFragment extends TopAdsNewCostFragment {

    private StepperListener stepperListener;
    private TopAdsCreatePromoNewGroupModel stepperModel;

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = new TopAdsDetailGroupViewModel();
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
                stepperModel = new TopAdsCreatePromoNewGroupModel();
            }
            stepperModel.setDetailAd((TopAdsDetailGroupViewModel)detailAd);
            stepperListener.goToNextPage(stepperModel);
            hideLoading();
        }
    }
}
