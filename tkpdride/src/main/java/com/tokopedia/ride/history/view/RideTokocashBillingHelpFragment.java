package com.tokopedia.ride.history.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.ride.R;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

public class RideTokocashBillingHelpFragment extends BaseFragment {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private RideHistoryViewModel rideHistory;


    public RideTokocashBillingHelpFragment() {
        // Required empty public constructor
    }


    public static RideTokocashBillingHelpFragment newInstance(RideHistoryViewModel rideHistory) {
        RideTokocashBillingHelpFragment fragment = new RideTokocashBillingHelpFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_REQUEST_ID, rideHistory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rideHistory = getArguments().getParcelable(EXTRA_REQUEST_ID);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_need_help_tokocash_billing;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
