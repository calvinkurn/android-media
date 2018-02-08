package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.fragment.ManagePaymentOptionsFragment;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;

public class ManagePaymentOptionsActivity extends BaseActivity implements HasComponent<RideComponent> {
    private static final String KEY_TYPE = "KEY_TYPE";
    public static final int TYPE_MANAGE_PAYMENT_OPTION = 1;
    public static final int TYPE_CHANGE_PAYMENT_OPTION = 2;

    private RideComponent rideComponent;
    private int type;

    public static Intent getCallingActivity(Activity activity, int type) {
        Intent intent = new Intent(activity, ManagePaymentOptionsActivity.class);
        intent.putExtra(KEY_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment_options);

        initInjector();
        //executeInjector();

        type = -1;
        if (getIntent() != null) {
            type = getIntent().getIntExtra(KEY_TYPE, 0);
        }

        setupToolbar();
        ManagePaymentOptionsFragment fragment = ManagePaymentOptionsFragment.newInstance(type);
        replaceFragment(R.id.fl_container, fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        }
        return true;
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
            mToolbar.setTitle(type == TYPE_MANAGE_PAYMENT_OPTION ? R.string.payment_toolbar_title : R.string.change_payment_toolbar_title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null)
            initInjector();
        return rideComponent;
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }
}
