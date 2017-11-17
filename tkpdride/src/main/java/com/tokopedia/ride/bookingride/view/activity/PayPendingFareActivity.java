package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.view.PayPendingFareContract;
import com.tokopedia.ride.bookingride.view.PayPendingFarePresenter;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.GetPending;
import com.tokopedia.ride.scrooge.ScroogePGUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PayPendingFareActivity extends BaseActivity implements PayPendingFareContract.View, HasComponent<RideComponent> {
    private static final String KEY_GET_PENDING = "KEY_GET_PENDING";

    private Unbinder unbinder;
    private Toolbar mToolbar;

    @BindView(R2.id.tv_source)
    AppCompatTextView sourceTextView;
    @BindView(R2.id.tv_destination)
    AppCompatTextView destinationTextView;

    @BindView(R2.id.tv_total_fare_value)
    TextView totalFarevalue;
    @BindView(R2.id.tv_total_charged)
    TextView totalAmountCharged;
    @BindView(R2.id.tv_total_pending)
    TextView totalPendingTextView;
    @BindView(R2.id.tv_id)
    TextView requestIdView;
    @BindView(R2.id.tv_date)
    TextView dateView;
    @BindView(R2.id.label_total_charged)
    TextView labelAmountCharged;

    private GetPending getPending;
    RideComponent rideComponent;

    @Inject
    PayPendingFarePresenter mPresenter;
    private ProgressDialog progressDialog;

    public static Intent getCallingIntent(Activity activity, GetPending getPending) {
        Intent intent = new Intent(activity, PayPendingFareActivity.class);
        intent.putExtra(KEY_GET_PENDING, getPending);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pending_fare);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);

        if (getIntent() != null) {
            getPending = getIntent().getParcelableExtra(KEY_GET_PENDING);
        }

        setupToolbar();
        renderScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void executeInjector() {
        if (rideComponent == null) initInjector();
        BookingRideComponent component = DaggerBookingRideComponent.builder()
                .rideComponent(rideComponent)
                .build();
        component.inject(this);
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.toolbar_title_pending_fare);
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

    private void renderScreen() {
        if (getPending != null) {
            totalAmountCharged.setText(getPending.getLastRidePayment());
            totalFarevalue.setText(getPending.getLastRideAmount());
            totalPendingTextView.setText(getPending.getPendingAmountFormatted());
            sourceTextView.setText(getPending.getPickupAddressName());
            destinationTextView.setText(getPending.getDestinationAddressName());
            requestIdView.setText(getPending.getLastRequestId());
            dateView.setText(getPending.getDate());
            labelAmountCharged.setText(getPending.getPaymentMethod() + " " + getString(R.string.label_charged));
        }
    }

    @OnClick(R2.id.btn_pay_pending_fare)
    public void actionPayPendingFare() {
        mPresenter.payPendingFare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mPresenter.detachView();
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null) initInjector();
        return rideComponent;
    }

    @Override
    public void opeScroogePage(String url, String postData) {
        ScroogePGUtil.openScroogePage(this, url, true, postData, getString(R.string.title_pay_pending_fare));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showErrorMessage(String error) {
        NetworkErrorHelper.showCloseSnackbar(this, error);
    }

    @Override
    public void showProgressbar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.message_please_wait));
            progressDialog.setCancelable(false);
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressbar() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE){
            if(resultCode == ScroogePGUtil.RESULT_CODE_SUCCESS){
                hideProgressbar();
                finish();
            }else{
                NetworkErrorHelper.showCloseSnackbar(this , getString(R.string.error_fail_pay_pending));
            }
        }
    }
}