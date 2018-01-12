package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import static com.tokopedia.transaction.purchase.detail.fragment.RejectOrderWeightPriceFragment.FRAGMENT_EDIT_WEIGHT_PRICE_REQUEST_CODE;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderProductPriceWeightEditFragment extends TkpdFragment{

    private static final String EDITABLE_EXTRA = "EDITABLE_EXTRA";

    public static RejectOrderProductPriceWeightEditFragment createFragment(
            WrongProductPriceWeightEditable editable
    ) {
        RejectOrderProductPriceWeightEditFragment fragment = new RejectOrderProductPriceWeightEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDITABLE_EXTRA, editable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        WrongProductPriceWeightEditable editable = getArguments()
                .getParcelable(EDITABLE_EXTRA);
        View view = inflater.inflate(R.layout.order_reject_price_weight_edit_page, container, false);
        Spinner currencySpinner = view.findViewById(R.id.currency_spinner);
        EditText priceEditText = view.findViewById(R.id.price);
        Spinner weightSpinner = view.findViewById(R.id.weight_spinner);
        EditText weightEditText = view.findViewById(R.id.weight_amount);
        Button rejectOrderConfirmButton = view.findViewById(R.id.reject_order_confirm_button);
        rejectOrderConfirmButton.setOnClickListener(onConfirmButtonClickedListener(
                editable, priceEditText, weightEditText, currencySpinner, weightSpinner
        ));
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    private View.OnClickListener onConfirmButtonClickedListener(final WrongProductPriceWeightEditable editable,
                                                                final EditText priceEditText,
                                                                final EditText weightEditText,
                                                                final Spinner priceSpinner,
                                                                final Spinner weightSpinner) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editable.setProductPrice(priceEditText.getText().toString());
                editable.setProductWeight(weightEditText.getText().toString());
                editable.setWeightMode((int)priceSpinner.getSelectedItem());
                editable.setWeightMode((int)weightSpinner.getSelectedItem());
                getTargetFragment().onActivityResult(
                        FRAGMENT_EDIT_WEIGHT_PRICE_REQUEST_CODE,
                        Activity.RESULT_OK, new Intent());
            }
        };
    }

}
