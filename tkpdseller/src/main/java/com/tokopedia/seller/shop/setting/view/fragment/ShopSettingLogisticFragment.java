package com.tokopedia.seller.shop.setting.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSetingLogisticModule;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLogisticFragment extends BaseDaggerFragment implements ShopSettingLogisticView {

    public static final String TAG = "ShopSettingLogistic";
    public static final int UNSELECTED_DISTRICT_VIEW = -1;

    @Inject
    public ShopSettingLogisticPresenter presenter;
    private ShopSetingLogisticComponent component;
    private int districtCode = UNSELECTED_DISTRICT_VIEW;

    public static ShopSettingLogisticFragment getInstance() {
        return new ShopSettingLogisticFragment();
    }

    @Override
    protected void initInjector() {
        component = DaggerShopSetingLogisticComponent
                .builder()
                .shopSetingLogisticModule(new ShopSetingLogisticModule())
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_logistic, container, false);
        updateLogistic();
        return view;
    }

    @Override
    public void changeDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    private void updateLogistic() throws RuntimeException {
        if (districtCode != UNSELECTED_DISTRICT_VIEW) {
            presenter.updateLogistic(districtCode);
        } else {
            throw new RuntimeException("District code must be selected");
        }
    }

    private void showMessageError(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

//    private void onSelected() {
//        try {
//            updateLogistic();
//        } catch (Exception e) {
//            showMessageError(getString(R.string.shop_setting_city_not_filled));
//            listener.goBackToLocation();
//        }
//    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
