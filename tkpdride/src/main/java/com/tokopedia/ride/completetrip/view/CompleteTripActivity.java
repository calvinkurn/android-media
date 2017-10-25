package com.tokopedia.ride.completetrip.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;

public class CompleteTripActivity extends BaseActivity implements CompleteTripFragment.OnFragmentInteractionListener,
        HasComponent<RideComponent> {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_DRIVER_VEHICLE = "EXTRA_DRIVER_VEHICLE";
    private RideComponent rideComponent;

    public static Intent getCallingIntent(Activity activity, String requestId, DriverVehicleAddressViewModel driverVehicleAddressViewModel) {
        Intent intent = new Intent(activity, CompleteTripActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_DRIVER_VEHICLE, driverVehicleAddressViewModel);
        return intent;
    }

    public static Intent getCallingIntentFromPushNotification(Context activity, String requestId, DriverVehicleAddressViewModel driverVehicleAddressViewModel) {
        Intent intent = new Intent(activity, CompleteTripActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_DRIVER_VEHICLE, driverVehicleAddressViewModel);
        intent.putExtra(Constants.EXTRA_FROM_PUSH, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_completed);
        setupToolbar();
        clearActiveRequest();

        String requestId = getIntent().getStringExtra(EXTRA_REQUEST_ID);
        DriverVehicleAddressViewModel viewModel = getIntent().getParcelableExtra(EXTRA_DRIVER_VEHICLE);
        if (getIntent().getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)) {
            replaceFragment(R.id.container, CompleteTripFragment.newInstanceFromNotif(requestId, viewModel));
        } else {
            replaceFragment(R.id.container, CompleteTripFragment.newInstance(requestId, viewModel));
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_COMPLETED;
    }

    private void clearActiveRequest() {
        RideConfiguration configuration = new RideConfiguration(this);
        configuration.clearActiveRequestId();
        configuration.clearActiveProductName();
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.title_trip_completed));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RideGATracking.eventBackPress(getScreenName());
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        actionBackToAppHomeScreen();
    }

    private void actionBackToAppHomeScreen() {
        Intent homeIntent = HomeRouter.getHomeActivity(getApplicationContext());
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    @Override
    public void actionSuccessRatingSubmited() {
        actionBackToAppHomeScreen();
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