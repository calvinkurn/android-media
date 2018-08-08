package com.tokopedia.transaction.purchase.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 9/12/17. Tokopedia
 */

public class CancelTransactionDialog extends DialogFragment {

    private static final String MESSAGE_KEY = "MESSAGE_KEY";
    private static final String PAYMENT_ID_KEY = "PAYMENT_ID_KEY";

    private CancelPaymentDialogListener listener;

    public CancelTransactionDialog() {

    }

    public static CancelTransactionDialog showCancelTransactionDialog(String message,
                                                                         String paymentId) {
        CancelTransactionDialog dialog = new CancelTransactionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_KEY, message);
        bundle.putString(PAYMENT_ID_KEY, paymentId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CancelPaymentDialogListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CancelPaymentDialogListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.cancel_transaction_dialog, container, false);
        TextView dialogMessage = (TextView) view.findViewById(R.id.message_cancel_transaction);
        TextView confirmDeleteTransaction = (TextView) view
                .findViewById(R.id.confirm_delete_transaction_button);
        TextView cancelDelete = (TextView) view
                .findViewById(R.id.cancel_button);
        TextView cancelDialogTitle = (TextView) view
                .findViewById(R.id.cancel_transaction_dialog_title);

        //TODO once minimum API > 17
        //listener = (CancelPaymentDialogListener) getParentFragment();

        confirmDeleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirmCancelDialog(getArguments().getString(PAYMENT_ID_KEY));
                dismiss();
            }
        });
        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String message = getArguments().getString(MESSAGE_KEY);
        if(message == null || message.isEmpty()) {
            cancelDialogTitle.setVisibility(View.GONE);
            message = getActivity().getString(R.string.question_cancel_transaction);
        }
        dialogMessage.setText(Html.fromHtml(message));
        return view;
    }

    public interface CancelPaymentDialogListener {
        void confirmCancelDialog(String paymentId);
    }

}
