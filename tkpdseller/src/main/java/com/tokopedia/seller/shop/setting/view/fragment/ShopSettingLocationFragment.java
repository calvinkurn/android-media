package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.network.NetworkErrorHelper;
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
        implements ShopSettingLocationView {
    public static final String TAG = "ShopSettingLocation";

    @Inject
    public ShopSettingLocationPresenter shopSettingLocationPresenter;
    private LocationCityAdapter locationDistrictAdapter;
    private TkpdProgressDialog tkpdProgressDialog;
    private ShopSettingLocationListener listener;

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
    protected void initialListener(Activity activity) {
        if (activity instanceof ShopSettingLocationListener) {
            this.listener = ((ShopSettingLocationListener)activity);
        } else {
            throw new RuntimeException("Please implement ShopSettingLocationListener to the activity");
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_setting_location;
    }

    @Override
    protected void initView(View view) {
        setupTextLocationDistrict(view);
    }

    private void setupTextLocationDistrict(View view) {
        AutoCompleteTextView locationDistrictTextView = (AutoCompleteTextView) view.findViewById(R.id.edit_text_shop_setting_location_district);
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
        locationDistrictTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                locationDistrictAdapter.setSelected(position);
            }
        });
    }

    @Override
    protected void setActionVar() {
        presenter.getDistrictData();
    }

    @Override
    public void renderRecomendationDistrictModel(List<RecommendationDistrictViewModel> viewModels) {
        locationDistrictAdapter.addDistrictModel(viewModels);
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
    public void showRetryGetDistrictData() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getDistrictData();
            }
        });
    }


}
