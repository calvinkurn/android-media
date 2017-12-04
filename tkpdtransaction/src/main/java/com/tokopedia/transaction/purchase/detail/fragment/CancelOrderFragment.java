package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 11/29/17. Tokopedia
 */

public class CancelOrderFragment extends TkpdFragment{

    private CancelOrderListener listener;

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    @Override
    protected String getScreenName() {
        return null;
    }

    public CancelOrderFragment() {

    }

    public static CancelOrderFragment createFragment(String orderId) {
        CancelOrderFragment fragment = new CancelOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(orderId, ORDER_ID_ARGUMENT);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CancelOrderListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CancelOrderListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cancel_order_fragment, container, false);
        EditText cancelOrderNotesField = view.findViewById(R.id.cancel_order_notes_field);
        Button cancelOrderConfirmButton = view.findViewById(R.id.cancel_order_confirm_button);
        cancelOrderConfirmButton.setOnClickListener(
                onConfirmCancelOrderButton(cancelOrderNotesField)
        );
        return view;
    }

    private View.OnClickListener onConfirmCancelOrderButton(final EditText notesField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cancelOrder(
                        getArguments().getString(ORDER_ID_ARGUMENT),
                        notesField.getText().toString()
                );
            }
        };
    }

    public interface CancelOrderListener {
        void cancelOrder(String orderId, String notes);
    }
}
