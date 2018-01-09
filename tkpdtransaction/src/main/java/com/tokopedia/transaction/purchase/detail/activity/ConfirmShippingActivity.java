package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.app.TActivity;
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
        implements ConfirmShippingView, ServiceSelectionFragment.ServiceSelectionListener{

    private static final int REQUEST_CODE_BARCODE = 1;
    private static final String EXTRA_ORDER_DETAIL_DATA = "EXTRA_ORDER_DETAIL_DATA";
    private static final String SELECT_COURIER_FRAGMENT_TAG = "select_courier";
    public static final String SELECT_SERVICE_FRAGMENT_TAG = "select_service";

    private OrderDetailShipmentModel editableModel;

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
        editableModel = new OrderDetailShipmentModel();
        editableModel.setOrderId(orderDetailData.getOrderId());
        editableModel.setShipmentId(orderDetailData.getShipmentId());
        editableModel.setPackageId(orderDetailData.getShipmentServiceId());
        editableModel.setShipmentName(orderDetailData.getShipmentName());
        editableModel.setPackageName(orderDetailData.getShipmentServiceName());
        EditText barcodeEditText = findViewById(R.id.barcode_edit_text);
        ImageView barcodeScanner = findViewById(R.id.icon_scan);
        LinearLayout courierLayout = findViewById(R.id.courier_layout);
        TextView confirmButton = findViewById(R.id.confirm_button);
        courierLayout.setOnClickListener(onGetCourierButtonClickedListener());
        confirmButton.setOnClickListener(onConfirmButtonClickedListener(barcodeEditText));
        barcodeEditText.setText(orderDetailData.getAwb());
        barcodeScanner.setOnClickListener(onBarcodeScanClickedListener());
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
        finish();
    }

    @Override
    public void onShowError(String errorMessage) {

    }

    @Override
    public void onFinishSelectShipment(CourierSelectionModel courierSelectionModel) {
        removeServiceSelectionFragment();
        removeCourierSelectionFragment();
        editableModel.setShipmentName(courierSelectionModel.getCourierName());
        editableModel.setPackageName(courierSelectionModel.getServiceName());
        editableModel.setShipmentId(courierSelectionModel.getCourierId());
        editableModel.setPackageId(courierSelectionModel.getServiceId());
    }

    private View.OnClickListener onGetCourierButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGetCourierList(ConfirmShippingActivity.this);
            }
        };
    }

    private View.OnClickListener onConfirmButtonClickedListener(final EditText barcodeEditText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editableModel.setShippingRef(barcodeEditText.getText().toString());
                presenter.onConfirmShipping(editableModel);
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
}
