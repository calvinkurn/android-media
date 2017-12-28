package com.tokopedia.ride.ontrip.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.ontrip.di.DaggerOnTripComponent;
import com.tokopedia.ride.ontrip.di.OnTripComponent;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.view.adapter.CancelReasonAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

public class SendCancelReasonActivity extends BaseActivity implements SendCancelReasonContract.View,
        CancelReasonAdapter.OnItemClickListener, HasComponent<RideComponent> {
    private static final String DATE_SERVER_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_CANCELLATION_TIMESTAMP = "EXTRA_CANCELLATION_TIMESTAMP";

    @Inject
    SendCancelReasonPresenter presenter;

    private CancelReasonAdapter adapter;
    private List<String> reasons;

    private ProgressBar progressBar;
    private RelativeLayout mainLayout;
    private RecyclerView reasonsRecyclerView;
    private TextView submitButtonTextView;
    private LinearLayout cancellationLayout;
    private TextView cancellationTextView;
    private Toolbar toolbar;
    private String selectedReason;
    private String requestId;
    private RideComponent rideComponent;

    public static Intent getCallingIntent(Activity activity, String requestId, String timestamp) {
        Intent intent = new Intent(activity, SendCancelReasonActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_CANCELLATION_TIMESTAMP, timestamp);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity, String requestId) {
        Intent intent = new Intent(activity, SendCancelReasonActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_cancel_reason);
        initInjector();
        requestId = getIntent().getStringExtra(EXTRA_REQUEST_ID);
        setupUiVariable();
        setupViewListener();
        presenter.attachView(this);
        presenter.initialize();
    };

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_CANCEL_REASON;
    }

    private void setupViewListener() {
        submitButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submitReasons();
            }
        });
        if (TextUtils.isEmpty(selectedReason)) {
            disableSubmitButton();
        } else {
            enableSubmitButton();
        }

        if (toolbar != null) {
            toolbar.setTitle(R.string.send_cancel_reason_toolbar_title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }

        if (getIntent().hasExtra(EXTRA_CANCELLATION_TIMESTAMP) && !TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_CANCELLATION_TIMESTAMP))) {
            SimpleDateFormat serverDateFormatter = new SimpleDateFormat(DATE_SERVER_FORMAT, Locale.US);
            Date date = null;
            try {
                date = serverDateFormatter.parse(getIntent().getStringExtra(EXTRA_CANCELLATION_TIMESTAMP));
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));

            long delta = date.getTime() - now.getTimeInMillis();
            new CountDownTimer(delta, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!SendCancelReasonActivity.this.isDestroyed()) {
                            showCancellationLayout();
                        }
                    } else {
                        showCancellationLayout();
                    }
                }
            }.start();
        }
    }

    private void showCancellationLayout() {
        cancellationLayout.setVisibility(View.VISIBLE);
        RideConfiguration rideConfiguration = new RideConfiguration(this);
        cancellationTextView.setText(rideConfiguration.getActiveCancellationFee());
    }

    private void setupUiVariable() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        reasonsRecyclerView = (RecyclerView) findViewById(R.id.rv_reasons);
        submitButtonTextView = (TextView) findViewById(R.id.tv_submit_reason);
        cancellationLayout = (LinearLayout) findViewById(R.id.cancellation_charges_layout);
        cancellationTextView = (TextView) findViewById(R.id.tv_cancellation_charges);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public RequestParams getReasonsParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderReasons(List<String> reasons) {
        this.reasons = reasons;
        adapter = new CancelReasonAdapter(this);

        adapter.setOnItemClickListener(this);
        reasonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reasonsRecyclerView.setAdapter(adapter);
        adapter.setReasons(reasons);
    }

    @Override
    public void hideMainLayout() {
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMainLayout() {
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public RequestParams getCancelParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CancelRideRequestUseCase.PARAM_REQUEST_ID, requestId);
        requestParams.putString(CancelRideRequestUseCase.PARAM_REASON, selectedReason);
        return requestParams;
    }

    @Override
    public void showErrorCancelRequest() {
        NetworkErrorHelper.showEmptyState(this, mainLayout, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.submitReasons();
            }
        });
    }

    @Override
    public void onSuccessCancelRequest() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public String getSelectedReason() {
        return selectedReason;
    }

    @Override
    public void showReasonEmptyError() {
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.send_cancel_selected_reason_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClicked(String reason) {
        RideGATracking.eventClickCancelReason(getScreenName(),reason);  //23
        adapter.setSelectedReason(reason);
        adapter.setReasons(this.reasons);
        this.selectedReason = reason;
        enableSubmitButton();
    }

    private void enableSubmitButton() {
        submitButtonTextView.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            submitButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
        } else {
            submitButtonTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
        }
    }


    private void disableSubmitButton() {
        submitButtonTextView.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            submitButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        } else {
            submitButtonTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
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
        super.onBackPressed();
        RideGATracking.eventBackPress(getScreenName());
    }

    @Override
    public void showErrorGetReasons() {
        NetworkErrorHelper.showEmptyState(this, mainLayout, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.initialize();
            }
        });
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null) initInjector();
        return rideComponent;
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
        OnTripComponent component = DaggerOnTripComponent.builder()
                .rideComponent(rideComponent)
                .build();
        component.inject(this);
    }
}
