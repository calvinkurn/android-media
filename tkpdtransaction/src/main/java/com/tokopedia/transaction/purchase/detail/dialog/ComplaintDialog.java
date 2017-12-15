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

public class ComplaintDialog extends DialogFragment {

    private static final String ORDER_ID_EXTRA = "ORDER_ID_EXTRA";
    private static final String SHOP_NAME_EXTRA = "SHOP_NAME_EXTRA";

    private ComplaintDialogListener listener;

    public ComplaintDialog() {

    }

    public static ComplaintDialog createDialog(String orderId, String shopNameExtra) {
        ComplaintDialog dialog = new ComplaintDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_EXTRA, orderId);
        bundle.putString(SHOP_NAME_EXTRA, shopNameExtra);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ComplaintDialogListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ComplaintDialogListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_complaint, container, false);

        TextView complaintTitle = (TextView) view.findViewById(R.id.complaint_title);
        TextView complaintText = (TextView) view.findViewById(R.id.complaint_body);
        TextView complaintButton = (TextView) view.findViewById(R.id.receive_btn);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        Button notReceiveButton = (Button) view.findViewById(R.id.not_receive_btn);
        LinearLayout llFreeReturn = (LinearLayout) view.findViewById(R.id.layout_free_return);
        TextView tvFreeReturn = (TextView) view.findViewById(R.id.tv_free_return);

        complaintTitle.setText(getString(R.string.button_order_detail_complaint));
        complaintText.setText(String.format(getString(R.string.text_complaint_dialog),
                getArguments().getString(SHOP_NAME_EXTRA)));
        complaintButton.setOnClickListener(onComplaintButtonListener());
        cancelButton.setOnClickListener(onCancelButtonListener());

        llFreeReturn.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
        notReceiveButton.setVisibility(View.GONE);

        return view;
    }

    private View.OnClickListener onComplaintButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onComplaintClicked(getArguments().getString(ORDER_ID_EXTRA));
            }
        };
    }

    private View.OnClickListener onCancelButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    public interface ComplaintDialogListener {
        void onComplaintClicked(String orderId);
    }

}
