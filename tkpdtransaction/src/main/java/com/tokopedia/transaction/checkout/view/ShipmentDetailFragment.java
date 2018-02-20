package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.DaggerShipmentDetailComponent;
import com.tokopedia.transaction.checkout.di.component.ShipmentDetailComponent;
import com.tokopedia.transaction.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentDetailPresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;
import com.tokopedia.transaction.insurance.view.InsuranceTnCActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailFragment extends BasePresenterFragment<IShipmentDetailPresenter>
        implements IShipmentDetailView, CourierChoiceAdapter.ViewListener, OnMapReadyCallback,
        ShipmentChoiceBottomSheet.ActionListener {

    private static final int REQUEST_CODE_SHIPMENT_CHOICE = 11;
    private static final int REQUEST_CODE_PINPOINT = 22;
    private static final int DELAY_IN_MILISECOND = 500;
    private static final String EXTRA_SELECTED_COURIER = "selectedCourier";

    @BindView(R2.id.scroll_view_content)
    ScrollView scrollViewContent;
    @BindView(R2.id.ll_network_error_view)
    LinearLayout llNetworkErrorView;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.img_bt_close_ticker)
    ImageButton imgBtCloseTicker;
    @BindView(R2.id.ll_shipment_info_ticker)
    LinearLayout llShipmentInfoTicker;
    @BindView(R2.id.tv_shipment_info_ticker)
    TextView tvShipmentInfoTicker;
    @BindView(R2.id.ll_shipment_choice)
    LinearLayout llShipmentChoice;
    @BindView(R2.id.tv_shipment_type)
    TextView tvShipmentType;
    @BindView(R2.id.rv_courier_choice)
    RecyclerView rvCourierChoice;
    @BindView(R2.id.ll_pinpoint)
    LinearLayout llPinpoint;
    @BindView(R2.id.map_view_pinpoint)
    MapView mapViewPinpoint;
    @BindView(R2.id.bt_change_pinpoint)
    Button btChangePinpoint;
    @BindView(R2.id.tv_no_ponpoint_information)
    TextView tvNoPonpointInformation;
    @BindView(R2.id.bt_choose_pinpoint)
    Button btChoosePinpoint;
    @BindView(R2.id.fl_pinpoint_map)
    FrameLayout flPinpointMap;
    @BindView(R2.id.tv_shipment_address)
    TextView tvShipmentAddress;
    @BindView(R2.id.tv_label_insurance)
    TextView tvLabelInsurance;
    @BindView(R2.id.img_bt_insurance_info)
    ImageButton imgBtInsuranceInfo;
    @BindView(R2.id.switch_insurance)
    Switch switchInsurance;
    @BindView(R2.id.tv_special_insurance_condition)
    TextView tvSpecialInsuranceCondition;
    @BindView(R2.id.ll_insurance)
    LinearLayout llInsurance;
    @BindView(R2.id.separator_insurance)
    View separatorInsurance;
    @BindView(R2.id.img_bt_partly_accept_info)
    ImageButton imgBtPartlyAcceptInfo;
    @BindView(R2.id.switch_partly_accept)
    Switch switchPartlyAccept;
    @BindView(R2.id.ll_partial_order)
    LinearLayout llPartialOrder;
    @BindView(R2.id.separator_partial_order)
    View separatorPartialOrder;
    @BindView(R2.id.img_bt_dropshipper_info)
    ImageButton imgBtDropshipperInfo;
    @BindView(R2.id.switch_dropshipper)
    Switch switchDropshipper;
    @BindView(R2.id.ll_dropshipper)
    LinearLayout llDropshipper;
    @BindView(R2.id.et_shipper_name)
    EditText etShipperName;
    @BindView(R2.id.text_input_layout_shipper_name)
    TextInputLayout textInputLayoutShipperName;
    @BindView(R2.id.et_shipper_phone)
    EditText etShipperPhone;
    @BindView(R2.id.text_input_layout_shipper_phone)
    TextInputLayout textInputLayoutShipperPhone;
    @BindView(R2.id.ll_dropshipper_info)
    LinearLayout llDropshipperInfo;
    @BindView(R2.id.tv_delivery_fee)
    TextView tvDeliveryFee;
    @BindView(R2.id.bt_save)
    Button btSave;
    @BindView(R2.id.ll_fees_group)
    LinearLayout llFeesGroup;
    @BindView(R2.id.ll_insurance_fee)
    LinearLayout llInsuranceFee;
    @BindView(R2.id.ll_additional_fee)
    LinearLayout llAdditionalFee;
    @BindView(R2.id.tv_delivery_fee_total)
    TextView tvDeliveryFeeTotal;
    @BindView(R2.id.tv_additional_fee)
    TextView tvAdditionalFee;
    @BindView(R2.id.v_no_pinpoint_layer)
    View vNoPinpointLayer;
    @BindView(R2.id.ll_shipment_address)
    LinearLayout llShipmentAddress;
    @BindView(R2.id.tv_insurance_terms)
    TextView tvInsuranceTerms;
    @BindView(R2.id.tv_insurance_price)
    TextView tvInsurancePrice;

    private ShipmentChoiceBottomSheet shipmentChoiceBottomSheet;

    @Inject
    CourierChoiceAdapter courierChoiceAdapter;

    @Inject
    IShipmentDetailPresenter presenter;

    public static ShipmentDetailFragment newInstance() {
        ShipmentDetailFragment fragment = new ShipmentDetailFragment();
        Bundle bundle = new Bundle();
        // Todo : Add bundle if any
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_detail;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(view);
        initializeInjector();
        presenter.attachView(this);
        courierChoiceAdapter.setViewListener(this);
        courierChoiceAdapter.setCouriers(presenter.getCouriers());
        presenter.loadShipmentData();
    }

    private void initializeInjector() {
        ShipmentDetailComponent shipmentDetailComponent = DaggerShipmentDetailComponent.builder()
                .build();
        shipmentDetailComponent.inject(this);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoading() {
        scrollViewContent.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
        scrollViewContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnection(@NonNull String message) {
        scrollViewContent.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.loadShipmentData();
                    }
                });
    }

    @Override
    public void showPinPointMap(ShipmentDetailData shipmentDetailData) {
        setText(tvShipmentAddress, shipmentDetailData.getAddress());
        setupMapView();
        if (shipmentDetailData.getDestinationLatitude() == null ||
                shipmentDetailData.getDestinationLongitude() == null) {
            renderNoPinpoint();
        } else {
            renderPinpoint();
        }
        llPinpoint.setVisibility(View.VISIBLE);
    }

    private void showErrorSnackbar(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    private void initializeShipmentChoiceHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showShipmentChoiceBottomSheet();
            }
        }, DELAY_IN_MILISECOND);
    }

    private void showShipmentChoiceBottomSheet() {
        if (shipmentChoiceBottomSheet == null) {
            shipmentChoiceBottomSheet = new ShipmentChoiceBottomSheet(
                    getActivity(),
                    presenter.getShipmentDetailData(),
                    presenter.getSelectedShipment());
        }
        shipmentChoiceBottomSheet.setListener(this);
        shipmentChoiceBottomSheet.show();
    }

    private void renderShipment(ShipmentDetailData shipmentDetailData) {
        if (presenter.getSelectedShipment() == null) {
            presenter.setSelectedShipment(shipmentDetailData.getShipmentItemData().get(0));
        }
        tvShipmentType.setText(presenter.getSelectedShipment().getType());
        llDropshipper.setVisibility(View.GONE);
        separatorPartialOrder.setVisibility(View.GONE);
        setText(tvDeliveryFeeTotal, shipmentDetailData.getDeliveryPriceTotal());
    }

    @Override
    public void renderShipmentWithMap(ShipmentDetailData shipmentDetailData) {
        showPinPointMap(shipmentDetailData);
        renderShipment(shipmentDetailData);
    }

    @Override
    public void renderShipmentWithoutMap(ShipmentDetailData shipmentDetailData) {
        llPinpoint.setVisibility(View.GONE);
        renderShipment(shipmentDetailData);
    }

    @Override
    public void renderFirstLoadedRatesData(ShipmentDetailData shipmentDetailData) {
        initializeShipmentChoiceHandler();
        renderShipmentWithoutMap(shipmentDetailData);
    }

    @Override
    public void showPinPointChooserMap(ShipmentDetailData shipmentDetailData) {
        Intent intent = GeolocationActivity.createInstance(context, null);
        startActivityForResult(intent, REQUEST_CODE_PINPOINT);
    }

    @Override
    public void showAllCouriers() {
        setupRecyclerView();
    }

    private void formatInsuranceTncView() {
        String formatText = getString(R.string.text_tos_agreement);
        String messageTosAgreement = getString(R.string.message_tos_agreement);
        int startSpan = messageTosAgreement.indexOf(formatText);
        int endSpan = messageTosAgreement.indexOf(formatText) + formatText.length();
        Spannable tosAgreementText = new SpannableString(messageTosAgreement);
        int color = ContextCompat.getColor(context, R.color.tkpd_green_header);
        tosAgreementText.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tosAgreementText.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tosAgreementText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                getActivity().startActivity(new Intent(getActivity(), InsuranceTnCActivity.class));
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInsuranceTerms.setMovementMethod(LinkMovementMethod.getInstance());
        tvInsuranceTerms.setText(tosAgreementText);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvCourierChoice.setLayoutManager(linearLayoutManager);
        rvCourierChoice.setAdapter(courierChoiceAdapter);
    }

    private void renderNoPinpoint() {
        llShipmentAddress.setVisibility(View.GONE);
        btChangePinpoint.setVisibility(View.GONE);
        btChoosePinpoint.setVisibility(View.VISIBLE);
        tvNoPonpointInformation.setVisibility(View.VISIBLE);
        vNoPinpointLayer.setVisibility(View.VISIBLE);
        flPinpointMap.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background_no_pinpoint));
    }

    private void renderPinpoint() {
        llShipmentAddress.setVisibility(View.VISIBLE);
        btChoosePinpoint.setVisibility(View.GONE);
        tvNoPonpointInformation.setVisibility(View.GONE);
        btChangePinpoint.setVisibility(View.VISIBLE);
        vNoPinpointLayer.setVisibility(View.GONE);
        flPinpointMap.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
    }

    private void setupMapView() {
        if (mapViewPinpoint != null) {
            mapViewPinpoint.onCreate(null);
            mapViewPinpoint.onResume();
            mapViewPinpoint.getMapAsync(this);
        }
    }

    private void setGoogleMap(GoogleMap googleMap, ShipmentDetailData shipmentDetailData) {
        if (googleMap != null) {
            LatLng latLng;
            if (shipmentDetailData.getDestinationLatitude() == null ||
                    shipmentDetailData.getDestinationLongitude() == null) {
                latLng = new LatLng(-6.1754, 106.8272);
            } else {
                latLng = new LatLng(shipmentDetailData.getDestinationLatitude(),
                        shipmentDetailData.getDestinationLongitude());
            }

            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pointer_toped))
            ).setDraggable(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // need this even it's not used
                    // it's used to override default function of OnMapClickListener
                    // which is navigate to default Google Map Apps
                }
            });
        }
    }

    private void updateFeesGroupLayout() {
        if (llInsuranceFee.getVisibility() == View.GONE && llAdditionalFee.getVisibility() == View.GONE) {
            llFeesGroup.setVisibility(View.GONE);
        } else {
            llFeesGroup.setVisibility(View.VISIBLE);
        }
    }

    private void setupPinPointMap() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            presenter.getPinPointMapData();
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }

    private void setText(TextView textView, String text) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText("-");
        }
    }

    private void showBottomSheet(String title, String message, int image) {
        BottomSheetView bottomSheetView = new BottomSheetView(getActivity());
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(message)
                .setImg(image)
                .build());

        bottomSheetView.show();
    }

    private void renderTickerView(CourierItemData courierItemData) {
        if (!TextUtils.isEmpty(courierItemData.getCourierInfo())) {
            tvShipmentInfoTicker.setText(courierItemData.getCourierInfo());
            llShipmentInfoTicker.setVisibility(View.VISIBLE);
        } else {
            llShipmentInfoTicker.setVisibility(View.GONE);
        }
    }

    private void renderAdditionalPriceView(CourierItemData courierItemData) {
        if (courierItemData.getAdditionalPrice() != null) {
            llAdditionalFee.setVisibility(View.VISIBLE);
            setText(tvAdditionalFee, courierItemData.getAdditionalPrice());
        } else {
            llAdditionalFee.setVisibility(View.GONE);
        }
    }

    private void renderDropshipperView(CourierItemData courierItemData) {
        if (courierItemData.isAllowDropshiper()) {
            llDropshipper.setVisibility(View.VISIBLE);
            separatorPartialOrder.setVisibility(View.VISIBLE);
        } else {
            llDropshipper.setVisibility(View.GONE);
            separatorPartialOrder.setVisibility(View.GONE);
            llDropshipperInfo.setVisibility(View.GONE);
        }
    }

    private void renderInsuranceView(CourierItemData courierItemData) {
        imgBtInsuranceInfo.setVisibility(View.VISIBLE);
        if (courierItemData.getInsuranceType() == InsuranceConstant.InsuranceType.NO) {
            tvSpecialInsuranceCondition.setText(R.string.label_insurance_not_available);
            switchInsurance.setVisibility(View.GONE);
            switchInsurance.setChecked(false);
            tvSpecialInsuranceCondition.setVisibility(View.VISIBLE);
        } else if (courierItemData.getInsuranceType() == InsuranceConstant.InsuranceType.MUST) {
            tvSpecialInsuranceCondition.setText(R.string.label_must_insurance);
            switchInsurance.setVisibility(View.GONE);
            switchInsurance.setChecked(true);
            tvSpecialInsuranceCondition.setVisibility(View.VISIBLE);
        } else {
            tvSpecialInsuranceCondition.setVisibility(View.GONE);
            switchInsurance.setVisibility(View.VISIBLE);
            if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.InsuranceUsedDefault.YES) {
                switchInsurance.setChecked(true);
            }
        }
    }

    private void renderInsuranceTncView(CourierItemData courierItemData) {
        if (courierItemData.getInsuranceUsedType() == InsuranceConstant.InsuranceUsedType.TOKOPEDIA_INSURANCE) {
            formatInsuranceTncView();
            tvInsuranceTerms.setVisibility(View.VISIBLE);
        } else {
            tvInsuranceTerms.setVisibility(View.GONE);
        }
    }

    private void resetView() {
        switchInsurance.setChecked(false);
        switchDropshipper.setChecked(false);
        switchPartlyAccept.setChecked(false);
        switchInsurance.setVisibility(View.GONE);
        tvSpecialInsuranceCondition.setVisibility(View.VISIBLE);
        tvSpecialInsuranceCondition.setText(R.string.label_insurance_not_available);
        llFeesGroup.setVisibility(View.GONE);
        llPinpoint.setVisibility(View.GONE);
    }

    @OnClick(R2.id.bt_choose_pinpoint)
    void onChoosePinPoint() {
        setupPinPointMap();
    }

    @OnClick(R2.id.bt_change_pinpoint)
    void onChangePinPointClick() {
        setupPinPointMap();
    }

    @OnClick(R2.id.img_bt_insurance_info)
    void onInsuranceInfoClick() {
        showBottomSheet(getString(R.string.title_bottomsheet_insurance),
                presenter.getSelectedCourier().getInsuranceUsedInfo(), R.drawable.ic_insurance);
    }

    @OnClick(R2.id.img_bt_partly_accept_info)
    void onPartlyAcceptInfoClick() {
        showBottomSheet(getString(R.string.label_accept_partial_order_new),
                presenter.getShipmentDetailData().getPartialOrderInfo(), R.drawable.ic_partial_order);
    }

    @OnClick(R2.id.img_bt_dropshipper_info)
    void onDropshipperInfoClick() {
        showBottomSheet(getString(R.string.label_dropshipper_new),
                presenter.getShipmentDetailData().getDropshipperInfo(), R.drawable.ic_dropshipper);
    }

    @OnClick(R2.id.img_bt_close_ticker)
    void onCloseTickerClick() {
        llShipmentInfoTicker.setVisibility(View.GONE);
    }

    @OnClick(R2.id.ll_shipment_choice)
    void onShipmentChoiceClick() {
        showShipmentChoiceBottomSheet();
    }

    @OnClick(R2.id.bt_save)
    void onSaveClick() {
        Intent intentResult = new Intent();
        intentResult.putExtra(EXTRA_SELECTED_COURIER, presenter.getSelectedCourier());
        getActivity().setResult(Activity.RESULT_OK, intentResult);
        getActivity().finish();
    }

    @OnCheckedChanged(R2.id.switch_insurance)
    void onSwitchInsuranceChanged(CompoundButton view, boolean checked) {
        if (checked) {
            llInsuranceFee.setVisibility(View.VISIBLE);
            if (presenter.getSelectedCourier().getInsuranceType() == InsuranceConstant.InsuranceType.MUST ||
                    presenter.getSelectedCourier().getInsuranceType() == InsuranceConstant.InsuranceType.OPTIONAL) {
                renderInsuranceTncView(presenter.getSelectedCourier());
                tvInsurancePrice.setText(String.valueOf(presenter.getSelectedCourier().getInsurancePrice()));
            }
        } else {
            llInsuranceFee.setVisibility(View.GONE);
            tvInsuranceTerms.setVisibility(View.GONE);
        }
        updateFeesGroupLayout();
    }

    @OnCheckedChanged(R2.id.switch_partly_accept)
    void onSwitchPartlyAcceptChanged(CompoundButton view, boolean checked) {

    }

    @OnCheckedChanged(R2.id.switch_dropshipper)
    void onSwitchDropshipperChanged(CompoundButton view, boolean checked) {
        if (checked) {
            llDropshipperInfo.setVisibility(View.VISIBLE);
        } else {
            llDropshipperInfo.setVisibility(View.GONE);
        }
        updateFeesGroupLayout();
    }

    @Override
    public void onCourierItemClick(CourierItemData courierItemData) {
        if (presenter.getSelectedCourier() == null ||
                presenter.getSelectedCourier().getShipperProductId() !=
                        courierItemData.getShipperProductId()) {
            resetView();
            presenter.setSelectedCourier(courierItemData);
            if (courierItemData.isUsePinPoint()) {
                renderShipmentWithMap(presenter.getShipmentDetailData());
            } else {
                renderShipmentWithoutMap(presenter.getShipmentDetailData());
            }
            switchInsurance.setChecked(false);
            setText(tvDeliveryFee, courierItemData.getDeliveryPrice());
            renderTickerView(courierItemData);
            renderInsuranceView(courierItemData);
            renderAdditionalPriceView(courierItemData);
            renderDropshipperView(courierItemData);
            updateFeesGroupLayout();
        }
    }

    @Override
    public void onShipmentItemClick(ShipmentItemData shipmentItemData) {
        if (presenter.getSelectedShipment() == null ||
                presenter.getSelectedShipment().getServiceId() != shipmentItemData.getServiceId()) {
            resetView();
            presenter.setSelectedShipment(shipmentItemData);
            tvShipmentType.setText(shipmentItemData.getType());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setGoogleMap(googleMap, presenter.getShipmentDetailData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PINPOINT:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        LocationPass locationPass =
                                bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                        presenter.updatePinPoint(locationPass);
                    }
                    break;

                case REQUEST_CODE_SHIPMENT_CHOICE:
                    break;
            }
        }
    }

}
