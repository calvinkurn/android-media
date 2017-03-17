package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenterImpl;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLocationFragment extends BasePresenterFragment<ShopSettingLocationPresenterImpl> implements ShopSettingLocationView {
    public static final String TAG = "ShopSettingLocation";

    public static ShopSettingLocationFragment getInstance() {
        return new ShopSettingLocationFragment();
    }

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
        return R.layout.fragment_shop_setting_location;
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
        presenter.fetchDistrictData();

    }


}
