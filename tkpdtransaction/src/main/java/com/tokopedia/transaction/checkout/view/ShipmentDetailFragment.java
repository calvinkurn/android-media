package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentDetailPresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailFragment extends BasePresenterFragment implements IShipmentDetailView,
        CourierChoiceAdapter.ViewListener, OnMapReadyCallback {

    public static final int REQUSET_CODE_OPEN_SHIPMENT_CHOICE = 1;

    @BindView(R2.id.scroll_view_content)
    ScrollView scrollViewContent;
    @BindView(R2.id.ll_network_error_view)
    LinearLayout llNetworkErrorView;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.ll_shipment_choice)
    LinearLayout llShipmentChoice;
    @BindView(R2.id.tv_shipment_type)
    TextView tvShipmentType;
    @BindView(R2.id.rv_courier_choice)
    RecyclerView rvCourierChoice;
    @BindView(R2.id.tv_shipment_information)
    TextView tvShipmentInformation;
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
    @BindView(R2.id.tv_subtotal)
    TextView tvSubtotal;
    @BindView(R2.id.bt_save)
    Button btSave;

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
    public void renderInstantShipment(ShipmentDetailData shipmentDetailData) {
        setupMapView();
        if (shipmentDetailData.getLatitude() == null || shipmentDetailData.getLongitude() == null) {
            renderNoPinpoint();
        } else {
            renderPinpoint();
        }
    }

    @Override
    public void renderRegularShipment(ShipmentDetailData shipmentDetailData) {
        flPinpointMap.setVisibility(View.GONE);
    }

    private void setupRecyclerView(List<CourierItemData> couriers) {
        courierChoiceAdapter = new CourierChoiceAdapter(couriers, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvCourierChoice.setLayoutManager(linearLayoutManager);
        rvCourierChoice.setAdapter(courierChoiceAdapter);
    }

    private void renderNoPinpoint() {
        btChangePinpoint.setVisibility(View.GONE);
        btChoosePinpoint.setVisibility(View.VISIBLE);
        tvNoPonpointInformation.setVisibility(View.VISIBLE);
    }

    private void renderPinpoint() {
        btChoosePinpoint.setVisibility(View.GONE);
        tvNoPonpointInformation.setVisibility(View.GONE);
        btChangePinpoint.setVisibility(View.VISIBLE);
    }

    private void setupMapView() {
        if (mapViewPinpoint != null) {
            mapViewPinpoint.onCreate(null);
            mapViewPinpoint.onResume();
            mapViewPinpoint.getMapAsync(this);
        }
    }

    private void setGoogleMap(GoogleMap googleMap) {
        if (googleMap != null) {
            Double latitude = 0D;
            Double longitude = 0D;
            LatLng latLng = new LatLng(latitude, longitude);

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

    @OnClick(R2.id.ll_shipment_choice)
    void onShipmentChoiceClicked() {

    }

    @OnCheckedChanged(R2.id.switch_insurance)
    void onSwitchInsuranceChanged() {

    }

    @OnCheckedChanged(R2.id.switch_partly_accept)
    void onSwitchPartlyAcceptChanged() {

    }

    @OnCheckedChanged(R2.id.switch_dropshipper)
    void onSwitchDropshipperChanged() {
        llDropshipperInfo.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.img_bt_insurance_info)
    void onInsuranceInfoClick() {
        showBottomSheet(getString(R.string.title_bottomsheet_insurance),
                "", R.drawable.ic_insurance);
    }

    @OnClick(R2.id.img_bt_partly_accept_info)
    void onPartlyAcceptInfoClick() {
        showBottomSheet(getString(R.string.title_bottomsheet_insurance),
                "", R.drawable.ic_insurance);
    }

    @OnClick(R2.id.img_bt_dropshipper_info)
    void onDropshipperInfoClick() {
        showBottomSheet(getString(R.string.title_bottomsheet_insurance),
                "", R.drawable.ic_insurance);
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

    @Override
    public void onCourierItemClick(CourierItemData courierItemData) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setGoogleMap(googleMap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
