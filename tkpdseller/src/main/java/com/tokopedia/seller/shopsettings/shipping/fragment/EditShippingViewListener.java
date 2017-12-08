package com.tokopedia.seller.shopsettings.shipping.fragment;

import android.content.Context;

import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.Courier;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;

/**
 * Created by Kris on 2/23/2016.
 TOKOPEDIA
 */
public interface EditShippingViewListener {

    int LOCATION_FRAGMENT_REQUEST_CODE = 1;

    int ADDITIONAL_OPTION_REQUEST_CODE = 2;

    int OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE = 3;

    String SELECTED_LOCATION_ID_KEY = "location_id";

    String MAP_MODE = "map_mode";

    String EDIT_SHIPPING_RESULT_KEY = "edit_shipping_result";

    String MODIFIED_COURIER_INDEX_KEY = "modified_courier_index";

    String EDIT_SHIPPING_DATA = "edit_shipping_data";

    int OPEN_MAP_CODE = 10;

    int SETTING_PAGE = 12;

    int CREATE_SHOP_PAGE = 13;

    String CURRENT_COURIER_MODEL = "current_courier_model";

    String CURRENT_OPEN_SHOP_MODEL = "current_open_shop_model";

    String RESUME_OPEN_SHOP_DATA_KEY = "resume_open_shop_data_key";

    Context getMainContext();

    void addCourier(Courier courier, int courierIndex);

    void setShopDetailedInformation(ShopShipping data);

    void setShopLocationData(ShopShipping shopData);

    String getDistrictAndCity();

    String getZipCode();

    String getStreetAddress();

    void openGeoLocation();

    void zipCodeEmpty();

    void noServiceChosen();

    void finishLoading();

    void finishStartingFragment();

    void setLocationProvinceCityDistrict(String Province, String City, String District);

    void setLocationProvinceCityDistrict();

    void initializeZipCodes();

    void refreshLocationViewListener(ShopShipping updatedShopInfo);

    void refreshLocationViewListener(Address address);

    void locationDialogTimeoutListener();

    void dismissFragment(String messageStatus);

    void openWebView(String webResources, int courierIndex);

    void showErrorToast(String error);

    void onFragmentTimeout();

    void onFragmentNoConnection();

    void onShowViewAfterLoading();

    void showLoading();

    void openDataWebViewResources(int courierIndex);

    void setServiceCondition(boolean isChecked, int serviceIndex, int courierIndex);

    void editAddress();

    void showInfoBottomSheet(String information, String courierServiceName);

}