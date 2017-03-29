package com.tokopedia.ride.ontrip.view;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.gcm.PeriodicTask;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.ontrip.service.GetCurrentRideRequestService;
import com.tokopedia.ride.ontrip.view.fragment.OnTripMapFragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class OnTripActivity extends BaseActivity implements OnTripMapFragment.OnFragmentInteractionListener {
    public static String EXTRA_CONFIRM_BOOKING = "EXTRA_CONFIRM_BOOKING";

    public static final String TASK_TAG_PERIODIC = "periodic_task";

    ConfirmBookingViewModel confirmBookingViewModel;
    Toolbar mToolbar;

    public static Intent getCallingIntent(Activity activity, ConfirmBookingViewModel confirmBookingViewModel) {
        Intent intent = new Intent(activity, OnTripActivity.class);
        intent.putExtra(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, OnTripActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_trip);
        confirmBookingViewModel = getIntent().getParcelableExtra(EXTRA_CONFIRM_BOOKING);
        OnTripActivityPermissionsDispatcher.initFragmentWithCheck(this);
        setupToolbar();
    }


    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void initFragment() {
        if (confirmBookingViewModel != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
            addFragment(R.id.container, OnTripMapFragment.newInstance(bundle));
        } else {
            addFragment(R.id.container, OnTripMapFragment.newInstance());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        OnTripActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void actionCancelBooking() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = HomeRouter.getHomeActivityInterfaceRouter(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
}
