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
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

public class RideHistoryDetailActivity extends BaseActivity implements RideHistoryDetailFragment.OnFragmentInteractionListener,
        FragmentGeneralWebView.OnFragmentInteractionListener, HasComponent<RideComponent> {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private RideHistoryViewModel rideHistory;
    private RideComponent rideComponent;

    public static Intent getCallingIntent(Activity activity, RideHistoryViewModel rideHistory) {
        Intent intent = new Intent(activity, RideHistoryDetailActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, rideHistory);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_detail);
        setupToolbar();
        rideHistory = getIntent().getParcelableExtra(EXTRA_REQUEST_ID);
        replaceFragment(R.id.top_container, RideHistoryDetailFragment.newInstance(rideHistory));
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.trip_detail_toolbar_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
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
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_HISTORY_DETAIL;
    }

    public void setTitle(int resId) {
        getSupportActionBar().setTitle(resId);
    }

    @Override
    public void rideHistoryUpdated(RideHistoryViewModel viewModel) {
        this.rideHistory = viewModel;
    }


    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {

    }

    @Override
    public void onWebViewProgressLoad() {

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
}
