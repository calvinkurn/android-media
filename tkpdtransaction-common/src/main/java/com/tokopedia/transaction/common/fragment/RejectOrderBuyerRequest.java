package com.tokopedia.transaction.common.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.transaction.common.R;
import com.tokopedia.transaction.common.adapters.RejectOrderReasonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderBuyerRequest extends Fragment implements RejectOrderReasonAdapter.ActionListener{

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";
    private static final String NEW_CANCELLATION_FLOW_ARGUMENT = "NEW_CANCELLATION_FLOW_ARGUMENT";
    private static final String ORDER_ID = "order_id";
    private static final String REASON = "reason";
    private static final String R_CODE = "r_code";
    public static final int NEW_CANCELLATION_FLOW = 1;
    private static final int OLD_CANCELLATION_FLOW = 0;

    private RejectOrderBuyerRequestListener listener;
    private RejectOrderReasonAdapter adapter;
    private EditText otherReasonField;
    private TextView submitButton;
    private String reason;
    private String reasonCode;


    public static RejectOrderBuyerRequest createFragment(String orderId) {
        RejectOrderBuyerRequest fragment = new RejectOrderBuyerRequest();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RejectOrderBuyerRequest createFragment(String orderId, int newCancellationFlow) {
        RejectOrderBuyerRequest fragment = new RejectOrderBuyerRequest();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        bundle.putInt(NEW_CANCELLATION_FLOW_ARGUMENT, newCancellationFlow);
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
        if (getArguments().getInt(NEW_CANCELLATION_FLOW_ARGUMENT, OLD_CANCELLATION_FLOW) == NEW_CANCELLATION_FLOW) {
            View view = inflater.inflate(R.layout.order_reject_buyer_request_new, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            otherReasonField = view.findViewById(R.id.other_reason_field);
            submitButton = view.findViewById(R.id.submit_button);
            submitButton.setText(getString(R.string.ajukan));
            submitButton.setOnClickListener(onSubmitButtonClickedListener());
            /*Hardcoded as per requirement**/
            ArrayList<String> reasons = new ArrayList<>(
                    Arrays.asList(
                            getString(R.string.tkpdtransaction_title_replacement_reason_1),
                            getString(R.string.tkpdtransaction_title_replacement_reason_2),
                            getString(R.string.tkpdtransaction_title_replacement_reason_3),
                            getString(R.string.tkpdtransaction_title_replacement_reason_4),
                            getString(R.string.title_other_reason)
                    )
            );
            adapter = new RejectOrderReasonAdapter(reasons);
            adapter.setActionListener(this);
            recyclerView.setAdapter(adapter);
            return view;
        } else {
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

    @Override
    public void onChecked(int position, String reason, int reasonCode) {
        adapter.markChecked(position);
        this.reason = reason;
        this.reasonCode = reasonCode+"";
        setButtonCancelSearch(true);
        if(getString(R.string.title_other_reason).equals(reason)){
            otherReasonField.setVisibility(View.VISIBLE);
            otherReasonField.requestFocus();
            otherReasonField.setSelection(otherReasonField.getText().length());
        } else{
            otherReasonField.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener onSubmitButtonClickedListener(
    ) {
        return view -> {
            if (otherReasonField.getVisibility() == View.GONE && reason!=null) {
                HashMap<String, String> rejectParam = new HashMap<>();
                rejectParam.put(ORDER_ID, getArguments().getString(ORDER_ID_ARGUMENT));
                rejectParam.put(REASON, reason);
                rejectParam.put(R_CODE, reasonCode);
                listener.rejectOrderBuyerRequest(rejectParam);
            } else if (otherReasonField.getVisibility() == View.VISIBLE) {
                if (otherReasonField.getText().toString().isEmpty()) {
                    otherReasonField.setError(getActivity()
                            .getString(R.string.error_note_empty));
                } else {
                    HashMap<String, String> rejectParam = new HashMap<>();
                    rejectParam.put(ORDER_ID, getArguments().getString(ORDER_ID_ARGUMENT));
                    rejectParam.put(REASON, otherReasonField.getText().toString());
                    rejectParam.put(R_CODE, reasonCode);
                    listener.rejectOrderBuyerRequest(rejectParam);
                }
            } else {
                setButtonCancelSearch(false);
                Toast.makeText(
                        getActivity(),
                        "Mohon Pilih Salah Satu",
                        Toast.LENGTH_LONG).show();
            }
        };
    }

    private void setButtonCancelSearch(boolean active) {
        submitButton.setEnabled(active);
        submitButton.setBackground(active ? ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_green) :
                ContextCompat.getDrawable(getActivity(), R.drawable.bg_grey_button_rounded));
        submitButton.setTextColor(active ? ContextCompat.getColor(getActivity(), R.color.white) :
                ContextCompat.getColor(getActivity(), R.color.grey_700));
    }

    public interface RejectOrderBuyerRequestListener {
        void rejectOrderBuyerRequest(Map<String, String> rejectParam);
    }

}
