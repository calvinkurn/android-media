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

public class RideHistoryNeedHelpActivity extends BaseActivity implements RideHistoryNeedHelpFragment.OnFragmentInteractionListener, FragmentGeneralWebView.OnFragmentInteractionListener {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String HELP_URL = "https://help.uber.com/h/d69a65cd-e466-43bc-900e-55e04cc9e656";

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
        setTitle(R.string.title_tokocash_billing);
        replaceFragment(R.id.fl_container, RideTokocashBillingHelpFragment.newInstance(rideHistory));
    }

    @Override
    public void rideHelpButtonClicked() {
        setTitle(R.string.title_ride);
        replaceFragment(R.id.fl_container, FragmentGeneralWebView.createInstance(HELP_URL));
    }

    public void setTitle(int resId) {
        getSupportActionBar().setTitle(resId);
    }

    @Override
    public void onBackPressed() {
        if ((getFragmentManager().findFragmentById(R.id.fl_container) instanceof RideTokocashBillingHelpFragment)
                || (getFragmentManager().findFragmentById(R.id.fl_container) instanceof FragmentGeneralWebView)) {
            setTitle(R.string.help_toolbar_title);
            replaceFragment(R.id.fl_container, RideHistoryNeedHelpFragment.newInstance(rideHistory));
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
