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
import android.widget.TextView;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 11/16/17. Tokopedia
 */

public class FinishOrderDialog extends DialogFragment {

    private static final String ORDER_ID_KEY = "ORDER_ID_KEY";

    private FinishOrderDialogListener listener;

    public FinishOrderDialog() {

    }

    public static FinishOrderDialog createDialog(String orderId) {
        FinishOrderDialog dialog = new FinishOrderDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_KEY, orderId);
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
        View view = inflater.inflate(R.layout.finish_order_dialog, container, false);
        TextView finishButton = (TextView) view.findViewById(R.id.finish_button);
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_button);
        finishButton.setOnClickListener(onFinishButtonClickedListener());
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    private View.OnClickListener onFinishButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmFinish(getArguments().getString(ORDER_ID_KEY));
            }
        };
    }

    public interface FinishOrderDialogListener {

        void onConfirmFinish(String orderId);

    }

}
