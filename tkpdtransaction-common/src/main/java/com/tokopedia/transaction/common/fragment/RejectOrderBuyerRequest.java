package com.tokopedia.transaction.common.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.transaction.common.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderBuyerRequest extends Fragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private RejectOrderBuyerRequestListener listener;


    public static RejectOrderBuyerRequest createFragment(String orderId) {
        RejectOrderBuyerRequest fragment = new RejectOrderBuyerRequest();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }


    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderBuyerRequestListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderBuyerRequestListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_reject_buyer_request, container, false);
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
                                    .getString(R.string.error_note_empty)
                    );
                } else {
                    if (getArguments() != null) {
                        HashMap<String, String> rejectParam = new HashMap<>();
                        rejectParam.put("order_id", getArguments().getString(ORDER_ID_ARGUMENT));
                        rejectParam.put("reason", notesField.getText().toString());
                        rejectParam.put("reason_code", "8");
                        listener.rejectOrderBuyerRequest(rejectParam);
                    }
                }
            }
        };
    }

    public interface RejectOrderBuyerRequestListener {
        void rejectOrderBuyerRequest(Map<String, String> rejectParam);
    }

}
