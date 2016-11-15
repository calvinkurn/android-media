package com.tokopedia.transaction.cart.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.addtocart.model.responseatcform.Destination;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.transaction.cart.adapter.ShipmentCartAdapter;
import com.tokopedia.transaction.cart.adapter.ShipmentPackageCartAdapter;
import com.tokopedia.transaction.cart.listener.IShipmentCartView;
import com.tokopedia.transaction.cart.model.ShipmentCartPassData;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentData;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.calculateshipment.ShipmentPackage;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartData;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;
import com.tokopedia.transaction.cart.presenter.IShipmentCartPresenter;
import com.tokopedia.transaction.cart.presenter.ShipmentCartPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         modified by alvarisi
 */

public class ShipmentCartFragment extends BasePresenterFragment<IShipmentCartPresenter>
        implements IShipmentCartView {
    public static final int CHOOSE_ADDRESS = 0;
    public static final int CREATE_NEW_ADDRESS = 1;
    private static final int CHOOSE_LOCATION = 2;
    private static final String ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA =
            "ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA";

    @Bind(R2.id.tv_title_address)
    TextView tvTitleAddress;
    @Bind(R2.id.tv_detail_address)
    TextView tvDetailAddress;
    @Bind(R2.id.btn_choose_address)
    TextView btnChooseAddress;
    @Bind(R2.id.btn_add_address)
    TextView btnAddAddress;
    @Bind(R2.id.cv_address)
    CardView cvAddress;
    @Bind(R2.id.sp_shipment)
    AppCompatSpinner spShipment;
    @Bind(R2.id.sp_shipment_package)
    AppCompatSpinner spShipmentPackage;
    @Bind(R2.id.cv_shipment)
    CardView cvShipment;
    @Bind(R2.id.iv_icon_geo_location)
    ImageView ivIconGeoLocation;
    @Bind(R2.id.btn_change_value_location)
    ImageView btnChangeValueLocation;
    @Bind(R2.id.tv_value_location)
    AppCompatTextView tvValueLocation;
    @Bind(R2.id.layout_value_location)
    RelativeLayout layoutValueLocation;
    @Bind(R2.id.cv_geo_location)
    CardView cvGeoLocation;
    @Bind(R2.id.pb_price)
    ProgressBar pbPrice;
    @Bind(R2.id.tv_price)
    TextView tvPrice;
    @Bind(R2.id.holder_price)
    RelativeLayout holderPrice;
    @Bind(R2.id.cb_prices)
    CardView cbPrices;
    @Bind(R2.id.btn_save)
    AppCompatTextView btnSave;
    @Bind(R2.id.container)
    LinearLayout holderContainer;
    private ShipmentCartPassData shipmentCartPassData;
    private CalculateShipmentData shipmentData;
    private ShipmentCartAdapter adapterShipment;
    private ShipmentPackageCartAdapter adapterShipmentPackage;
    private ShipmentCartWrapper wrapper;
    private LocationPass locationPass;


    public static ShipmentCartFragment newInstance(ShipmentCartPassData passData) {
        ShipmentCartFragment fragment = new ShipmentCartFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        calculateShipment();
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
        presenter = new ShipmentCartPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        shipmentCartPassData = arguments.getParcelable(ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_cart;
    }

    @Override
    protected void initView(View view) {
        spShipment.setAdapter(adapterShipment);
        spShipmentPackage.setAdapter(adapterShipmentPackage);
    }

    @Override
    protected void setViewListener() {
        spShipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Shipment shipment = (Shipment) adapterView.getSelectedItem();
                wrapper.setShipmentId(shipment.getShipmentId());
                renderShipmentPackageSpinner(shipment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spShipmentPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ShipmentPackage shipmentPackage = (ShipmentPackage) adapterView.getSelectedItem();
                wrapper.setShipmentPackageId(shipmentPackage.getShipmentId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void renderShipmentPackageSpinner(Shipment shipment) {
        if (shipment.getShipmentPackage().size() > 0) {
            wrapper.setShipmentPackageId(shipment.getShipmentPackage().get(0).getShipmentId());
            adapterShipmentPackage.setAdapterData(
                    (ArrayList<ShipmentPackage>) shipment.getShipmentPackage()
            );
        } else {
            adapterShipmentPackage.setAdapterData(new ArrayList<ShipmentPackage>());
        }
    }

    @Override
    protected void initialVar() {
        locationPass = new LocationPass();
        locationPass.setLatitude(shipmentCartPassData.getLatitude());
        locationPass.setLongitude(shipmentCartPassData.getLongitude());
        shipmentData = new CalculateShipmentData();
        adapterShipment = ShipmentCartAdapter.newInstance(getActivity());
        adapterShipmentPackage = ShipmentPackageCartAdapter.newInstance(getActivity());
        wrapper = new ShipmentCartWrapper();
        wrapper.setOldAddressId(shipmentCartPassData.getAddressId());
        wrapper.setOldShipmentId(shipmentCartPassData.getShippingId());
        wrapper.setOldShipmentPackageId(shipmentCartPassData.getShippingPackageId());
        wrapper.setAddressId(shipmentCartPassData.getAddressId());
        wrapper.setShipmentId(shipmentCartPassData.getShippingId());
        wrapper.setShipmentPackageId(shipmentCartPassData.getShippingPackageId());
        wrapper.setShopId(shipmentCartPassData.getShopId());
    }

    @Override
    protected void setActionVar() {
        fetchGeocodeLocation();
    }

    private void fetchGeocodeLocation() {
        presenter.processGeoCodeLocation(locationPass);
    }

    private void calculateShipment() {
        CalculateShipmentWrapper calculateShipmentWrapper = new CalculateShipmentWrapper();
        calculateShipmentWrapper.setShopId(shipmentCartPassData.getShopId());
        calculateShipmentWrapper.setAddressId(shipmentCartPassData.getAddressId());
        calculateShipmentWrapper.setWeight(shipmentCartPassData.getWeight());
        presenter.processCalculateShipment(calculateShipmentWrapper);
    }

    @Override
    public void renderCalculateShipment(@NonNull CalculateShipmentData data) {
        shipmentData = data;
        adapterShipment.setAdapterData((ArrayList<Shipment>) data.getShipment());
        if (data.getShipment().size() > 0) {
            renderShipmentPackageSpinner(data.getShipment().get(0));
        } else {
            adapterShipmentPackage.setAdapterData(new ArrayList<ShipmentPackage>());
        }
    }

    @Override
    public void renderSpinnerShipment() {
        adapterShipment.notifyDataSetChanged();
        int selectedPosition = 0;
        List<Shipment> shipments = shipmentData.getShipment();
        for (int i = 0; i < shipments.size(); i++) {
            if (wrapper.getAddressId().equalsIgnoreCase(shipments.get(i).getShipmentId())) {
                selectedPosition = i;
            }
        }
        spShipment.setSelection(selectedPosition);
    }

    @Override
    public void renderCostShipment() {

    }

    @Override
    public void renderGeocodeLocation(String location) {
        tvValueLocation.setText(location);
    }

    @Override
    public void renderErrorCalculateShipment(String error) {

    }

    @Override
    public void renderErrorEditShipment(String error) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismisLoading() {

    }

    @Override
    public void renderResultChangeAddress(@NonNull Bundle bundle) {

    }

    @Override
    public void showGeolocationMap() {

    }

    @Override
    public void navigateToChooseAddress() {
        Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChooseAddressActivity.REQUEST_CODE_PARAM_ADDRESS, shipmentCartPassData.getAddressId());
        intent.putExtras(bundle);
        startActivityForResult(intent, ShipmentCartFragment.CHOOSE_ADDRESS);
    }

    @Override
    public void navigateToAddAddress() {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("page", 1);
        bundle.putBoolean(ManageAddressConstant.IS_EDIT, false);
        intent.putExtras(bundle);
        startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @Override
    public void navigateToCart(String message) {
        Bundle bundle = new Bundle();
        bundle.putString("response", message);
        Intent intent = getActivity().getIntent();
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public ShipmentCartAdapter getShipmentAdapter() {
        return adapterShipment;
    }

    @Override
    public ShipmentPackageCartAdapter getShipmentPackageAdapter() {
        return adapterShipmentPackage;
    }

    @OnClick(R2.id.btn_save)
    public void actionSaveShipment() {
        presenter.processEditShipmentCart(wrapper);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_ADDRESS:
                    onSuccessSelectAddress(data.getExtras());
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    onSuccessSelectAddress(data.getExtras());
                    break;
                case CHOOSE_LOCATION:
                    getNewLocation(data.getExtras());
                    break;
            }
        }
    }

    private void onSuccessSelectAddress(Bundle bundle) {
        Destination temp = bundle.getParcelable(ManageAddressConstant.EXTRA_ADDRESS);
        locationPass.setLatitude(temp.getLatitude());
        locationPass.setLongitude(temp.getLongitude());
        tvTitleAddress.setText(Html.fromHtml(temp.getAddressName()));
        tvDetailAddress.setText(Html.fromHtml(temp.getAddressDetail()));
        shipmentCartPassData.setAddressId(temp.getAddressId());
        calculateShipment();
        fetchGeocodeLocation();
    }

    private void getNewLocation(Bundle bundle) {
        LocationPass locationResult = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
        if (locationResult != null) {
            this.locationPass = locationResult;
            if (locationResult.getGeneratedAddress().equals(getString(R.string.choose_this_location))) {
                this.locationPass.setGeneratedAddress(locationPass.getLatitude() + ", " + locationPass.getLongitude());
            } else {
                this.locationPass.setGeneratedAddress(locationResult.getGeneratedAddress());
            }
            tvValueLocation.setText(this.locationPass.getGeneratedAddress());
        }
        SaveLocationWrapper location = new SaveLocationWrapper();
        location.setLatitude(locationPass.getLatitude());
        location.setLongitude(locationPass.getLongitude());
        location.setAddressId(wrapper.getAddressId());
        presenter.processSaveLocationShipment(location);
    }
}
