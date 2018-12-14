package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/9/18. Tokopedia
 */

public class CancelShipmentFragment extends TkpdFragment{

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private CancelShipmentListener listener;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static CancelShipmentFragment createFragment(String orderId) {
        CancelShipmentFragment fragment = new CancelShipmentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CancelShipmentListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CancelShipmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reject_shipment_fragment, container, false);
        TextInputLayout textInputLayout = view.findViewById(R.id.notes_text_input_layout);
        EditText cancelOrderNotesField = view.findViewById(R.id.reject_shipment_notes_field);
        Button cancelOrderConfirmButton = view.findViewById(R.id.reject_shipment_confirm_button);
        cancelOrderConfirmButton.setOnClickListener(
                onConfirmCancelOrderButton(cancelOrderNotesField, textInputLayout)
        );
        cancelOrderNotesField.requestFocus();
        return view;
    }

    private View.OnClickListener onConfirmCancelOrderButton(
            final EditText notesField,
            final TextInputLayout notesTextInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notesField.getText().toString().isEmpty()) {
                    notesTextInputLayout.setError(
                            getActivity()
                                    .getString(com.tokopedia.core2.R.string.error_note_empty)
                    );
                } else {
                    if (getArguments() != null) {
                        listener.cancelShipment(
                                getArguments().getString(ORDER_ID_ARGUMENT),
                                notesField.getText().toString()
                        );
                    }
                }
            }
        };
    }

    public interface CancelShipmentListener {
        void cancelShipment(String orderId, String notes);
    }
}
