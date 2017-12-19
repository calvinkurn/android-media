package com.tokopedia.seller.shop.setting.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSetingLogisticModule;
import com.tokopedia.seller.shop.setting.domain.model.LogisticAvailableDomainModel;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLogisticFragment extends BaseDaggerFragment implements ShopSettingLogisticView {

    public static final int UNSELECTED_DISTRICT_VIEW = -1;

    @Inject
    public ShopSettingLogisticPresenter presenter;
    private int districtCode = UNSELECTED_DISTRICT_VIEW;
    private LoadingStateView loadingStateView;
    private boolean needRefreshData;

    public static ShopSettingLogisticFragment getInstance() {
        return new ShopSettingLogisticFragment();
    }

    @Override
    protected void initInjector() {
        ShopSetingLogisticComponent component = DaggerShopSetingLogisticComponent
                .builder()
                .shopSetingLogisticModule(new ShopSetingLogisticModule())
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needRefreshData = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_logistic, container, false);
        loadingStateView = view.findViewById(R.id.vg_root);
        // changeDistrictCode(4528);
        return view;
    }

    public void changeDistrictCode(int districtCode) {
        if (districtCode!= this.districtCode) {
            this.districtCode = districtCode;
            needRefreshData = true;
        }
    }

    public void updateLogistic() {
        showLoading();
        //presenter.updateLogistic(districtCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        if (needRefreshData) {
            updateLogistic();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachView();
    }

    private void showLoading() {
        loadingStateView.setViewState(LoadingStateView.VIEW_LOADING);
    }

    private void hideLoading() {
        loadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
    }

    @Override
    public void onSuccessLoadLogistic(LogisticAvailableDomainModel logisticAvailableDomainModel) {
        hideLoading();
        needRefreshData = false;
        //TODO on Success
    }

    @Override
    public void onErrorLoadLogistic(Throwable t) {
        hideLoading();
        needRefreshData = false;
        String message = ErrorHandler.getErrorMessage(t);
        if (!TextUtils.isEmpty(message)) {
            showMessageError(message);
        }
    }

    private void showMessageError(String messsage) {
        NetworkErrorHelper.showEmptyState(getActivity(),
                        loadingStateView, messsage,
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                updateLogistic();
                            }
                        }
                );
    }

    @Override
    protected String getScreenName() {
        return null;
    }


}
