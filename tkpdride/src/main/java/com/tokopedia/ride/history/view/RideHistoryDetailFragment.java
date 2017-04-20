package com.tokopedia.ride.history.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.ride.R;
import com.tokopedia.ride.base.presentation.BaseFragment;

public class RideHistoryDetailFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private String requestId;

    public RideHistoryDetailFragment() {
        // Required empty public constructor
    }


    public static RideHistoryDetailFragment newInstance(String requestId) {
        RideHistoryDetailFragment fragment = new RideHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestId = getArguments().getString(EXTRA_REQUEST_ID);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_history_detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
