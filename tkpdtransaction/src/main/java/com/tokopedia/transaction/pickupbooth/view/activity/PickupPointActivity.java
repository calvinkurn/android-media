package com.tokopedia.transaction.pickupbooth.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickupbooth.di.DaggerPickupPointComponent;
import com.tokopedia.transaction.pickupbooth.di.PickupPointComponent;
import com.tokopedia.transaction.pickupbooth.domain.model.Store;
import com.tokopedia.transaction.pickupbooth.view.adapter.PickupPointAdapter;
import com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract.Constant.INTENT_DATA_PARAMS;

public class PickupPointActivity extends BaseActivity
        implements PickupPointContract.View, PickupPointAdapter.Listener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R2.id.app_bar)
    AppBarLayout appBar;
    @BindView(R2.id.sp_pickup_booth)
    Spinner spPickupBooth;
    @BindView(R2.id.search_view_pickup_booth)
    SearchView searchViewPickupBooth;
    @BindView(R2.id.rv_pickup_booth)
    RecyclerView rvPickupBooth;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.network_error_view)
    LinearLayout networkErrorView;
    @BindView(R2.id.ll_empty_result)
    LinearLayout llEmptyResult;
    @BindView(R2.id.ll_header)
    LinearLayout llHeader;

    @Inject
    PickupPointContract.Presenter presenter;

    private PickupPointAdapter pickupPointAdapter;

    public static Intent createInstance(Activity activity, HashMap<String, String> params) {
        Intent intent = new Intent(activity, PickupPointActivity.class);
        intent.putExtra(INTENT_DATA_PARAMS, params);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_point);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(
                    com.tokopedia.core.R.drawable.ic_webview_back_button
            );
        }

        initializeInjector();
        presenter.attachView(this);
        setupRecycleView();

        doQuery((HashMap<String, String>) getIntent().getSerializableExtra(INTENT_DATA_PARAMS));
    }

    @Override
    protected void onResume() {
        super.onResume();
        llHeader.requestFocus();
    }

    private void doQuery(HashMap<String, String> param) {
        presenter.queryPickupPoints(searchViewPickupBooth.getQuery().toString(), param);
    }

    private void setupRecycleView() {
        pickupPointAdapter = new PickupPointAdapter(presenter.getPickupPoints(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvPickupBooth.setLayoutManager(linearLayoutManager);
        rvPickupBooth.setAdapter(pickupPointAdapter);
        ViewCompat.setNestedScrollingEnabled(rvPickupBooth, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void initializeInjector() {
        AppComponent component = getApplicationComponent();
        PickupPointComponent pickupPointComponent =
                DaggerPickupPointComponent.builder()
                        .appComponent(component)
                        .build();
        pickupPointComponent.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showResult() {
        pickupPointAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoConnection(@NonNull String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), networkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        doQuery((HashMap<String, String>) getIntent().getSerializableExtra(INTENT_DATA_PARAMS));
                    }
                });
    }

    @Override
    public void showNoResult() {
        llEmptyResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Store store) {

    }
}
