package com.tokopedia.flight.booking.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.flight.common.constant.FlightUrl;

public class FlightInsuranceWebViewFragment extends BaseWebViewFragment {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_UID = "EXTRA_UID";

    public static FlightInsuranceWebViewFragment newInstance(String url, String userId) {
        FlightInsuranceWebViewFragment fragment = new FlightInsuranceWebViewFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        args.putString(EXTRA_UID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getUrl() {
        return getArguments().getString(EXTRA_URL, FlightUrl.WEB_DOMAIN);
    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        return getArguments().getString(EXTRA_UID);
    }
}
