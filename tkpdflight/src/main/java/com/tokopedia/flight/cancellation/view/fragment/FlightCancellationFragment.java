package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationPresenter;

import javax.inject.Inject;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationFragment extends BaseDaggerFragment implements FlightCancellationContract.View {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";

    @Inject
    private FlightCancellationPresenter flightCancellationPresenter;

    public static FlightCancellationFragment createInstance(String invoiceId) {
        FlightCancellationFragment fragment = new FlightCancellationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
