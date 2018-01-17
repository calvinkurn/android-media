package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/16/18. Tokopedia
 */

public class RequestPickupFragment extends Fragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private ConfirmRequestPickupListener listener;

    public static RequestPickupFragment createFragment(String orderId) {
        RequestPickupFragment fragment = new RequestPickupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ConfirmRequestPickupListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ConfirmRequestPickupListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_pickup_fragment, container, false);
        Button confirmRequestPickup = view.findViewById(R.id.request_pickup_confirm_button);
        confirmRequestPickup.setOnClickListener(onConfirmRequestPickup());
        return view;
    }

    private View.OnClickListener onConfirmRequestPickup() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onConfirmPickup(getArguments().getString(ORDER_ID_ARGUMENT));
            }
        };
    }

    public interface ConfirmRequestPickupListener {
        void onConfirmPickup(String orderId);
    }
}
