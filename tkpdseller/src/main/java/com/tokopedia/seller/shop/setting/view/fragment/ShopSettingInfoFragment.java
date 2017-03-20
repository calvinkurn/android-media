package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoView;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingInfoFragment extends BasePresenterFragment<ShopSettingInfoView> {

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_setting_info;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    public static ShopSettingInfoFragment createInstance() {
        return new ShopSettingInfoFragment();
    }
}
