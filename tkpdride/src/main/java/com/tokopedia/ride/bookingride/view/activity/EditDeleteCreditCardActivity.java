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
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.bookingride.view.fragment.EditDeleteCreditCardFragment;
import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.ontrip.view.OnTripActivity;

/**
 * Created by sandeepgoyal on 28/09/17.
 */

public class EditDeleteCreditCardActivity extends BaseActivity implements HasComponent<RideComponent> {
    private RideComponent rideComponent;
    private BackButtonListener backButtonListener;

    public static final String KEY_PAYMENT_METHOD_VIEW_MODEL = "CardDetails";

    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();
    }

    public static Intent getCallingActivity(Activity activity, PaymentMethodViewModel paymentMethodViewModel) {
        Intent intent = new Intent(activity, EditDeleteCreditCardActivity.class);
        intent.putExtra(KEY_PAYMENT_METHOD_VIEW_MODEL, paymentMethodViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_credit_card);

        initInjector();

        PaymentMethodViewModel paymentMethodViewModel = (PaymentMethodViewModel) getIntent().getParcelableExtra(KEY_PAYMENT_METHOD_VIEW_MODEL);
        setupToolbar(paymentMethodViewModel.getType().equalsIgnoreCase(PaymentMode.WALLET) ? getString(R.string.title_tokocash) : getString(R.string.credit_card));
        EditDeleteCreditCardFragment fragment = EditDeleteCreditCardFragment.newInstance(paymentMethodViewModel);
        backButtonListener = fragment.getBackButtonListener();
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

    private void setupToolbar(String title) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
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

    @Override
    public void onBackPressed() {
        if (backButtonListener != null && backButtonListener.canGoBack()) {
            backButtonListener.onBackPressed();
        } else {
            finish();
        }
    }
}
