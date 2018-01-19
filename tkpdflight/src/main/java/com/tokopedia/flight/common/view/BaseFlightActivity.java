package com.tokopedia.flight.common.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.orderlist.view.FlightOrderListActivity;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/5/17.
 */

public abstract class BaseFlightActivity extends BaseSimpleActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    FlightAnalytics flightAnalytics;

    private FlightComponent component;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initInjector();
    }

    private void initInjector() {
        getFlightComponent().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_flight_dashboard, menu);
        updateOptionMenuColorWhite(menu);
        return true;
    }

    protected FlightComponent getFlightComponent() {
        if (component == null) {
            component = FlightComponentInstance.getFlightComponent(getApplication());
        }
        return component;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_promo) {
            if (getApplication() instanceof FlightModuleRouter
                    && ((FlightModuleRouter) getApplication())
                    .getBannerWebViewIntent(this, FlightUrl.ALL_PROMO_LINK) != null) {
                startActivity(((FlightModuleRouter) getApplication())
                        .getBannerWebViewIntent(this, FlightUrl.ALL_PROMO_LINK));
            }
            return true;
        } else if (item.getItemId() == R.id.menu_transaction_list) {
            flightAnalytics.eventClickTransactions(getScreenName());
            startActivity(FlightOrderListActivity.getCallingIntent(this));
            return true;
        } else if (item.getItemId() == R.id.menu_help) {
            if (getApplication() instanceof FlightModuleRouter
                    && ((FlightModuleRouter) getApplication())
                    .getDefaultContactUsIntent(this) != null) {
                startActivity(((FlightModuleRouter) getApplication())
                        .getDefaultContactUsIntent(this));
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
