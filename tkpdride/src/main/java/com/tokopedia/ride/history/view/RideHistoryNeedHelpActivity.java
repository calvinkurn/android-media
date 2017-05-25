package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

public class RideHistoryNeedHelpActivity extends BaseActivity implements RideHistoryNeedHelpFragment.OnFragmentInteractionListener {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private RideHistoryViewModel rideHistory;

    public static Intent getCallingIntent(Activity activity, RideHistoryViewModel rideHistory) {
        Intent intent = new Intent(activity, RideHistoryNeedHelpActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, rideHistory);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_need_help);
        setupToolbar();
        rideHistory = (RideHistoryViewModel) getIntent().getParcelableExtra(EXTRA_REQUEST_ID);
        replaceFragment(R.id.fl_container, RideHistoryNeedHelpFragment.newInstance(rideHistory));
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.help_toolbar_title));
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

    private void addFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
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
    public void showTokoCashBillingFragment() {
        replaceFragment(R.id.fl_container, RideTokocashBillingHelpFragment.newInstance(rideHistory));
    }

    @Override
    public void rideHelpButtonClicked() {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentById(R.id.fl_container) instanceof RideTokocashBillingHelpFragment) {
            replaceFragment(R.id.fl_container, RideHistoryNeedHelpFragment.newInstance(rideHistory));
        } else {
            super.onBackPressed();
        }
    }
}
