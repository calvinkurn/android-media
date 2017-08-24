package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductListStepperModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListNewGroupFragment extends TopAdsNewProductListFragment<TopAdsProductListStepperModel, TopAdsGetProductDetailPresenter>{

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void initiateStepperModel() {
        stepperModel = new TopAdsProductListStepperModel();
    }

    @Override
    protected void goToNextPage() {
        hideLoading();
        stepperListener.goToNextPage(stepperModel);
    }

    @Override
    protected boolean isHideExistingGroup() {
        return false;
    }
}
