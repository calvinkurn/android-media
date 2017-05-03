package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.fragment.ApplyPromoFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;

public class ApplyPromoActivity extends BaseActivity {
    private static final String EXTRA_CONFIRM_BOOKING = "EXTRA_CONFIRM_BOOKING";

    public static Intent getCallingActivity(Activity activity,
                                            ConfirmBookingViewModel confirmBookingViewModel) {
        Intent intent = new Intent(activity, ApplyPromoActivity.class);
        intent.putExtra(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        return intent;
    }

    BackButtonListener backButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_promo);
        setupToolbar();
        ConfirmBookingViewModel confirmBookingViewModel = getIntent().getExtras().getParcelable(EXTRA_CONFIRM_BOOKING);
        ApplyPromoFragment fragment = ApplyPromoFragment.newInstance(confirmBookingViewModel);
        backButtonListener = fragment.getBackButtonListener();
        replaceFragment(R.id.fl_container, fragment);
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
            mToolbar.setTitle(R.string.apply_promo_toolbar_title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
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
        if (backButtonListener != null) {
            ConfirmBookingViewModel confirmBookingViewModel = backButtonListener.getConfirmParam();
            Intent intent = getIntent();
            intent.putExtra(ConfirmBookingViewModel.EXTRA_CONFIRM_PARAM, confirmBookingViewModel);
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    public interface BackButtonListener {
        ConfirmBookingViewModel getConfirmParam();
    }
}
