package com.tokopedia.seller.shop.setting.view.fragment;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingLocationModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLocationFragment extends BaseDiFragment<ShopSettingLocationPresenter> implements ShopSettingLocationView {
    public static final String TAG = "ShopSettingLocation";
    private ShopSettingLocationComponent component;
    private TextView textViewShopSettingLocationPickup;

    public static ShopSettingLocationFragment getInstance() {
        return new ShopSettingLocationFragment();
    }

    @Override
    protected void initInjection() {
        component = DaggerShopSettingLocationComponent
                .builder()
                .shopSettingLocationModule(new ShopSettingLocationModule(this))
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
    }

    @Override
    protected ShopSettingLocationPresenter getPresenter() {
        return component.getPresenter();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_setting_location;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.verify_button)
                .setOnClickListener(getVerifyButton());
        textViewShopSettingLocationPickup =
                (TextView) view.findViewById(R.id.text_view_shop_setting_location_pickup);
    }


    public View.OnClickListener getVerifyButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeLocationPickup();

            }
        };
    }

    @Override
    public void changeLocationString(String s) {
        textViewShopSettingLocationPickup.setText(s);
    }
}
