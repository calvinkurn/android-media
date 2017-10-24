package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingLocationModule;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingDistrictViewHolderListener;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLocationPickupViewHolderListener;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLocationView;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;
import com.tokopedia.seller.shop.setting.view.model.ShopSettingLocationModel;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.viewholder.ShopSettingDistrictViewHolder;
import com.tokopedia.seller.shop.setting.view.viewholder.ShopSettingLocationPickupViewHolder;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */
public class ShopSettingLocationFragment extends BaseDaggerFragment implements
        ShopSettingLocationView, ShopSettingDistrictViewHolderListener, ShopSettingLocationPickupViewHolderListener {

    private static final int OPEN_MAP_CODE = 1000;

    @Inject
    public ShopSettingLocationPresenter presenter;
    private ShopSettingLocationComponent component;
    private TkpdProgressDialog tkpdProgressDialog;
    private ShopSettingDistrictViewHolder shopSettingDistrictViewHolder;
    private ShopSettingLocationPickupViewHolder shopSettingLocationPickupViewHolder;

    public static ShopSettingLocationFragment getInstance() {
        return new ShopSettingLocationFragment();
    }

    @Override
    protected void initInjector() {
        component = DaggerShopSettingLocationComponent
                .builder()
                .shopSettingLocationModule(new ShopSettingLocationModule())
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_location, container, false);
        shopSettingDistrictViewHolder = new ShopSettingDistrictViewHolder(getActivity(), view, this);
        shopSettingLocationPickupViewHolder = new ShopSettingLocationPickupViewHolder(getActivity(), view, this);
        view.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked();
            }
        });
        presenter.attachView(this);
        presenter.getDistrictData();
        return view;
    }

    protected void onNextButtonClicked() {

    }

    protected ShopSettingLocationModel getDataModel() throws RuntimeException {
        ShopSettingLocationModel model = new ShopSettingLocationModel();
        model.setDistrictCode(shopSettingDistrictViewHolder.getDistrictCode());
        model.setPostalCode(shopSettingLocationPickupViewHolder.getPostalCode());
        return model;
    }

    @Override
    public void showRetryGetDistrictData() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getDistrictData();
            }
        });
    }

    @Override
    public void renderRecomendationDistrictModel(RecommendationDistrictViewModel viewModels) {
        shopSettingDistrictViewHolder.renderRecomendationDistrictModel(viewModels);
    }

    @Override
    public void getRecomendationLocationDistrict(String stringTyped) {
        presenter.getRecommendationLocationDistrict(stringTyped);
    }

    @Override
    public void goToPickupLocationPicker(LocationPass locationPass) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {
            Intent intent = GeolocationActivity.createInstance(getActivity(), locationPass);
            startActivityForResult(intent, OPEN_MAP_CODE);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_MAP_CODE:
                    LocationPass locationPass = data.getParcelableExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                    changePickupLocation(locationPass);
                    break;
            }
        }
    }

    @Override
    public void changePickupLocation(LocationPass locationPass) {
        shopSettingLocationPickupViewHolder.changePickupLocation(locationPass);
    }

    @Override
    public void showGenericError() {
        SnackbarManager.make(getActivity(), "Terjadi Kesalahan", Snackbar.LENGTH_SHORT);
    }

    @Override
    public void showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
        tkpdProgressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}