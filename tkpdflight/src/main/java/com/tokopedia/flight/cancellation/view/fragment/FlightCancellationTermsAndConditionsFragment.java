package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;

/**
 * @author by furqan on 16/04/18.
 */

public class FlightCancellationTermsAndConditionsFragment extends BaseDaggerFragment {

    AppCompatButton btnSelengkapnya;

    public static FlightCancellationTermsAndConditionsFragment createInstance() {
        FlightCancellationTermsAndConditionsFragment fragment = new FlightCancellationTermsAndConditionsFragment();
        return fragment;
    }

    public FlightCancellationTermsAndConditionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_terms_and_conditions, container, false);
        btnSelengkapnya = view.findViewById(R.id.btn_next);

        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
