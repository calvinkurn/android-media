package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.booking.view.fragment.FlightInsuranceWebViewFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

import javax.inject.Inject;

public class FlightInsuranceWebviewActivity extends BaseWebViewActivity implements HasComponent<FlightComponent> {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    @Inject
    UserSession userSession;

    public static Intent getCallingIntent(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, FlightInsuranceWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TITLE));
        getComponent().inject(this);
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightInsuranceWebViewFragment.newInstance(getIntent().getStringExtra(EXTRA_URL), userSession.getUserId());
    }

    @Override
    public FlightComponent getComponent() {
        return FlightComponentInstance.getFlightComponent(getApplication());
    }
}
