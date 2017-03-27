package com.tokopedia.seller.shop.setting.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSetingLogisticModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticView;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLogisticFragment
        extends BaseDaggerFragment
        implements ShopSettingLogisticView {
    public static final String TAG = "ShopSettingLogistic";
    public static final String DISTRICT_CODE = "DISTRICT_CODE";
    public static final int UNSELECTED_DISTRICT_VIEW = -1;
    @Inject
    public ShopSettingLogisticPresenter presenter;
    private ShopSetingLogisticComponent component;
    private int districtCode;

    public static ShopSettingLogisticFragment getInstance(int districtCode) {
        ShopSettingLogisticFragment fragment = new ShopSettingLogisticFragment();
        Bundle args = new Bundle();
        args.putInt(DISTRICT_CODE, districtCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupArguments(getArguments());
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
        return view;
    }


    protected void setupArguments(Bundle arguments) {
        districtCode = arguments.getInt(DISTRICT_CODE, UNSELECTED_DISTRICT_VIEW);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
