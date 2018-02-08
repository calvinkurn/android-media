package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleShopFragment extends TopAdsNewScheduleFragment<TopAdsCreatePromoShopModel,
        TopAdsDetailShopViewModel, TopAdsDetailNewShopPresenter>{

    @Override
    protected void initialVar() {
        super.initialVar();
        if(stepperModel != null){
            loadAd(stepperModel.getTopAdsDetailShopViewModel());
        }
    }


    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        trackerScheduleShop();
        if (stepperModel == null) {
            stepperModel = new TopAdsCreatePromoShopModel();
        }
        stepperModel.setDetailShopScheduleViewModel(detailAd);
        daggerPresenter.saveAd(stepperModel.getTopAdsDetailShopViewModel());
    }

    private void trackerScheduleShop() {
        if(detailAd.isScheduled()){
            UnifyTracking.eventTopAdsShopAddPromoShowTime(AppEventTracking.EventLabel.SHOWTIME_SETUP);
        }else{
            UnifyTracking.eventTopAdsShopAddPromoShowTime(AppEventTracking.EventLabel.SHOWTIME_AUTO);
        }
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        if(stepperListener != null) {
            stepperListener.finishPage();
        }
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
    }
}
