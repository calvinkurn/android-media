package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListExistingGroupFragment extends TopAdsNewProductListFragment<TopAdsCreatePromoExistingGroupModel, TopAdsDetailNewGroupPresenter> implements TopAdsDetailNewGroupView {

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
    protected void initView(View view) {
        super.initView(view);
        buttonNext.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void initiateStepperModel() {
        stepperModel = new TopAdsCreatePromoExistingGroupModel();
    }

    @Override
    protected void goToNextPage() {
        UnifyTracking.eventTopAdsProductAddPromoExistingGroupStep1();
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
    public void onSuggestionSuccess(GetSuggestionResponse s) { /* this class not do anything about this */ }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
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
