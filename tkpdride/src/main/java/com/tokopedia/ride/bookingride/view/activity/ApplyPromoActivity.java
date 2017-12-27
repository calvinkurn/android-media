package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.bookingride.view.fragment.ApplyPromoFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;

public class ApplyPromoActivity extends BaseActivity implements ApplyPromoFragment.OnFragmentInteractionListener, HasComponent<RideComponent> {
    private static final String EXTRA_CONFIRM_BOOKING = "EXTRA_CONFIRM_BOOKING";
    private RideComponent rideComponent;
    private ConfirmBookingViewModel confirmBookingViewModel;

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
        confirmBookingViewModel = getIntent().getExtras().getParcelable(EXTRA_CONFIRM_BOOKING);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apply_promo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.action_remove) {
            RideGATracking.eventDeletePromotion(getScreenName(),confirmBookingViewModel.getPromoCode());
            confirmBookingViewModel.setPromoCode("");
            confirmBookingViewModel.setPromoDescription("");

            //set result back
            Intent intent = getIntent();
            intent.putExtra(ConfirmBookingViewModel.EXTRA_CONFIRM_PARAM, confirmBookingViewModel);
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_APPLYPROMO;
    }

    @Override
    public void onBackPressed() {
        RideGATracking.eventBackPress(getScreenName());
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

    @Override
    public void openWebView(String url) {
        String seamlessURL = URLGenerator.generateURLSessionLogin(
                (Uri.encode(url)),
                this
        );
        Intent intent = RideWebViewActivity.getCallingIntent(this, seamlessURL);
        startActivity(intent);
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

    public interface BackButtonListener {
        ConfirmBookingViewModel getConfirmParam();
    }
}
