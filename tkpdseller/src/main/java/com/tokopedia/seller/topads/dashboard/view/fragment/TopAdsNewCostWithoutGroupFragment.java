package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.text.TextUtils;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostWithoutGroupFragment extends TopAdsNewCostFragment<TopAdsCreatePromoWithoutGroupModel, TopAdsDetailGroupViewModel> implements TopAdsDetailEditView {

    @Inject
    TopAdsDetailNewProductPresenter topAdsDetailNewProductPresenter;

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        topAdsDetailNewProductPresenter.attachView(this);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd.setShopId(Long.parseLong(SessionHandler.getShopID(getActivity())));
        detailAd.setType(TopAdsNetworkConstant.TYPE_PRODUCT_STAT);
    }

    @Override
    protected void onClickedNext() {
        super.onClickedNext();
        if (stepperModel == null) {
            stepperModel = new TopAdsCreatePromoWithoutGroupModel();
        }
        stepperModel.setDetailGroupCostViewModel(detailAd);
        topAdsDetailNewProductPresenter.saveAd(stepperModel.getDetailProductViewModel(), new ArrayList<>(stepperModel.getTopAdsProductViewModels()));
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
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
            hideLoading();
            stepperListener.finishPage();
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
