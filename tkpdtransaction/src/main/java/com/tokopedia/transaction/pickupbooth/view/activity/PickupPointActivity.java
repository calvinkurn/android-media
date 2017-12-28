package com.tokopedia.transaction.pickupbooth.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickupbooth.di.DaggerPickupPointComponent;
import com.tokopedia.transaction.pickupbooth.di.PickupPointComponent;
import com.tokopedia.transaction.pickupbooth.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract.Constant.INTENT_DATA_PARAMS;

public class PickupPointActivity extends BaseActivity implements PickupPointContract.View {

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

    @Inject
    PickupPointContract.Presenter presenter;

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
    public void showNoConnection(@NonNull String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), networkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getPickupPoints(searchViewPickupBooth.getQuery().toString());
                    }
                });
    }
}
