package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
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

public class TopAdsNewProductListExistingGroupFragment extends TopAdsNewProductListFragment<TopAdsCreatePromoExistingGroupModel, TopAdsDetailNewGroupPresenter> implements TopAdsDetailNewGroupView {

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
        stepperModel = new TopAdsCreatePromoExistingGroupModel();
    }

    @Override
    protected void goToNextPage() {
        daggerPresenter.saveAdExisting(stepperModel.getGroupId(), stepperModel.getTopAdsProductViewModels());
    }


    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        // do nothing
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        // do nothing
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        if (stepperListener != null) {
            stepperListener.finishPage();
        }
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
    }

    @Override
    public void goToGroupDetail(String groupId) {
        Intent intent = TopAdsDetailGroupActivity.createIntent(getActivity(), groupId);
        startActivity(intent);
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }
}
