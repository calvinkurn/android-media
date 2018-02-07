package com.tokopedia.transaction.cart.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.adapter.ShipmentCartAdapter;
import com.tokopedia.transaction.cart.adapter.ShipmentPackageCartAdapter;
import com.tokopedia.transaction.cart.listener.IShipmentCartView;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.calculateshipment.ShipmentPackage;
import com.tokopedia.transaction.cart.model.cartdata.CartDestination;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;
import com.tokopedia.transaction.cart.presenter.IShipmentCartPresenter;
import com.tokopedia.transaction.cart.presenter.ShipmentCartPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity.RESULT_NOT_SELECTED_DESTINATION;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         modified by alvarisi
 */

public class ShipmentCartFragment extends BasePresenterFragment<IShipmentCartPresenter>
        implements IShipmentCartView {
    public static final int CHOOSE_ADDRESS = 10;
    private static final int CHOOSE_LOCATION = 12;
    private static final String ARG_PARAM_EXTRA_TRANSACTION_DATA =
            "ARG_PARAM_EXTRA_TRANSACTION_DATA";

    @BindView(R2.id.tv_title_address)
    TextView tvTitleAddress;
    @BindView(R2.id.tv_detail_address)
    TextView tvDetailAddress;
    @BindView(R2.id.btn_choose_address)
    TextView btnChooseAddress;
    @BindView(R2.id.btn_add_address)
    TextView btnAddAddress;
    @BindView(R2.id.cv_address)
    CardView cvAddress;
    @BindView(R2.id.sp_shipment)
    AppCompatSpinner spShipment;
    @BindView(R2.id.sp_shipment_package)
    AppCompatSpinner spShipmentPackage;
    @BindView(R2.id.cv_shipment)
    CardView cvShipment;
    @BindView(R2.id.iv_icon_geo_location)
    ImageView ivIconGeoLocation;
    @BindView(R2.id.tv_value_location)
    AppCompatTextView tvValueLocation;
    @BindView(R2.id.tv_error_geo_location)
    AppCompatTextView tvErrorGeoLocation;
    @BindView(R2.id.layout_value_location)
    RelativeLayout layoutValueLocation;
    @BindView(R2.id.cv_geo_location)
    CardView cvGeoLocation;
    @BindView(R2.id.pb_price)
    ProgressBar pbPrice;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.holder_price)
    RelativeLayout holderPrice;
    @BindView(R2.id.cb_prices)
    CardView cbPrices;
    @BindView(R2.id.btn_save)
    AppCompatTextView btnSave;
    @BindView(R2.id.container)
    LinearLayout holderContainer;

    private TkpdProgressDialog progressdialog;
    private List<Shipment> shipmentData;
    private CartItem transactionPassData;
    private ShipmentCartAdapter adapterShipment;
    private ShipmentPackageCartAdapter adapterShipmentPackage;
    private ShipmentCartWrapper wrapper;
    private LocationPass locationPass;

    public static ShipmentCartFragment newInstance(CartItem passData) {
        ShipmentCartFragment fragment = new ShipmentCartFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_EXTRA_TRANSACTION_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        actionCalculateShipment();
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
        transactionPassData = arguments.getParcelable(ARG_PARAM_EXTRA_TRANSACTION_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_cart_tx_module;
    }

    @Override
    protected void initView(View view) {
        spShipment.setAdapter(adapterShipment);
        spShipmentPackage.setAdapter(adapterShipmentPackage);
    }

    @Override
    protected void setViewListener() {
        cvGeoLocation.setVisibility(View.GONE);
        tvTitleAddress.setText(
                MethodChecker.fromHtml(transactionPassData.getCartDestination().getAddressName())
        );
        tvDetailAddress.setText(renderDetailAddressFromTransaction(
                transactionPassData.getCartDestination())
        );
        spShipment.setOnItemSelectedListener(getShipmentItemSelectionListener());
        spShipmentPackage.setOnItemSelectedListener(getShipmentPackageItemSelectionListener());
        layoutValueLocation.setOnClickListener(getChangeLocationListener());
        tvValueLocation.setOnClickListener(getChangeLocationListener());
    }

    private View.OnClickListener getChangeLocationListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToGeolocation(locationPass);
            }
        };
    }

    private AdapterView.OnItemSelectedListener getShipmentItemSelectionListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Shipment shipment = (Shipment) adapterView.getSelectedItem();
                wrapper.setShipmentId(shipment.getShipmentId());
                renderShipmentPackageSpinner(shipment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener getShipmentPackageItemSelectionListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ShipmentPackage shipmentPackage = (ShipmentPackage) adapterView.getSelectedItem();

                wrapper.setShipmentPackageId(
                        shipmentPackage.getShipmentPackageId() != null ?
                                shipmentPackage.getShipmentPackageId() : String.valueOf(0)
                );

                tvPrice.setText(shipmentPackage.getPrice());
                showGeolocationMap(shipmentPackage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private String renderDetailAddressFromTransaction(CartDestination destination) {
        return String.format("%s\n%s\n%s, %s, %s\n%s\n%s",
                MethodChecker.fromHtml(destination.getReceiverName()),
                MethodChecker.fromHtml(destination.getAddressStreet()).toString(),
                destination.getAddressDistrict(),
                destination.getAddressCity(),
                destination.getAddressPostal(),
                destination.getAddressProvince(),
                destination.getReceiverPhone()
        );
    }

    private void renderShipmentPackageSpinner(Shipment shipment) {
        List<ShipmentPackage> shipmentPackages = new ArrayList<>();
        for (ShipmentPackage shipmentPackage : shipment.getShipmentPackage()) {
            if (shipmentPackage.getPackageAvailable() != 0) {
                shipmentPackages.add(shipmentPackage);
            }
        }
        if (shipmentPackages.size() > 0) {
            adapterShipmentPackage.setAdapterData(
                    (ArrayList<ShipmentPackage>) shipmentPackages
            );
            int selectedPosition = 0;
            for (int i = 0; i < shipmentPackages.size(); i++) {
                if (wrapper.getShipmentId().equalsIgnoreCase(shipment.getShipmentId())
                        && wrapper.getShipmentPackageId().equalsIgnoreCase(
                        shipmentPackages.get(i).getShipmentPackageId()
                )) {
                    selectedPosition = i;
                    break;
                }
            }
            spShipmentPackage.setAdapter(adapterShipmentPackage);
            spShipmentPackage.setSelection(selectedPosition);
        } else {
            renderEmptyShipmentPackageSpinner();
        }

    }

    @Override
    protected void initialVar() {
        locationPass = new LocationPass();
        locationPass.setLatitude(transactionPassData.getCartDestination().getLatitude());
        locationPass.setLongitude(transactionPassData.getCartDestination().getLongitude());
        shipmentData = new ArrayList<>();
        adapterShipment = ShipmentCartAdapter.newInstance(getActivity());
        adapterShipmentPackage = ShipmentPackageCartAdapter.newInstance(getActivity());
        wrapper = new ShipmentCartWrapper();
        wrapper.setOldAddressId(transactionPassData.getCartDestination().getAddressId());
        wrapper.setOldShipmentId(transactionPassData.getCartShipments().getShipmentId());
        wrapper.setOldShipmentPackageId(
                transactionPassData.getCartShipments().getShipmentPackageId()
        );
        wrapper.setAddressId(transactionPassData.getCartDestination().getAddressId());
        wrapper.setShipmentId(transactionPassData.getCartShipments().getShipmentId());
        wrapper.setShipmentPackageId(transactionPassData.getCartShipments().getShipmentPackageId());
        wrapper.setShopId(transactionPassData.getCartShop().getShopId());
        progressdialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {
        fetchGeocodeLocation();
    }

    private void fetchGeocodeLocation() {
        presenter.processGeoCodeLocation(locationPass);
    }

    private void actionCalculateShipment() {
        CalculateShipmentWrapper calculateShipmentWrapper = new CalculateShipmentWrapper();
        calculateShipmentWrapper.setShopId(wrapper.getShopId());
        calculateShipmentWrapper.setAddressId(wrapper.getAddressId());
        calculateShipmentWrapper.setWeight(transactionPassData.getCartTotalWeight());
        calculateShipmentWrapper.setShippingId(transactionPassData.getCartShipments().getShipmentId());
        presenter.processCalculateShipment(calculateShipmentWrapper);
    }

    @Override
    public void renderCalculateShipment(@NonNull List<Shipment> data) {
        shipmentData = data;
        renderSpinnerShipment();
    }

    @Override
    public void renderSpinnerShipment() {
        ArrayList<Shipment> shipments = new ArrayList<>();
        for (Shipment shipment : shipmentData) {
            if (shipment.getShipmentAvailable() != 0) {
                shipments.add(shipment);
            }
        }

        if (shipments.size() > 0) {
            adapterShipment.setAdapterData(shipments);
            spShipment.setAdapter(adapterShipment);
            int selectedPosition = 0;
            for (int i = 0; i < shipments.size(); i++) {
                if (wrapper.getShipmentId().equalsIgnoreCase(shipments.get(i).getShipmentId())) {
                    selectedPosition = i;
                    break;
                }
            }
            spShipment.setSelection(selectedPosition);

            renderShipmentPackageSpinner(shipments.get(selectedPosition));
        } else {
            renderEmptyShipment();
            renderEmptyShipmentPackageSpinner();
        }
    }

    @Override
    public void renderGeocodeLocation(String location) {
        tvValueLocation.setText(location);
    }

    @Override
    public void renderErrorCalculateShipment(String error) {
        showRetry(getCalculateShipmentRetryListener(), error);
    }

    private NetworkErrorHelper.RetryClickedListener getCalculateShipmentRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                actionCalculateShipment();
            }
        };
    }

    @Override
    public void renderErrorEditShipment(String error) {
        showRetry(getEditShipmentRetryListener(), error);
    }

    private NetworkErrorHelper.RetryClickedListener getEditShipmentRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processEditShipmentCart(wrapper);
            }
        };
    }

    private void showRetry(NetworkErrorHelper.RetryClickedListener listener, String error) {
        View view = getView();
        NetworkErrorHelper.showEmptyState(getActivity(),
                view,
                error,
                listener);
    }

    @Override
    public void renderErrorEditLocationShipment(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public boolean isLoading() {
        return progressdialog.isProgress();
    }

    @Override
    public void showLoading() {
        progressdialog.showDialog();
    }

    @Override
    public void dismisLoading() {
        progressdialog.dismiss();
    }

    @Override
    public void showGeolocationMap(ShipmentPackage shipmentPackage) {
        if (shipmentPackage.getShowMap() == 1) {
            cvGeoLocation.setVisibility(View.VISIBLE);
        } else {
            cvGeoLocation.setVisibility(View.GONE);
        }
    }


    @Override
    @OnClick(R2.id.btn_choose_address)
    public void navigateToChooseAddress() {
        Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChooseAddressActivity.REQUEST_CODE_PARAM_ADDRESS,
                transactionPassData.getCartDestination().getAddressId()
        );
        intent.putExtras(bundle);
        startActivityForResult(intent, ShipmentCartFragment.CHOOSE_ADDRESS);
    }

    @Override
    @OnClick(R2.id.btn_add_address)
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
    public void navigateToGeolocation(LocationPass locationPass) {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {
            LocationPass locationArg = null;
            if (!(TextUtils.isEmpty(locationPass.getLongitude())
                    || TextUtils.isEmpty(locationPass.getLatitude()))) {
                locationArg = locationPass;
            }
            Intent intent = GeolocationActivity.createInstance(getActivity(), locationArg);
            startActivityForResult(intent, CHOOSE_LOCATION);
        } else {
            Toast.makeText(
                    getActivity(), "Google Play Service Unavailable", Toast.LENGTH_LONG
            ).show();
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    @Override
    public ShipmentCartAdapter getShipmentAdapter() {
        return adapterShipment;
    }

    @Override
    public ShipmentPackageCartAdapter getShipmentPackageAdapter() {
        return adapterShipmentPackage;
    }

    @Override
    public void renderEmptyShipment() {
        spShipment.setAdapter(getEmptyShipmentAdapter());
        wrapper.setShipmentId(String.valueOf(0));
    }

    @Override
    public void renderEmptyShipmentPackageSpinner() {
        spShipmentPackage.setAdapter(getEmptyShipmentPackageAdapter());
        wrapper.setShipmentPackageId(String.valueOf(0));
    }

    @OnClick(R2.id.btn_save)
    public void actionSaveShipment() {
        if (validateSaveForm()) return;
        presenter.processEditShipmentCart(wrapper);
    }

    private boolean validateSaveForm() {
        tvErrorGeoLocation.setVisibility(View.GONE);
        ShipmentPackage shipmentPackage = (ShipmentPackage) spShipmentPackage.getSelectedItem();
        if (shipmentPackage.getShowMap() == 1 && (
                locationPass.getLatitude().equalsIgnoreCase("") ||
                        locationPass.getLongitude().equalsIgnoreCase(""))) {
            tvErrorGeoLocation.setVisibility(View.VISIBLE);
            showSnackbar(getString(com.tokopedia.transaction.R.string.shipment_data_not_complete));
            return true;
        } else if (shipmentData == null || shipmentData.size() == 0) {
            showSnackbar(getString(com.tokopedia.transaction.R.string.courier_not_available));
            return true;
        }
        return false;
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                message,
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void renderErrorEditLocationShipmentNoConnection() {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_no_connection_initial_cart_data),
                getString(R.string.label_transaction_error_message_try_again),
                getString(R.string.label_title_button_retry), 0,
                getEditShipmentRetryListener()
        );
    }

    @Override
    public void renderErrorEditShipmentTimeout() {

    }

    @Override
    public void renderEditShipmentErrorSnackbar(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_ADDRESS:
                    renderResultChangeAddress(data.getExtras());
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    renderResultChangeAddress(data.getExtras());
                    break;
                case CHOOSE_LOCATION:
                    renderResultChooseLocation(data.getExtras());
                    break;
            }
        }
        if (resultCode == RESULT_NOT_SELECTED_DESTINATION && data != null) {
            renderResultChangeAddress(data.getExtras());
        }
    }

    @Override
    public void renderResultChangeAddress(@NonNull Bundle bundle) {
        Destination temp = bundle.getParcelable(ManageAddressConstant.EXTRA_ADDRESS);
        assert temp != null;
        locationPass.setLatitude(temp.getLatitude());
        locationPass.setLongitude(temp.getLongitude());
        tvTitleAddress.setText(MethodChecker.fromHtml(temp.getAddressName()));
        tvDetailAddress.setText(MethodChecker.fromHtml(temp.getAddressDetail()));
        wrapper.setAddressId(temp.getAddressId());
        actionCalculateShipment();
        fetchGeocodeLocation();
    }

    @Override
    public void renderResultChooseLocation(@NonNull Bundle bundle) {
        LocationPass locationResult = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
        if (locationResult != null) {
            this.locationPass = locationResult;
            if (locationResult.getGeneratedAddress().equals(getString(R.string.choose_this_location))) {
                this.locationPass.setGeneratedAddress(String.format("%s, %s", locationPass.getLatitude(),
                        locationPass.getLongitude())
                );
            } else {
                this.locationPass.setGeneratedAddress(locationResult.getGeneratedAddress());
            }
            tvValueLocation.setText(this.locationPass.getGeneratedAddress());
        }
        actionSaveLocationShipment();
    }

    private void actionSaveLocationShipment() {
        SaveLocationWrapper location = new SaveLocationWrapper();
        location.setLatitude(locationPass.getLatitude());
        location.setLongitude(locationPass.getLongitude());
        location.setAddressId(wrapper.getAddressId());
        presenter.processSaveLocationShipment(location);
    }

    private ArrayAdapter<Shipment> getEmptyShipmentAdapter() {
        ArrayAdapter<Shipment> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                new Shipment[]{Shipment.createSelectionInfo(
                        getString(com.tokopedia.transaction.R.string.shipment_no_courier_available))}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<ShipmentPackage> getEmptyShipmentPackageAdapter() {
        ArrayAdapter<ShipmentPackage> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                new ShipmentPackage[]{ShipmentPackage.createSelectionInfo(
                        getString(com.tokopedia.transaction.R.string.shipment_no_courier_available))}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void renderEditLocationShipment(@NonNull String message) {
        actionCalculateShipment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}