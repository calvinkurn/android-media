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
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderCourierProblemFragment extends TkpdFragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private RejectOrderCourierReasonListener listener;

    public static RejectOrderCourierProblemFragment createFragment(String orderId) {
        RejectOrderCourierProblemFragment fragment = new RejectOrderCourierProblemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderCourierReasonListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderCourierReasonListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_reject_courier_problem, container, false);
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
                                    .getString(com.tokopedia.core.R.string.error_note_empty)
                    );
                } else {
                    if (getArguments() != null) {
                        TKPDMapParam<String, String> rejectParam = new TKPDMapParam<>();
                        rejectParam.put("order_id", getArguments().getString(ORDER_ID_ARGUMENT));
                        rejectParam.put("reason", notesField.getText().toString());
                        rejectParam.put("reason_code", "7");
                        listener.rejectOrderCourierReason(rejectParam);
                    }
                }
            }
        };
    }

    public interface RejectOrderCourierReasonListener {
        void rejectOrderCourierReason(TKPDMapParam<String, String> notes);
    }

}
