package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleShopFragment extends TopAdsNewScheduleFragment implements TopAdsDetailEditView {

    @Inject
    TopAdsDetailNewShopPresenter topAdsDetailShopPresenter;

    private StepperListener stepperListener;
    private TopAdsCreatePromoShopModel stepperModel;

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        detailAd = new TopAdsDetailShopViewModel();
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        topAdsDetailShopPresenter.attachView(this);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        if (stepperModel == null) {
            stepperModel = new TopAdsCreatePromoShopModel();
        }
        stepperModel.setTopAdsDetailShopViewModel((TopAdsDetailShopViewModel) detailAd);
        topAdsDetailShopPresenter.saveAd(stepperModel.getTopAdsDetailShopViewModel());
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {

    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {

    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        if (stepperListener != null) {
            stepperListener.goToNextPage(stepperModel);
        }
    }

    @Override
    public void onSaveAdError(String errorMessage) {

    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {

    }
}
