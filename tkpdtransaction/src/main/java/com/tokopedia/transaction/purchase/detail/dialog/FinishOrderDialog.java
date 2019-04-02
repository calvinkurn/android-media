package com.tokopedia.transaction.purchase.detail.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 11/16/17. Tokopedia
 */

public class FinishOrderDialog extends DialogFragment {

    private static final String ORDER_ID_KEY = "ORDER_ID_KEY";
    private static final String ORDER_STATUS_KEY = "ORDER_STATUS_KEY";

    private FinishOrderDialogListener listener;

    public FinishOrderDialog() {

    }

    public static FinishOrderDialog createDialog(String orderId, String orderStatus) {
        FinishOrderDialog dialog = new FinishOrderDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_KEY, orderId);
        bundle.putString(ORDER_STATUS_KEY, orderStatus);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FinishOrderDialogListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (FinishOrderDialogListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_complaint, container, false);

        TextView complaintTitle = (TextView) view.findViewById(R.id.complaint_title);
        TextView complaintText = (TextView) view.findViewById(R.id.complaint_body);
        TextView complaintButton = (TextView) view.findViewById(R.id.receive_btn);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        Button notReceiveButton = (Button) view.findViewById(R.id.not_receive_btn);
        LinearLayout llFreeReturn = (LinearLayout) view.findViewById(R.id.layout_free_return);
        TextView tvFreeReturn = (TextView) view.findViewById(R.id.tv_free_return);

        complaintTitle.setText(getString(R.string.title_dialog_finish_order));
        complaintText.setText(getString(R.string.body_dialog_finish_order));
        complaintButton.setOnClickListener(onFinishButtonClickedListener());
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        llFreeReturn.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
        notReceiveButton.setVisibility(View.GONE);
        return view;
    }

    private View.OnClickListener onFinishButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmFinish(getArguments().getString(ORDER_ID_KEY),
                        getArguments().getString(ORDER_STATUS_KEY));
            }
        };
    }

    public interface FinishOrderDialogListener {

        void onConfirmFinish(String orderId, String orderStatus);

    }

}
