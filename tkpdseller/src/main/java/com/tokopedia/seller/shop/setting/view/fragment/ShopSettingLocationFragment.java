package com.tokopedia.seller.shop.setting.view.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingLocationModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */
public class ShopSettingLocationFragment
        extends BaseDiFragment<ShopSettingLocationComponent, ShopSettingLocationPresenter>
        implements ShopSettingLocationView, Step {
    public static final String TAG = "ShopSettingLocation";
    @Inject
    public ShopSettingLocationPresenter shopSettingLocationPresenter;
    private TextView textViewShopSettingLocationPickup;

    public static ShopSettingLocationFragment getInstance() {
        return new ShopSettingLocationFragment();
    }

    @Override
    protected ShopSettingLocationComponent initInjection() {
        return DaggerShopSettingLocationComponent
                .builder()
                .shopSettingLocationModule(new ShopSettingLocationModule(this))
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
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

    @Override
    protected void setActionVar() {
        presenter.getDistrictData();
    }


    public View.OnClickListener getVerifyButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
