package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.ride.R;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

public class RideHistoryDetailActivity extends BaseActivity implements RideHistoryDetailFragment.OnFragmentInteractionListener, FragmentGeneralWebView.OnFragmentInteractionListener {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private RideHistoryViewModel rideHistory;

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
        rideHistory = (RideHistoryViewModel) getIntent().getParcelableExtra(EXTRA_REQUEST_ID);
        replaceFragment(R.id.fl_container, RideHistoryDetailFragment.newInstance(rideHistory));
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

    public void setTitle(int resId) {
        getSupportActionBar().setTitle(resId);
    }

    @Override
    public void showHelpWebview(String helpUrl) {
        setTitle(R.string.title_ride);
        replaceFragment(R.id.fl_container, FragmentGeneralWebView.createInstance(helpUrl));
    }

    @Override
    public void onBackPressed() {
        if ((getFragmentManager().findFragmentById(R.id.fl_container) instanceof RideTokocashBillingHelpFragment)
                || (getFragmentManager().findFragmentById(R.id.fl_container) instanceof FragmentGeneralWebView)) {
            setTitle(R.string.help_toolbar_title);
            replaceFragment(R.id.fl_container, RideHistoryDetailFragment.newInstance(rideHistory));
        } else {
            super.onBackPressed();
        }
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
}
