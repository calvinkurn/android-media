package com.tokopedia.topads.dashboard.view.fragment;

import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostNewGroupFragment extends TopAdsNewCostFragment<TopAdsCreatePromoNewGroupModel, TopAdsDetailGroupViewModel> implements TopAdsDetailEditView {

    @Inject
    TopAdsDetailNewProductPresenter topAdsDetailNewProductPresenter;

    @Inject
    ShopInfoRepository shopInfoRepository;

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        topAdsDetailNewProductPresenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if(stepperModel != null){
            loadAd(stepperModel.getDetailAd());
        }
    }

    @Override
    protected void loadSuggestionBid() {
        GetSuggestionBody getSuggestionBody = new GetSuggestionBody();
        getSuggestionBody.setRounding(true);
        if(shopInfoRepository.getShopId() != null)
            getSuggestionBody.setShopId(Long.valueOf(shopInfoRepository.getShopId()));
        getSuggestionBody.setSource(TopAdsNetworkConstant.SOURCE_NEW_COST_GROUP);
        getSuggestionBody.setDataType(TopAdsNetworkConstant.SUGGESTION_DATA_TYPE_SUMMARY);
        getSuggestionBody.setSuggestionType(TopAdsNetworkConstant.SUGGESTION_TYPE_DEPARTMENT_ID);
        for (TopAdsProductViewModel topAdsProductViewModel : stepperModel.getTopAdsProductViewModels()) {
            getSuggestionBody.addId(topAdsProductViewModel.getDepartmentId()+"");
        }

        topAdsDetailNewProductPresenter.getSuggestionBid(getSuggestionBody);
    }

    @Override
    protected void onClickedNext() {
        if(!isError()) {
            super.onClickedNext();
            if (stepperListener != null) {
                trackingNewCostTopads();
                if (stepperModel == null) {
                    stepperModel = new TopAdsCreatePromoNewGroupModel();
                }
                stepperModel.setDetailGroupCostViewModel(detailAd);
                stepperListener.goToNextPage(stepperModel);
                hideLoading();
            }
        }
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    private void trackingNewCostTopads() {
        if(detailAd != null && detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductAddPromoStep2(AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsProductAddPromoStep2(AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsDetailNewProductPresenter.detachView();
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        setSuggestionBidText(s);
    }

    @Override
    protected void onSuggestionTitleUseClick() {
        // TODO things to do
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) { /* remain empty*/  }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) { /* remain empty*/  }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) { /* remain empty*/  }

    @Override
    public void onLoadDetailAdError(String errorMessage) { /* remain empty*/  }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) { /* remain empty*/  }

    @Override
    public void onSaveAdError(String errorMessage) { /* remain empty*/  }
}
