package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/16/18. Tokopedia
 */

public class RequestPickupFragment extends Fragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";
    public static final String INSTANT_COURIER_INFO_URL = "https://www.tokopedia.com/bantuan/penjual/pengiriman-penjual/layanan-pengiriman-dengan-sistem-pick-up-pengiriman-penjual/";
    public static final String INFO_FRAGMENT_TAG = "info_fragment";

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
        TextView requestPickupStep2 = view.findViewById(R.id.request_pickup_step_2_text);
        requestPickupStep2.setText(Html.fromHtml(getString(R.string.request_pickup_step_2)));
        TextView requestPickupInfo = view.findViewById(R.id.request_pickup_info);
        requestPickupInfo.setText(Html.fromHtml(getString(R.string.request_pickup_info)));
        confirmRequestPickup.setOnClickListener(onConfirmRequestPickup());
        requestPickupInfo.setOnClickListener(onInfoButtonClickedListener());
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

    private View.OnClickListener onInfoButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentGeneralWebView webViewFragment = FragmentGeneralWebView
                        .createInstance(INSTANT_COURIER_INFO_URL, false);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                        .add(R.id.main_view, webViewFragment, INFO_FRAGMENT_TAG)
                        .commit();
            }
        };
    }

    public interface ConfirmRequestPickupListener {
        void onConfirmPickup(String orderId);
    }
}
