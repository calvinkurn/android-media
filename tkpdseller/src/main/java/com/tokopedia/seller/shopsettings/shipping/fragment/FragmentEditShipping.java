package com.tokopedia.seller.shopsettings.shipping.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.district_recommendation.domain.model.Address;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.district_recommendation.view.DistrictRecommendationActivity;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.customview.CourierView;
import com.tokopedia.seller.shopsettings.shipping.customview.ShippingAddressLayout;
import com.tokopedia.seller.shopsettings.shipping.customview.ShippingHeaderLayout;
import com.tokopedia.seller.shopsettings.shipping.customview.ShippingInfoBottomSheet;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.Courier;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;
import com.tokopedia.seller.shopsettings.shipping.presenter.EditShippingPresenter;
import com.tokopedia.seller.shopsettings.shipping.presenter.EditShippingPresenterImpl;

import static com.tokopedia.district_recommendation.view.DistrictRecommendationContract.Constant.INTENT_DATA_ADDRESS;

/**
 * Created by Kris on 2/19/2016.
 * TOKOPEDIA
 */
public class FragmentEditShipping extends Fragment implements EditShippingViewListener {

    private static final int GET_DISTRICT_RECCOMENDATION_REQUEST_CODE = 100;

    LinearLayout fragmentShipingMainLayout;

    ShippingHeaderLayout fragmentShippingHeader;

    ShippingAddressLayout addressLayout;

    TextView submitButtonCreateShop;

    LogisticRouter logisticRouter;

    private EditShippingPresenter editShippingPresenter;
    private TkpdProgressDialog mainProgressDialog;
    private TkpdProgressDialog progressDialog;
    private InputMethodManager inputMethodManager;
    private int mapMode;

    public static FragmentEditShipping createInstance() {
        FragmentEditShipping fragment = new FragmentEditShipping();
        Bundle bundle = new Bundle();
        bundle.putInt(MAP_MODE, SETTING_PAGE);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FragmentEditShipping createShopInstance() {
        FragmentEditShipping fragment = new FragmentEditShipping();
        Bundle bundle = new Bundle();
        bundle.putInt(MAP_MODE, CREATE_SHOP_PAGE);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FragmentEditShipping resumeShopInstance(Parcelable previousOpenShopState) {
        FragmentEditShipping fragment = new FragmentEditShipping();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESUME_OPEN_SHOP_DATA_KEY, previousOpenShopState);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_shop_shipping, container, false);
        initiateVariables(mainView);
        hideAllView();
        setHasOptionsMenu(isEditShipping());
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentShippingHeader.setViewListener(this);
        addressLayout.setViewListener(this);
        fragmentShippingHeader.setListener(editShippingPresenter);
        addressLayout.setListener(editShippingPresenter);
        if (getArguments().containsKey(RESUME_OPEN_SHOP_DATA_KEY)) {
            editShippingPresenter.setSavedInstance(getArguments());
        } else {
            editShippingPresenter.setSavedInstance(savedInstanceState);
        }

        if (getActivity().getApplicationContext() instanceof LogisticRouter) {
            logisticRouter = (LogisticRouter) getActivity().getApplicationContext();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
        NetworkErrorHelper.removeEmptyState(getView());
        if (editShippingPresenter.getOpenShopModel() != null) {
            editShippingPresenter.bindDataToViewOpenShop(editShippingPresenter.getOpenShopModel());
        } else if (editShippingPresenter.getShopModel() != null) {
            editShippingPresenter.bindDataToView(editShippingPresenter.getShopModel());
        } else getData();
    }

    private void hideAllView() {
        fragmentShippingHeader.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
        fragmentShipingMainLayout.setVisibility(View.GONE);
    }

    private void getData() {
        if (getArguments().getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
            getShippingDataCreateShop();
        } else {
            getShippingData();
        }
    }

    private void initiateVariables(View mainView) {
        mapMode = getArguments().getInt(MAP_MODE);
        mainProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS);
        mainProgressDialog.setCancelable(true);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        editShippingPresenter = new EditShippingPresenterImpl(this);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        fragmentShipingMainLayout = (LinearLayout) mainView.findViewById(R.id.fragment_shipping_main_layout);
        fragmentShippingHeader = (ShippingHeaderLayout) mainView.findViewById(R.id.fragment_shipping_header);
        addressLayout = (ShippingAddressLayout) mainView.findViewById(R.id.shipping_address_layout);
        submitButtonCreateShop = (TextView) mainView.findViewById(R.id.submit_button_create_shop);
        submitButtonCreateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonOnClickListener();
            }
        });
    }

    private boolean isEditShipping() {
        return getArguments().getInt(MAP_MODE) == SETTING_PAGE;
    }

    private void getShippingData() {
        mainProgressDialog.showDialog();
        editShippingPresenter.fetchData();
    }

    private void getShippingDataCreateShop() {
        editShippingPresenter.fetchDataOpenShop();
        fragmentShippingHeader.setVisibility(View.GONE);
        mainProgressDialog.showDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editShippingPresenter.onViewDestroyed();
    }

    @Override
    public Context getMainContext() {
        return getActivity();
    }

    @Override
    public void addCourier(Courier courier, int courierIndex) {
        CourierView courierView = new CourierView(getActivity());
        fragmentShipingMainLayout.addView(courierView);
        courierView.setViewListener(this);
        courierView.renderData(courier, courierIndex);
    }

    @Override
    public void setShopDetailedInformation(ShopShipping data) {
        addressLayout.renderData(data);
    }

    @Override
    public void setShopLocationData(ShopShipping shopData) {
        fragmentShippingHeader.renderData(shopData);
    }

    @Override
    public String getDistrictAndCity() {
        return fragmentShippingHeader.getDistrictAndCity();
    }

    @Override
    public String getZipCode() {
        return fragmentShippingHeader.getZipCodeData();
    }

    @Override
    public String getStreetAddress() {
        return addressLayout.getAddressData();
    }

    @Override
    public void zipCodeEmpty() {
        fragmentShippingHeader.setZipCodeError(getActivity().getString(R.string.error_field_required));
    }

    @Override
    public void noServiceChosen() {
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity()
                .getString(R.string.error_shipping_must_choose));
    }

    @Override
    public void finishLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void finishStartingFragment() {
        mainProgressDialog.dismiss();
    }

    @Override
    public void setLocationProvinceCityDistrict(String Province, String City, String District) {
        fragmentShippingHeader.updateLocationData(Province, City, District);
    }

    @Override
    public void setLocationProvinceCityDistrict() {
        fragmentShippingHeader.updateLocationData(getString(R.string.hint_choose_city));
    }

    @Override
    public void initializeZipCodes() {
        fragmentShippingHeader.initializeZipCodes();
    }

    @Override
    public void refreshLocationViewListener(ShopShipping updatedShopShipping) {
        refreshView();
        fragmentShippingHeader.updateLocationData(updatedShopShipping.provinceName,
                updatedShopShipping.cityName,
                updatedShopShipping.districtName);
    }

    @Override
    public void refreshLocationViewListener(Address address) {
        refreshView();
        fragmentShippingHeader.updateLocationData(address.getProvinceName(),
                address.getCityName(), address.getDistrictName());
    }

    @Override
    public void locationDialogTimeoutListener() {
        progressDialog.dismiss();
    }

    @Override
    public void dismissFragment(String messageStatus) {
        Toast.makeText(getActivity(), messageStatus, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void openWebView(String webResources, int courierIndex) {
        progressDialog.dismiss();
        FragmentManager fm = getActivity().getFragmentManager();
        EditShippingWebViewDialog dialog = EditShippingWebViewDialog
                .openAdditionalOptionDialog(webResources, courierIndex);
        if (getFragmentManager().findFragmentByTag("web_view_dialog") == null) {
            dialog.setTargetFragment(FragmentEditShipping.this, ADDITIONAL_OPTION_REQUEST_CODE);
            dialog.show(fm, "web_view_dialog");
        }
    }

    @Override
    public void showErrorToast(String error) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentTimeout() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mainProgressDialog.showDialog();
                if (mapMode == CREATE_SHOP_PAGE) editShippingPresenter.fetchDataOpenShop();
                else editShippingPresenter.fetchData();
            }
        });
    }

    @Override
    public void onFragmentNoConnection() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                getString(R.string.msg_no_connection),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (mapMode == CREATE_SHOP_PAGE) editShippingPresenter.fetchDataOpenShop();
                        else editShippingPresenter.fetchData();
                    }
                });
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onShowViewAfterLoading() {
        getActivity().invalidateOptionsMenu();
        fragmentShippingHeader.setVisibility(View.VISIBLE);
        addressLayout.setVisibility(View.VISIBLE);
        fragmentShipingMainLayout.setVisibility(View.VISIBLE);
        if (getArguments().getInt(MAP_MODE) == CREATE_SHOP_PAGE)
            submitButtonCreateShop.setVisibility(View.VISIBLE);
        else if (getArguments().containsKey(RESUME_OPEN_SHOP_DATA_KEY)) {
            submitButtonCreateShop.setVisibility(View.VISIBLE);
        } else submitButtonCreateShop.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void openDataWebViewResources(int courierIndex) {
        editShippingPresenter.dataWebViewResource(courierIndex,
                editShippingPresenter.getCourierAdditionalOptionsURL(courierIndex));
    }

    @Override
    public void setServiceCondition(boolean isChecked, int serviceIndex, int courierIndex) {
        editShippingPresenter.setServiceCondition(isChecked, serviceIndex, courierIndex);
    }

    @Override
    public void editAddress() {
        startActivityForResult(DistrictRecommendationActivity.createInstanceIntent(getActivity(),
                editShippingPresenter.getToken()),
                GET_DISTRICT_RECCOMENDATION_REQUEST_CODE);
    }

    public boolean editShippingValid() {
        return editShippingPresenter.editShippingParamsValid();
    }

    public OpenShopData getCurrentShippingConfiguration() {
        return editShippingPresenter.passShippingData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GET_DISTRICT_RECCOMENDATION_REQUEST_CODE:
                    Address address = data.getParcelableExtra(INTENT_DATA_ADDRESS);
                    editShippingPresenter.setSelectedAddress(address);
                    fragmentShippingHeader.initializeZipCodes();
                    fragmentShippingHeader.updateLocationData(
                            address.getProvinceName(),
                            address.getCityName(),
                            address.getDistrictName());
                    changeLocationRequest(address.getDistrictId());
                    break;
                case OPEN_MAP_CODE:
                    changeGoogleMapData(data);
                    break;
                case ADDITIONAL_OPTION_REQUEST_CODE:
                    additionalOptionRequest(data);
                    inputMethodManager.hideSoftInputFromWindow(fragmentShipingMainLayout
                            .getWindowToken(), 0);
            }
        }
    }

    private void changeLocationRequest(int originId) {
        progressDialog.showDialog();
        if (getArguments().getInt(MAP_MODE) == CREATE_SHOP_PAGE || getArguments().containsKey(RESUME_OPEN_SHOP_DATA_KEY)) {
            editShippingPresenter.fetchDataByLocationOpenShop(String.valueOf(originId));
        } else {
            editShippingPresenter.fetchDataByLocation(String.valueOf(originId));
        }
    }

    private void changeGoogleMapData(Intent data) {
        addressLayout.setGoogleMapData(data);
    }

    private void additionalOptionRequest(Intent data) {
        editShippingPresenter.setCourierAdditionalOptionConfig(
                data.getIntExtra(MODIFIED_COURIER_INDEX_KEY, 0),
                data.getStringExtra(EDIT_SHIPPING_RESULT_KEY));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isAdded() && getActivity() != null) {
            if(GlobalConfig.isCustomerApp()) {
                getActivity().getMenuInflater().inflate(R.menu.save_btn_black, menu);
            } else {
                getActivity().getMenuInflater().inflate(R.menu.save_btn, menu);
            }
            MenuItem item = menu.findItem(R.id.action_send);
            item.setTitle(getString(R.string.title_action_save_shipping));
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_send);
        if (!fragmentShippingHeader.isShown()) item.setVisible(false);
        else item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        } else if (item.getItemId() == R.id.action_send) {
            if (fragmentShippingHeader.isShown()) {
                submitData();
            } else showErrorToast(getString(R.string.dialog_on_process));
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitData() {
        editShippingPresenter.submitValue();
    }

    private void refreshView() {
        fragmentShipingMainLayout.removeAllViews();
        editShippingPresenter.refreshData();
    }

    void submitButtonOnClickListener() {
        if (fragmentShipingMainLayout.getChildCount() > 1 && editShippingValid()) {
            Intent intent = new Intent();
            intent.putExtra(EDIT_SHIPPING_DATA, getCurrentShippingConfiguration());
            getActivity().setResult(OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE, intent);
            getActivity().finish();
        } else if (fragmentShipingMainLayout.getChildCount() < 1) {
            showErrorToast(getActivity().getString(R.string.title_select_shop_location));
            UnifyTracking.eventCreateShopFillLogisticError(getActivity());
        }
    }

    @Override
    public void openGeoLocation() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {
            LocationPass locationPass = new LocationPass();

            if (!editShippingPresenter.getShopInformation().getShopLatitude().isEmpty()
                    && !editShippingPresenter.getShopInformation().getShopLongitude().isEmpty()) {

                locationPass.setLatitude(editShippingPresenter.getShopInformation().getShopLatitude());
                locationPass.setLongitude(editShippingPresenter.getShopInformation().getShopLongitude());
                locationPass.setGeneratedAddress(addressLayout.getGoogleMapAddressString());
            } else {
                locationPass.setDistrictName(
                        editShippingPresenter.getShopInformation().getDistrictName()
                );
                locationPass.setCityName(editShippingPresenter.getShopInformation().getCityName());
            }
            Intent intent = logisticRouter.navigateToGeoLocationActivityRequest(getActivity(), locationPass);
            startActivityForResult(intent, OPEN_MAP_CODE);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    @Override
    public void showInfoBottomSheet(String information, String serviceName) {
        new ShippingInfoBottomSheet(information, serviceName, getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getArguments().getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
            editShippingPresenter.saveOpenShopModel();
            outState.putParcelable(CURRENT_OPEN_SHOP_MODEL,
                    editShippingPresenter.getOpenShopModel());
        } else {
            outState.putParcelable(CURRENT_COURIER_MODEL,
                    editShippingPresenter.getShopModel());
        }
    }

}
