package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListExistingGroupFragment extends TopAdsNewProductListFragment implements TopAdsDetailEditView {
    @Inject
    TopAdsDetailNewGroupPresenter topAdsDetailNewGroupPresenter;

    private StepperListener stepperListener;
    private TopAdsCreatePromoExistingGroupModel stepperModel;

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        topAdsDetailNewGroupPresenter.attachView(this);
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
            stepperModel = new TopAdsCreatePromoExistingGroupModel();
        }
        stepperModel.setTopAdsProductViewModels(adapter.getData());
        topAdsDetailNewGroupPresenter.saveAdExisting(stepperModel.getGroupId(), stepperModel.getTopAdsProductViewModels());
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        //do nothing
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        // do nothing
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        if (stepperListener != null) {
            hideLoading();
            stepperListener.goToNextPage(stepperModel);
        }
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        // do nothing
    }
}
