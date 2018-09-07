package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

public class RideHistoryActivity extends BaseActivity implements RideHistoryFragment.OnFragmentInteractionListener, HasComponent<RideComponent> {

    private static final int REQUEST_CODE_TRIP_DETAIL = 101;
    private RideComponent rideComponent;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, RideHistoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        setupToolbar();
        replaceFragment(R.id.fl_container, RideHistoryFragment.newInstance());
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.your_trips_toolbar_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_HISTORY;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RideGATracking.eventBackPress(getScreenName());
    }

    @Override
    public void actionNavigateToDetail(RideHistoryViewModel rideHistory) {
        Intent intent = RideHistoryDetailActivity.getCallingIntent(this, rideHistory);
        startActivityForResult(intent, REQUEST_CODE_TRIP_DETAIL);
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null) initInjector();
        return rideComponent;
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TRIP_DETAIL) {
            if (resultCode == RESULT_OK) {
                RideHistoryFragment fragment = (RideHistoryFragment) getFragmentManager().findFragmentById(R.id.fl_container);
                if (fragment != null) {
                    fragment.actionRefreshHistoriesData();
                }
            }
        }
    }
}
