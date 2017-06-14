package com.tokopedia.ride.ontrip.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.ride.R;
import com.tokopedia.ride.ontrip.di.OnTripDependencyInjection;
import com.tokopedia.ride.ontrip.view.adapter.CancelReasonAdapter;

import java.util.List;

public class SendCancelReasonActivity extends BaseActivity implements SendCancelReasonContract.View, CancelReasonAdapter.OnItemClickListener {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    private SendCancelReasonPresenter presenter;
    private CancelReasonAdapter adapter;
    private List<String> reasons;

    private ProgressBar progressBar;
    private RelativeLayout mainLayout;
    private RecyclerView reasonsRecyclerView;
    private TextView submitButtonTextView;
    private Toolbar toolbar;
    private String selectedReason;

    public static Intent getCallingIntent(Activity activity, String requestId) {
        Intent intent = new Intent(activity, SendCancelReasonActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_cancel_reason);
        setupUiVariable();
        setupViewListener();
        presenter = OnTripDependencyInjection.createSendCancelReasonPresenter(this);
        presenter.attachView(this);
        presenter.initialize();
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            invalidateOptionsMenu();
        }
    }

    private void setupUiVariable() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        reasonsRecyclerView = (RecyclerView) findViewById(R.id.rv_reasons);
        submitButtonTextView = (TextView) findViewById(R.id.tv_submit_reason);
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
    public void onItemClicked(String reason) {
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
}
