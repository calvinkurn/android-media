package com.tokopedia.seller.shopsettings.shipping.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.City;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.District;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ProvinceCitiesDistrict;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.ShopShipping;
import com.tokopedia.seller.shopsettings.shipping.presenter.EditShippingLocationDialogPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 5/3/2016.
 TOKOPEDIA
 */
public class ShippingLocationDialog extends DialogFragment implements EditShippingLocationDialogPresenter {

    Spinner provinceSpinner;
    Spinner citySpinner;
    Spinner districtSpinner;
    TextView confirmEditButton;
    TextView cancelButton;
    TextView selectProvinceText;
    TextView selectCityText;
    TextView selectDistrictText;

    private static final String PROVINCE_CITY_DISTRICT_KEY = "province_cities_districts_data";
    private static final String SHOP_DATA = "shop_data";
    private static final String CURRENT_LOCATION_LIST = "current_location_data";
    private static final String CURRENT_SHOP_DATA = "current_shop_data";
    private List<ProvinceCitiesDistrict> listLocations;
    private ShopShipping shop;
    private List<String> provinceNameList = new ArrayList<>();
    private List<String> cityNameList = new ArrayList<>();
    private List<String> districtNameList = new ArrayList<>();
    private List<String> districtIdList = new ArrayList<>();
    
    public ShippingLocationDialog() {

    }

    public static ShippingLocationDialog createDialog(List<ProvinceCitiesDistrict> listLocations,
                                                      ShopShipping shop) {
        ShippingLocationDialog dialogFragment = new ShippingLocationDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PROVINCE_CITY_DISTRICT_KEY, (ArrayList<? extends Parcelable>) listLocations);
        bundle.putParcelable(SHOP_DATA, shop);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setDataFromBundle(getArguments());
        View view = inflater.inflate(R.layout.edit_shipping_location_dialog, container, false);

        provinceSpinner = (Spinner) view.findViewById(R.id.edit_shipping_province_spinner);
        citySpinner = (Spinner) view.findViewById(R.id.edit_shipping_city_spinner);
        districtSpinner = (Spinner) view.findViewById(R.id.edit_shipping_district_spinner);

        confirmEditButton = (TextView) view.findViewById(R.id.edit_shipping_confirm_button);
        cancelButton = (TextView) view.findViewById(R.id.edit_shipping_cancel_button);

        selectProvinceText = (TextView) view.findViewById(R.id.edit_shipping_select_province_text);

        selectCityText = (TextView) view.findViewById(R.id.edit_shipping_select_city_text);
        selectDistrictText = (TextView) view.findViewById(R.id.edit_shipping_select_district_text);

        confirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataLocation();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateDistrictSpinner(provinceSpinner.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showSpinnerByProvinceType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        populateSpinner();
        return view;
    }

    private void setDataFromBundle(Bundle dataBundle) {
        listLocations = dataBundle.getParcelableArrayList(PROVINCE_CITY_DISTRICT_KEY);
        shop = dataBundle.getParcelable(SHOP_DATA);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void populateSpinner(){
        int provinceIndex = 0;
        for(int i = 0; i < listLocations.size(); i++){
            provinceNameList.add(listLocations.get(i).provinceName);
            if(listLocations.get(i).provinceId.equals(shop.provinceId)){
                provinceIndex = i;
            }
        }
        provinceSpinner.setAdapter(SimpleSpinnerAdapter
                .createAdapter(getActivity(), provinceNameList));
        provinceSpinner.setSelection(provinceIndex);
        showSpinnerByProvinceType(provinceIndex);
    }

    private void showSpinnerByProvinceType(int provinceIndex){
        if(listLocations.get(provinceIndex).cities.size()<1){
            citySpinner.setVisibility(View.GONE);
            districtSpinner.setVisibility(View.GONE);
            selectCityText.setVisibility(View.GONE);
            selectDistrictText.setVisibility(View.GONE);
        }else{
            citySpinner.setVisibility(View.VISIBLE);
            districtSpinner.setVisibility(View.VISIBLE);
            selectCityText.setVisibility(View.VISIBLE);
            selectDistrictText.setVisibility(View.VISIBLE);
            populateCitySpinner(provinceIndex);
        }
    }

    private void populateCitySpinner(int selectedProvince){
        int selectedCity = 0;
        cityNameList.clear();
        List<City> cities = listLocations.get(selectedProvince).cities;
        for(int i = 0; i < cities.size(); i++){
            cityNameList.add(cities.get(i).cityName);
            if(cities.get(i).cityId.equals(shop.cityId)){
                selectedCity = i;
            }
        }
        citySpinner.setAdapter(SimpleSpinnerAdapter.createAdapter(getActivity(), cityNameList));
        citySpinner.setSelection(selectedCity);
        populateDistrictSpinner(selectedProvince, selectedCity);
    }

    private void populateDistrictSpinner(int selectedProvince, int selectedCity){
        int districtIndex = 0;
        districtNameList.clear();
        districtIdList.clear();
        List<District> districts = listLocations.get(selectedProvince).cities.get(selectedCity).districts;
        for(int i = 0; i < districts.size(); i++){
            districtNameList.add(districts.get(i).districtName);
            districtIdList.add(districts.get(i).districtId.toString());
            if(districts.get(i).districtId.equals(shop.districtId)){
                districtIndex = i;
            }
        }
        districtSpinner.setAdapter(SimpleSpinnerAdapter.createAdapter(getActivity(), districtNameList));
        districtSpinner.setSelection(districtIndex);
    }

    private void setDataLocation(){
        Intent intent = new Intent();
        if(isJakarta()){
            intent.putExtra(EditShippingViewListener.SELECTED_LOCATION_ID_KEY, "5573");
        }else{
            intent.putExtra(EditShippingViewListener.SELECTED_LOCATION_ID_KEY,
                    districtIdList.get(districtSpinner.getSelectedItemPosition()));
        }
        getTargetFragment().onActivityResult(EditShippingViewListener
                .LOCATION_FRAGMENT_REQUEST_CODE, Activity.RESULT_OK, intent);
    }

    @Override
    public void onSuccess() {
        setShopLocationId();
        setShopLocationName();
        dismiss();
    }

    private void setShopLocationId(){
        if(isJakarta()){
            shop.setShopProvinceId(listLocations.get(provinceSpinner.getSelectedItemPosition()).provinceId);
            shop.setShopDistrictId(JAKARTA_ORIGIN_ID);
        }else{
            shop.setShopProvinceId(listLocations.get(provinceSpinner.getSelectedItemPosition()).provinceId);
            shop.setShopCityId(listLocations.get(provinceSpinner.getSelectedItemPosition())
                    .cities.get(citySpinner.getSelectedItemPosition()).cityId);
            shop.setShopDistrictId(listLocations.get(provinceSpinner.getSelectedItemPosition())
                    .cities.get(citySpinner.getSelectedItemPosition())
                    .districts.get(districtSpinner.getSelectedItemPosition()).districtId);
        }
    }

    private void setShopLocationName(){
        if(isJakarta()){
            shop.setShopProvinceName(listLocations.get(provinceSpinner.getSelectedItemPosition()).provinceName);
            shop.setShopCityName("");
            shop.setShopDistrictName("");
        }else{
            shop.setShopProvinceName(listLocations.get(provinceSpinner.getSelectedItemPosition()).provinceName);
            shop.setShopCityName(listLocations.get(provinceSpinner.getSelectedItemPosition())
                    .cities.get(citySpinner.getSelectedItemPosition()).cityName);
            shop.setShopDistrictName(listLocations.get(provinceSpinner.getSelectedItemPosition())
                    .cities.get(citySpinner.getSelectedItemPosition())
                    .districts.get(districtSpinner.getSelectedItemPosition()).districtName);
        }
    }

    @Override
    public void onTimeout() {
        Toast.makeText(getActivity(),
                getString(R.string.title_try_again), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CURRENT_LOCATION_LIST,
                (ArrayList<? extends Parcelable>) listLocations);
        outState.putParcelable(CURRENT_SHOP_DATA, shop);
    }

    private boolean isJakarta(){
        return !citySpinner.isShown() && !districtSpinner.isShown();
    }
}
