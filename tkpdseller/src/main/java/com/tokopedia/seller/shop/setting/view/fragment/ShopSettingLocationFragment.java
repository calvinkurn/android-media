package com.tokopedia.seller.shop.setting.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingLocationModule;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;
import com.tokopedia.seller.shop.setting.view.model.ShopSettingLocationModel;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationView;
import com.tokopedia.seller.shop.setting.view.viewholder.DistrictViewHolder;
import com.tokopedia.seller.shop.setting.view.viewholder.DistrictViewHolderListener;
import com.tokopedia.seller.shop.setting.view.viewholder.LocationPickupViewHolder;
import com.tokopedia.seller.shop.setting.view.viewholder.LocationPickupViewHolderListener;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */
public class ShopSettingLocationFragment
        extends BaseDaggerFragment
        implements ShopSettingLocationView,
        DistrictViewHolderListener,
        LocationPickupViewHolderListener {
    public static final String TAG = "ShopSettingLocation";
    @Inject
    public ShopSettingLocationPresenter presenter;
    private ShopSettingLocationComponent component;
    private TkpdProgressDialog tkpdProgressDialog;
    private ShopSettingLocationListener listener;
    private DistrictViewHolder districtViewHolder;
    private LocationPickupViewHolder locationPickupViewHolder;


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShopSettingLocationListener) {
            this.listener = ((ShopSettingLocationListener) context);
        } else {
            throw new RuntimeException("Please implement ShopSettingLocationListener to the activity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_location, container, false);
        districtViewHolder = new DistrictViewHolder(getActivity(), view, this);
        locationPickupViewHolder = new LocationPickupViewHolder(getActivity(), view, this);
        view
                .findViewById(R.id.continue_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        continueToNextStep();
                    }
                });
        presenter.attachView(this);
        presenter.getDistrictData();
        return view;
    }

    private void continueToNextStep() {
        try {
            ShopSettingLocationModel model = getDataModel();
            listener.goToShopSettingLogisticFragment(model);
        } catch (Exception e) {

        }
    }

    private ShopSettingLocationModel getDataModel() throws RuntimeException {
        ShopSettingLocationModel model = new ShopSettingLocationModel();
        model.setDistrictCode(districtViewHolder.getDistrictCode());
        model.setPostalCode(locationPickupViewHolder.getPostalCode());
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
        districtViewHolder.renderRecomendationDistrictModel(viewModels);
    }

    @Override
    public void getRecomendationLocationDistrict(String stringTyped) {
        presenter.getRecomendationLocationDistrict(stringTyped);
    }

    @Override
    public void goToPickupLocationPicker(LocationPass locationPass) {
        listener.goToPickupLocationPicker(locationPass);
    }

    @Override
    public void changePickupLocation(LocationPass locationPass) {
        locationPickupViewHolder.changePickupLocation(locationPass);
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
