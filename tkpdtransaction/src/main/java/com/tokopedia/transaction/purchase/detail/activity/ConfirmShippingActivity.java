package com.tokopedia.transaction.purchase.detail.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.fragment.CourierSelectionFragment;
import com.tokopedia.transaction.purchase.detail.fragment.ServiceSelectionFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.CourierSelectionModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class ConfirmShippingActivity extends TActivity
        implements ConfirmShippingView, ServiceSelectionFragment.ServiceSelectionListener{

    private static final String SELECT_COURIER_FRAGMENT_TAG = "select_courier";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_confirm_payment);
        EditText barcodeEditText = findViewById(R.id.barcode_edit_text);
        LinearLayout courierLayout = findViewById(R.id.courier_layout);
        TextView confirmButton = findViewById(R.id.confirm_button);
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
    public void onFinishSelectShipment(CourierSelectionModel courierSelectionModel) {

    }
}
