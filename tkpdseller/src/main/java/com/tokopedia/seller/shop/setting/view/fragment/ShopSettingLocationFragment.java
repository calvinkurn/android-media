package com.tokopedia.seller.shop.setting.view.fragment;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingLocationModule;
import com.tokopedia.seller.shop.setting.view.adapter.LocationCityAdapter;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;

import java.util.List;

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
    private AutoCompleteTextView locationDistrictTextView;
    private LocationCityAdapter locationDistrictAdapter;

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
        setupTextLocationDistrict(view);

        view.findViewById(R.id.verify_button)
                .setOnClickListener(getVerifyButton());
        textViewShopSettingLocationPickup =
                (TextView) view.findViewById(R.id.text_view_shop_setting_location_pickup);
    }

    private void setupTextLocationDistrict(View view) {
        locationDistrictTextView = (AutoCompleteTextView) view.findViewById(R.id.edit_text_shop_setting_location_district);
        locationDistrictAdapter = new LocationCityAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        locationDistrictTextView.setAdapter(locationDistrictAdapter);
        locationDistrictTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.getRecomendationLocationDistrict(s.toString());
            }
        });
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
    public void renderRecomendationDistrictModel(List<RecommendationDistrictViewModel> viewModels) {
        locationDistrictAdapter.addAll(viewModels);

    }

    @Override
    public void showGenericError() {
        SnackbarManager.make(getActivity(), "Terjadi Kesalahan", Snackbar.LENGTH_SHORT);
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
