package com.tokopedia.flight.dashboard.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.flight.R;
import com.tokopedia.flight.dashboard.view.fragment.passdata.SelectFlightPassengerPassData;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFlightPassengerFragment extends Fragment {

    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private OnFragmentInteractionListener interactionListener;

    public SelectFlightPassengerFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {

    }

    public static SelectFlightPassengerFragment newInstance(SelectFlightPassengerPassData passData) {
        SelectFlightPassengerFragment fragment = new SelectFlightPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_flight_passenger, container, false);
    }


}
