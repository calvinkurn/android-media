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
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleNewGroupFragment extends TopAdsNewScheduleFragment implements TopAdsDetailNewGroupView {

    @Inject
    TopAdsDetailNewGroupPresenter topAdsDetailEditPresenter;

    private StepperListener stepperListener;
    private TopAdsCreatePromoNewGroupModel stepperModel;

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        topAdsDetailEditPresenter.attachView(this);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
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
            stepperModel = new TopAdsCreatePromoNewGroupModel();
        }
        TopAdsDetailGroupViewModel detailAdSchedule = (TopAdsDetailGroupViewModel) detailAd;
        TopAdsDetailGroupViewModel detailAdStepper = stepperModel.getDetailAd();
        detailAdStepper.setEndDate(detailAdSchedule.getEndDate());
        detailAdStepper.setStartDate(detailAdSchedule.getStartDate());
        detailAdStepper.setStartTime(detailAdSchedule.getStartTime());
        detailAdStepper.setEndTime(detailAdSchedule.getEndTime());
        detailAdStepper.setScheduled(detailAdSchedule.isScheduled());
        stepperModel.setDetailAd(detailAdStepper);
        topAdsDetailEditPresenter.saveAdNew(stepperModel.getGroupName(), stepperModel.getDetailAd(), stepperModel.getTopAdsProductViewModels());
    }

    @Override
    public void goToGroupDetail(String groupId) {

    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        //do nothing
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        //do nothing
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        if (stepperListener != null) {
            stepperListener.finishPage();
            hideLoading();
        }
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        // do nothing
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }
}
