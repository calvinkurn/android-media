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
 * Created by kris on 1/2/18. Tokopedia
 */

public class AcceptOrderDialog extends DialogFragment {

    private static final String ORDER_ID_EXTRA = "ORDER_ID_EXTRA";
    private AcceptOrderListener listener;

    public static AcceptOrderDialog createDialog(String orderId) {
        AcceptOrderDialog dialog = new AcceptOrderDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_EXTRA, orderId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AcceptOrderListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (AcceptOrderListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_accept_order, container, false);
        TextView agreeButton = view.findViewById(R.id.agree_button);
        TextView cancelButton = view.findViewById(R.id.cancel_button);
        agreeButton.setOnClickListener(onAgreeButtonClicked());
        cancelButton.setOnClickListener(onCancelButtonClicked());
        return view;
    }

    private View.OnClickListener onAgreeButtonClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAcceptOrder(getArguments().getString(ORDER_ID_EXTRA));
                dismiss();
            }
        };
    }

    private View.OnClickListener onCancelButtonClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    public interface AcceptOrderListener{

        void onAcceptOrder(String orderId);

    }

}
