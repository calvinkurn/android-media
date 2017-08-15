package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListWithoutGroupFragment extends TopAdsNewProductListFragment<TopAdsCreatePromoWithoutGroupModel, TopAdsGetProductDetailPresenter> {

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
        stepperModel = new TopAdsCreatePromoWithoutGroupModel();
    }

    @Override
    protected void goToNextPage() {
        if(stepperListener != null){
            stepperListener.goToNextPage(stepperModel);
        }
    }
}
