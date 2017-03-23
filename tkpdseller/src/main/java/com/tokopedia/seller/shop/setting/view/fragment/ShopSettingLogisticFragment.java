package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSetingLogisticModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticView;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLogisticFragment
        extends BaseDiFragment<ShopSetingLogisticComponent, ShopSettingLogisticPresenter>
        implements ShopSettingLogisticView {
    public static final String TAG = "ShopSettingLogistic";
    public static final String DISTRICT_CODE = "DISTRICT_CODE";
    public static final int UNSELECTED_DISTRICT_VIEW = -1;
    private int districtCode;

    public static Fragment getInstance(int districtCode) {
        ShopSettingLogisticFragment fragment = new ShopSettingLogisticFragment();
        Bundle args = new Bundle();
        args.putInt(DISTRICT_CODE, districtCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ShopSetingLogisticComponent initInjection() {
        return DaggerShopSetingLogisticComponent
                .builder()
                .shopSetingLogisticModule(new ShopSetingLogisticModule(this))
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_setting_logistic;
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        districtCode = arguments.getInt(DISTRICT_CODE, UNSELECTED_DISTRICT_VIEW);
    }

}
