package com.tokopedia.ride.completetrip.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleViewModel;

public class CompleteTripActivity extends BaseActivity {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_DRIVER_VEHICLE = "EXTRA_DRIVER_VEHICLE";

    public static Intent getCallingIntent(Activity activity, String requestId, DriverVehicleViewModel driverAndVehicle) {
        Intent intent = new Intent(activity, CompleteTripActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_DRIVER_VEHICLE, driverAndVehicle);
        return intent;
    }

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_completed);
        setupToolbar();

        String requestId = getIntent().getStringExtra(EXTRA_REQUEST_ID);
        DriverVehicleViewModel viewModel = getIntent().getParcelableExtra(EXTRA_DRIVER_VEHICLE);
        addFragment(R.id.container, CompleteTripFragment.newInstance(requestId, viewModel));
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        Intent homeIntent = HomeRouter.getHomeActivity(getApplicationContext());
        Intent destination = new Intent(this, RideHomeActivity.class);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(destination);
        taskStackBuilder.startActivities();
        finish();
    }
}