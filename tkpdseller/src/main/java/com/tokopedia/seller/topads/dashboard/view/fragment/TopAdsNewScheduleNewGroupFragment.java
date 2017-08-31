package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleNewGroupFragment extends TopAdsNewScheduleFragment<TopAdsCreatePromoNewGroupModel,
        TopAdsDetailGroupViewModel, TopAdsDetailNewGroupPresenter> implements TopAdsDetailNewGroupView{

    @Override
    protected void initView(View view) {
        super.initView(view);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        if(stepperModel != null){
            loadAd(stepperModel.getDetailAd());
        }
    }

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
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        if (stepperModel == null) {
            stepperModel = new TopAdsCreatePromoNewGroupModel();
        }
        trackingNewScheduleTopads();
        stepperModel.setDetailGroupScheduleViewModel(detailAd);
        daggerPresenter.saveAdNew(stepperModel.getGroupName(), stepperModel.getDetailAd(), stepperModel.getTopAdsProductViewModels());
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        if(stepperListener != null) {
            stepperListener.finishPage();
        }
    }

    private void trackingNewScheduleTopads() {
        if(detailAd != null && detailAd.isScheduled()) {
            UnifyTracking.eventTopAdsProductAddPromoStep3(AppEventTracking.EventLabel.SHOWTIME_SETUP);
        }else{
            UnifyTracking.eventTopAdsProductAddPromoStep3(AppEventTracking.EventLabel.SHOWTIME_AUTO);
        }
    }


    @Override
    public void goToGroupDetail(String groupId) {

    }

}
