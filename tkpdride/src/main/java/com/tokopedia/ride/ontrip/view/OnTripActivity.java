package com.tokopedia.ride.ontrip.view;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.ontrip.view.fragment.OnTripMapFragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class OnTripActivity extends BaseActivity implements OnTripMapFragment.OnFragmentInteractionListener {
    public static String EXTRA_CONFIRM_BOOKING = "EXTRA_CONFIRM_BOOKING";
    ConfirmBookingViewModel confirmBookingViewModel;
    Toolbar mToolbar;
    public static Intent getCallingIntent(Activity activity, ConfirmBookingViewModel confirmBookingViewModel){
        Intent intent = new Intent(activity, OnTripActivity.class);
        intent.putExtra(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        return intent;
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
    public void initFragment(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        addFragment(R.id.container, OnTripMapFragment.newInstance(bundle));
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

    }
}
