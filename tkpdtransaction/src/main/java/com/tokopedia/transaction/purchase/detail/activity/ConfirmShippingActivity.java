package com.tokopedia.transaction.purchase.detail.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.constant.OrderShipmentTypeDef;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderCourierComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderCourierComponent;
import com.tokopedia.transaction.purchase.detail.fragment.CourierSelectionFragment;
import com.tokopedia.transaction.purchase.detail.fragment.ServiceSelectionFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.CourierSelectionModel;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.OrderDetailShipmentModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderCourierPresenterImpl;
import com.tokopedia.transaction.purchase.listener.ToolbarChangeListener;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

@RuntimePermissions
public class ConfirmShippingActivity extends TActivity
        implements ConfirmShippingView,
        ServiceSelectionFragment.ServiceSelectionListener,
        CourierSelectionFragment.OrderCourierFragmentListener,
        ToolbarChangeListener {

    private static final String EXTRA_ORDER_DETAIL_DATA = "EXTRA_ORDER_DETAIL_DATA";
    private static final String EXTRA_ORDER_MODE_KEY = "EXTRA_ORDER_MODE_KEY";
    private static final String SELECT_COURIER_FRAGMENT_TAG = "select_courier";
    public static final String SELECT_SERVICE_FRAGMENT_TAG = "select_service";
    public static final int CONFIRM_SHIPMENT_MODE = 1;
    public static final int CHANGE_COURIER_MODE = 2;

    private OrderDetailShipmentModel editableModel;

    private TextView courierName;

    private TkpdProgressDialog progressDialog;

    private EditText barcodeEditText;

    @Inject
    OrderCourierPresenterImpl presenter;

    public static Intent createInstance(Context context, OrderDetailData data) {
        Intent intent = new Intent(context, ConfirmShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_DETAIL_DATA, data);
        bundle.putInt(EXTRA_ORDER_MODE_KEY, CONFIRM_SHIPMENT_MODE);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createChangeCourierInstance(Context context, OrderDetailData data) {
        Intent intent = new Intent(context, ConfirmShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_DETAIL_DATA, data);
        bundle.putInt(EXTRA_ORDER_MODE_KEY, CHANGE_COURIER_MODE);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_confirm_payment);
        initInjector();
        presenter.setView(this);
        OrderDetailData orderDetailData = getIntent().getExtras()
                .getParcelable(EXTRA_ORDER_DETAIL_DATA);
        initateData(orderDetailData);
        initiateView(orderDetailData);
    }

    private void initiateView(OrderDetailData orderDetailData) {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        courierName = findViewById(R.id.courier_name);
        barcodeEditText = findViewById(R.id.barcode_edit_text);
        ImageView barcodeScanner = findViewById(R.id.icon_scan);
        LinearLayout courierLayout = findViewById(R.id.courier_layout);
        TextView confirmButton = findViewById(R.id.confirm_button);
        if (isChangeCourierMode(Integer.parseInt(orderDetailData.getOrderCode())))
            toolbar.setTitle(getString(R.string.button_order_detail_change_courier));
        courierLayout.setOnClickListener(onGetCourierButtonClickedListener(orderDetailData));
        confirmButton.setOnClickListener(onConfirmButtonClickedListener(barcodeEditText));
        barcodeEditText.setText(orderDetailData.getAwb());
        barcodeScanner.setOnClickListener(onBarcodeScanClickedListener());
        courierName.setText(editableModel.getShipmentName() + " " + editableModel.getPackageName());
    }

    private boolean isChangeCourierMode(int orderCode) {
        return (orderCode >= OrderShipmentTypeDef.ORDER_WAITING
                && orderCode< OrderShipmentTypeDef.ORDER_DELIVERED)
                || getIntent().getExtras().getInt(EXTRA_ORDER_MODE_KEY) == CHANGE_COURIER_MODE;
    }

    private void initateData(OrderDetailData orderDetailData) {
        editableModel = new OrderDetailShipmentModel();
        if(getIntent().getExtras().getInt(EXTRA_ORDER_MODE_KEY) == CONFIRM_SHIPMENT_MODE) {
            editableModel.setShipmentId(orderDetailData.getShipmentId());
            editableModel.setPackageId(orderDetailData.getShipmentServiceId());
            editableModel.setShipmentName(orderDetailData.getShipmentName());
            editableModel.setPackageName(orderDetailData.getShipmentServiceName());
        }
        editableModel.setOrderId(orderDetailData.getOrderId());
        editableModel.setOrderStatusCode(Integer.parseInt(orderDetailData.getOrderCode()));
    }

    @Override
    public void receiveShipmentData(ListCourierViewModel model) {
        CourierSelectionFragment courierSelectionFragment = CourierSelectionFragment.
                createInstance(model);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                .add(R.id.main_view, courierSelectionFragment, SELECT_COURIER_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onSuccessConfirm(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();
        //TODO REMOVE IF BUGGY
        setResult(Activity.RESULT_OK);
        finish();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcode() {
        CommonUtils.requestBarcodeScanner(this, CustomScannerBarcodeActivity.class);
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onShowError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void onFinishSelectShipment(CourierSelectionModel courierSelectionModel) {
        removeServiceSelectionFragment();
        removeCourierSelectionFragment();
        generateShipmentData(courierSelectionModel);
        toolbar.setTitle(getString(R.string.title_confirm_shipment));
    }

    @Override
    public void onCourierAdapterSelected(CourierSelectionModel model) {
        removeCourierSelectionFragment();
        generateShipmentData(model);
    }

    private void generateShipmentData(CourierSelectionModel courierSelectionModel) {
        editableModel.setShipmentName(courierSelectionModel.getCourierName());
        editableModel.setPackageName(courierSelectionModel.getServiceName());
        editableModel.setPackageId(courierSelectionModel.getServiceId());
        editableModel.setShipmentId(courierSelectionModel.getCourierId());
        courierName.setText(
                editableModel.getShipmentName() + " " + editableModel.getPackageName()
        );
    }

    private View.OnClickListener onGetCourierButtonClickedListener(final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGetCourierList(ConfirmShippingActivity.this, data);
            }
        };
    }

    private View.OnClickListener onConfirmButtonClickedListener(final EditText barcodeEditText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editableModel.getPackageId() == null || editableModel.getPackageId().isEmpty()) {
                    NetworkErrorHelper.showSnackbar(
                            ConfirmShippingActivity.this,
                            getString(R.string.error_no_courier_chosen)
                    );
                } else {
                    editableModel.setShippingRef(barcodeEditText.getText().toString());
                    presenter.onProcessCourier(
                            ConfirmShippingActivity.this, editableModel,
                            isChangeCourierMode(editableModel.getOrderStatusCode()));
                }
            }
        };
    }

    private View.OnClickListener onBarcodeScanClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmShippingActivityPermissionsDispatcher
                        .onScanBarcodeWithCheck(ConfirmShippingActivity.this);
            }
        };
    }

    private void initInjector() {
        OrderCourierComponent component = DaggerOrderCourierComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(SELECT_SERVICE_FRAGMENT_TAG) != null) {
            removeServiceSelectionFragment();
            toolbar.setTitle(R.string.label_select_courier);
        } else if (getFragmentManager().findFragmentByTag(SELECT_COURIER_FRAGMENT_TAG) != null) {
            removeCourierSelectionFragment();
            toolbar.setTitle(R.string.title_confirm_shipment);
        } else super.onBackPressed();
    }

    private void removeCourierSelectionFragment() {
        getFragmentManager()
                .beginTransaction()
                .remove(getFragmentManager()
                        .findFragmentByTag(SELECT_COURIER_FRAGMENT_TAG)).commit();
    }

    private void removeServiceSelectionFragment() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_out_right, R.animator.slide_out_right)
                .remove(getFragmentManager()
                        .findFragmentByTag(SELECT_SERVICE_FRAGMENT_TAG)).commit();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        barcodeEditText.setText(CommonUtils.getBarcode(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRemoveTitle() {
        toolbar.setTitle("");
    }

    @Override
    public void onChangeTitle(String toolbarTitle) {
        toolbar.setTitle(toolbarTitle);
    }
}
