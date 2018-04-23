package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;

/**
 * @author by furqan on 16/04/18.
 */

public class FlightCancellationTermsAndConditionsFragment extends BaseDaggerFragment {

    private static final String TNC_LINK = "https://www.tokopedia.com/bantuan/pengembalian-dana-dan-penggantian-jadwal";

    TextViewCompat txtTerms;
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
        txtTerms = view.findViewById(R.id.txt_cancellation_tnc);
        btnSelengkapnya = view.findViewById(R.id.btn_next);
        btnSelengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToWebview();
            }
        });

        setTncText();

        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    private void setTncText() {
        txtTerms.setText(MethodChecker.fromHtml(getString(R.string.flight_cancellation_terms_and_cancellation_text)));
    }

    private void navigateToWebview() {
        if (getActivity().getApplication() instanceof FlightModuleRouter) {
            startActivity(((FlightModuleRouter) getActivity().getApplication())
                    .getWebviewActivity(getActivity(), TNC_LINK));
        }
    }
}
