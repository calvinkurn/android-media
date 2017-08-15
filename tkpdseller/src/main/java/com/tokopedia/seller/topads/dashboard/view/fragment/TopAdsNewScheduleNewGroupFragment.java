package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleNewGroupFragment extends TopAdsNewScheduleFragment<TopAdsCreatePromoNewGroupModel,
        TopAdsDetailGroupViewModel, TopAdsDetailNewGroupPresenter> implements TopAdsDetailNewGroupView{


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


    @Override
    public void goToGroupDetail(String groupId) {

    }

}
