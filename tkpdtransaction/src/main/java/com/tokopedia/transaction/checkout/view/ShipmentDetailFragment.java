package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.tokopedia.transaction.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentDetailPresenter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentDetailPresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailFragment extends BasePresenterFragment implements IShipmentDetailView,
        CourierChoiceAdapter.ViewListener, OnMapReadyCallback, ShipmentChoiceBottomSheet.ActionListener {

    private static final int REQUEST_CODE_SHIPMENT_CHOICE = 11;
    private static final int REQUEST_CODE_PINPOINT = 22;
    private static final int DELAY_IN_MILISECOND = 500;

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
    @BindView(R2.id.ll_expanded_courier_list)
    LinearLayout llExpandedCourierList;
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
    @BindView(R2.id.tv_show_other_couriers)
    TextView tvShowOtherCouriers;
    @BindView(R2.id.iv_chevron)
    ImageView ivChevron;
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

    private ShipmentChoiceBottomSheet shipmentChoiceBottomSheet;
    private CourierChoiceAdapter courierChoiceAdapter;
    private IShipmentDetailPresenter presenter;

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
        initializeShipmentChoiceHandler();
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
        presenter = new ShipmentDetailPresenter();
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
        presenter.attachView(this);
        presenter.loadShipmentData();
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
    public void showData() {
        scrollViewContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPinPointMap(ShipmentDetailData shipmentDetailData) {
        setText(tvShipmentAddress, shipmentDetailData.getAddress());
        setupMapView();
        if (shipmentDetailData.getLatitude() == null || shipmentDetailData.getLongitude() == null) {
            renderNoPinpoint();
        } else {
            renderPinpoint();
        }
        llPinpoint.setVisibility(View.VISIBLE);
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
        presenter.setSelectedShipment(shipmentDetailData.getShipmentItemData().get(0));
        tvShipmentType.setText(presenter.getSelectedShipment().getType());
        llDropshipper.setVisibility(View.GONE);
        separatorPartialOrder.setVisibility(View.GONE);
        setText(tvDeliveryFeeTotal, shipmentDetailData.getDeliveryPriceTotal());
    }

    private void renderShipmentWithMap(ShipmentDetailData shipmentDetailData) {
        showPinPointMap(shipmentDetailData);
        renderShipment(shipmentDetailData);
        presenter.setCourierList(shipmentDetailData.getShipmentItemData().get(0).getCourierItemData());
    }

    private void renderShipmentWithoutMap(ShipmentDetailData shipmentDetailData) {
        llPinpoint.setVisibility(View.GONE);
        renderShipment(shipmentDetailData);
        presenter.setCourierList(shipmentDetailData.getShipmentItemData().get(0).getCourierItemData());
    }

    @Override
    public void renderInstantShipment(ShipmentDetailData shipmentDetailData) {
        renderShipmentWithMap(shipmentDetailData);
    }

    @Override
    public void renderSameDayShipment(ShipmentDetailData shipmentDetailData) {
        renderShipmentWithMap(shipmentDetailData);
    }

    @Override
    public void renderNextDayShipment(ShipmentDetailData shipmentDetailData) {
        renderShipmentWithMap(shipmentDetailData);
    }

    @Override
    public void renderRegularShipment(ShipmentDetailData shipmentDetailData) {
        renderShipmentWithoutMap(shipmentDetailData);
    }

    @Override
    public void renderKargoShipment(ShipmentDetailData shipmentDetailData) {
        renderShipmentWithoutMap(shipmentDetailData);
    }

    @Override
    public void showPinPointChooserMap(ShipmentDetailData shipmentDetailData) {
        Intent intent = GeolocationActivity.createInstance(context, null);
        startActivityForResult(intent, REQUEST_CODE_PINPOINT);
    }

    @Override
    public void showFirstThreeCouriers(List<CourierItemData> couriers) {
        tvShowOtherCouriers.setText(getString(R.string.label_show_other_courier));
        ivChevron.setImageResource(R.drawable.chevron_thin_down);
        setupRecyclerView(couriers);
    }

    @Override
    public void showAllCouriers(List<CourierItemData> couriers) {
        tvShowOtherCouriers.setText(R.string.label_hide_other_couriers);
        ivChevron.setImageResource(R.drawable.chevron_thin_up);
        setupRecyclerView(couriers);
    }

    private void setupRecyclerView(List<CourierItemData> couriers) {
        courierChoiceAdapter = new CourierChoiceAdapter(couriers, this);
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
            if (shipmentDetailData.getLatitude() == null || shipmentDetailData.getLongitude() == null) {
                latLng = new LatLng(-6.1754, 106.8272);
            } else {
                latLng = new LatLng(shipmentDetailData.getLatitude(), shipmentDetailData.getLongitude());
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

    private void showSnackbarError(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.black_70))
                .setAction(R.string.label_action_snackbar_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.red_50));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black_54));
        snackbar.show();
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
        if (courierItemData.getInsuranceType() == InsuranceConstant.InsuranceType.NO) {
            imgBtInsuranceInfo.setVisibility(View.GONE);
            tvSpecialInsuranceCondition.setText(R.string.label_insurance_not_available);
            switchInsurance.setVisibility(View.GONE);
            switchInsurance.setChecked(false);
            tvSpecialInsuranceCondition.setVisibility(View.VISIBLE);
        } else if (courierItemData.getInsuranceType() == InsuranceConstant.InsuranceType.MUST) {
            imgBtInsuranceInfo.setVisibility(View.VISIBLE);
            tvSpecialInsuranceCondition.setText(R.string.label_must_insurance);
            switchInsurance.setVisibility(View.GONE);
            switchInsurance.setChecked(true);
            tvSpecialInsuranceCondition.setVisibility(View.VISIBLE);
        } else {
            imgBtInsuranceInfo.setVisibility(View.VISIBLE);
            tvSpecialInsuranceCondition.setVisibility(View.GONE);
            switchInsurance.setVisibility(View.VISIBLE);
            if (courierItemData.getInsuranceUsedDefault() == InsuranceConstant.InsuranceUsedDefault.YES) {
                switchInsurance.setChecked(true);
            }
        }
    }

    @OnClick(R2.id.bt_choose_pinpoint)
    void onChoosePinPoint() {
        setupPinPointMap();
    }

    @OnClick(R2.id.bt_change_pinpoint)
    void onChangePinPointClick() {
        setupPinPointMap();
    }

    @OnClick(R2.id.ll_expanded_courier_list)
    void onExpandedCourierListClick() {
        if (courierChoiceAdapter.getItemCount() > 3) {
            presenter.loadFirstThreeCourier();
        } else {
            presenter.loadAllCourier();
        }
    }

    @OnClick(R2.id.img_bt_insurance_info)
    void onInsuranceInfoClick() {
        showBottomSheet(getString(R.string.title_bottomsheet_insurance),
                presenter.getSelectedCourier().getInsuranceUsedInfo(), R.drawable.ic_insurance);
    }

    @OnClick(R2.id.img_bt_partly_accept_info)
    void onPartlyAcceptInfoClick() {
        showBottomSheet(getString(R.string.label_accept_partial_order_new),
                presenter.getShipmentDetailData().getPartialOrderInfo(), R.drawable.ic_insurance);
    }

    @OnClick(R2.id.img_bt_dropshipper_info)
    void onDropshipperInfoClick() {
        showBottomSheet(getString(R.string.label_dropshipper_new),
                presenter.getShipmentDetailData().getDropshipperInfo(), R.drawable.ic_insurance);
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
        showSnackbarError(this.getView(), "Anda belum menandai lokasi tujuan dalam peta");
    }

    @OnCheckedChanged(R2.id.switch_insurance)
    void onSwitchInsuranceChanged(CompoundButton view, boolean checked) {
        if (checked) {
            llInsuranceFee.setVisibility(View.VISIBLE);
        } else {
            llInsuranceFee.setVisibility(View.GONE);
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
        presenter.setSelectedCourier(courierItemData);
        setText(tvDeliveryFee, courierItemData.getDeliveryPrice());
        renderTickerView(courierItemData);
        renderInsuranceView(courierItemData);
        renderAdditionalPriceView(courierItemData);
        renderDropshipperView(courierItemData);
        updateFeesGroupLayout();
    }

    @Override
    public void onShipmentItemClick(ShipmentItemData shipmentItemData) {
        presenter.setSelectedShipment(shipmentItemData);
        tvShipmentType.setText(shipmentItemData.getType());
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
