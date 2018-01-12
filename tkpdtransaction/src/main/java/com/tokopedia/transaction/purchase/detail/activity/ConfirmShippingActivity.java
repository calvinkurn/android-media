package com.tokopedia.transaction.purchase.detail.activity;

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
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderCourierComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderCourierComponent;
import com.tokopedia.transaction.purchase.detail.fragment.CourierSelectionFragment;
import com.tokopedia.transaction.purchase.detail.fragment.ServiceSelectionFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.CourierSelectionModel;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.OrderDetailShipmentModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderCourierPresenterImpl;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

import javax.inject.Inject;

public class ConfirmShippingActivity extends TActivity
        implements ConfirmShippingView,
        ServiceSelectionFragment.ServiceSelectionListener,
        CourierSelectionFragment.OrderCourierFragmentListener{

    private static final int REQUEST_CODE_BARCODE = 1;
    private static final String EXTRA_ORDER_DETAIL_DATA = "EXTRA_ORDER_DETAIL_DATA";
    private static final String SELECT_COURIER_FRAGMENT_TAG = "select_courier";
    public static final String SELECT_SERVICE_FRAGMENT_TAG = "select_service";

    private OrderDetailShipmentModel editableModel;

    private TextView courierName;

    private TkpdProgressDialog progressDialog;

    @Inject
    OrderCourierPresenterImpl presenter;

    public static Intent createInstance(Context context, OrderDetailData data) {
        Intent intent = new Intent(context, ConfirmShippingActivity.class);
        intent.putExtra(EXTRA_ORDER_DETAIL_DATA, data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_confirm_payment);
        initInjector();
        presenter.setView(this);
        OrderDetailData orderDetailData = getIntent().getParcelableExtra(EXTRA_ORDER_DETAIL_DATA);
        initateData(orderDetailData);
        initiateView(orderDetailData);
    }

    private void initiateView(OrderDetailData orderDetailData) {
        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        courierName = findViewById(R.id.courier_name);
        EditText barcodeEditText = findViewById(R.id.barcode_edit_text);
        ImageView barcodeScanner = findViewById(R.id.icon_scan);
        LinearLayout courierLayout = findViewById(R.id.courier_layout);
        TextView confirmButton = findViewById(R.id.confirm_button);
        courierLayout.setOnClickListener(onGetCourierButtonClickedListener(orderDetailData));
        confirmButton.setOnClickListener(onConfirmButtonClickedListener(barcodeEditText));
        barcodeEditText.setText(orderDetailData.getAwb());
        barcodeScanner.setOnClickListener(onBarcodeScanClickedListener());
        courierName.setText(editableModel.getShipmentName() + " " + editableModel.getPackageName());
    }

    private void initateData(OrderDetailData orderDetailData) {
        editableModel = new OrderDetailShipmentModel();
        editableModel.setOrderId(orderDetailData.getOrderId());
        editableModel.setShipmentId(orderDetailData.getShipmentId());
        editableModel.setPackageId(orderDetailData.getShipmentServiceId());
        editableModel.setShipmentName(orderDetailData.getShipmentName());
        editableModel.setPackageName(orderDetailData.getShipmentServiceName());
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
                editableModel.setShippingRef(barcodeEditText.getText().toString());
                presenter.onConfirmShipping(ConfirmShippingActivity.this, editableModel);
            }
        };
    }

    private View.OnClickListener onBarcodeScanClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO initiate scan barcode here
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
        if(getFragmentManager().findFragmentByTag(SELECT_SERVICE_FRAGMENT_TAG) != null) {
            removeServiceSelectionFragment();
        } else if(getFragmentManager().findFragmentByTag(SELECT_COURIER_FRAGMENT_TAG) != null) {
            removeCourierSelectionFragment();
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
                .remove(getFragmentManager()
                        .findFragmentByTag(SELECT_SERVICE_FRAGMENT_TAG)).commit();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
