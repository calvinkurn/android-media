package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListWithoutGroupFragment extends TopAdsNewProductListFragment {

    private StepperListener stepperListener;
    private TopAdsCreatePromoWithoutGroupModel stepperModel;

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        if(stepperModel != null ){
            populateView(stepperModel.getProductViewModels());
        }
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if(context instanceof StepperListener){
            this.stepperListener = (StepperListener)context;
        }
    }

    @Override
    protected void onNextClicked() {
        if(stepperListener != null) {
            if(stepperModel == null){
                stepperModel = new TopAdsCreatePromoWithoutGroupModel();
            }
            stepperModel.setProductViewModels(adapter.getData());
            stepperListener.goToNextPage(stepperModel);
        }
    }
}
