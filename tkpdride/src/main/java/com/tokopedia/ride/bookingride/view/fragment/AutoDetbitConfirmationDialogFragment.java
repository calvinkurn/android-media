package com.tokopedia.ride.bookingride.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

/**
 * Created by alvarisi on 3/29/17.
 */

public class AutoDetbitConfirmationDialogFragment extends DialogFragment {
    private static final String TAG = "AutoDetbitConfirmationDialogFragment";
    private static final String EXTRA_PAYMENT_METHOD = "";

    private PaymentMethodViewModel paymentMethodViewModel;


    public static AutoDetbitConfirmationDialogFragment newInstance(PaymentMethodViewModel paymentMethodViewModel) {
        AutoDetbitConfirmationDialogFragment fragment = new AutoDetbitConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PAYMENT_METHOD, paymentMethodViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentMethodViewModel = getArguments().getParcelable(EXTRA_PAYMENT_METHOD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_permission_dialog, container);


        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ScroogePGUtil.openScroogePage(getActivity(), paymentMethodViewModel.getSaveurl(), true, paymentMethodViewModel.getSaveBody(), getString(R.string.toolbar_title_auto_debit));
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
