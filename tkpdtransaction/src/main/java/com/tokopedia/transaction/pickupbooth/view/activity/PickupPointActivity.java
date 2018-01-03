package com.tokopedia.transaction.pickupbooth.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickupbooth.di.DaggerPickupPointComponent;
import com.tokopedia.transaction.pickupbooth.di.PickupPointComponent;
import com.tokopedia.transaction.pickupbooth.domain.model.Store;
import com.tokopedia.transaction.pickupbooth.view.adapter.PickupPointAdapter;
import com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract;
import com.tokopedia.transaction.pickupbooth.view.model.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract.Constant.INTENT_DATA_PARAMS;
import static com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

public class PickupPointActivity extends BaseActivity
        implements PickupPointContract.View, PickupPointAdapter.Listener {

    private static final int REQUEST_CODE_MAP = 100;

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
    @BindView(R2.id.btn_choose_pickup_booth)
    Button btnChoosePickupBooth;
    @BindView(R2.id.tv_expanded_title)
    TextView tvExpandedTitle;

    @Inject
    PickupPointContract.Presenter presenter;

    private Store selectedPickupBooth;
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

        setupToolbar();

        HashMap<String, String> params = (HashMap<String, String>) getIntent().getSerializableExtra(INTENT_DATA_PARAMS);

        setupSearchView(params);

        initializeInjector();

        presenter.attachView(this);

        setupRecycleView();

        doQuery(params);
    }

    private void setupSearchView(final HashMap<String, String> params) {
        searchViewPickupBooth.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doQuery(params);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doQuery(params);
                return true;
            }
        });

        searchViewPickupBooth.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                resetSearchResult();
                return true;
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(
                    com.tokopedia.core.R.drawable.ic_webview_back_button
            );

            appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float positiveVerticalOffset = verticalOffset * -1.0f;
                    int color = Color.argb((int) positiveVerticalOffset, 255, 255, 255);
                    toolbarLayout.setExpandedTitleColor(color);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        llHeader.requestFocus();
    }

    private void doQuery(HashMap<String, String> param) {
        llEmptyResult.setVisibility(View.GONE);
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
    public void showAllResult() {
        pickupPointAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchResult(ArrayList<StoreViewModel> storeViewModels) {
        pickupPointAdapter = new PickupPointAdapter(storeViewModels, this);
        rvPickupBooth.setAdapter(pickupPointAdapter);
        if (storeViewModels.size() == 0) {
            showNoResult();
        }
    }

    private void resetSearchResult() {
        llEmptyResult.setVisibility(View.GONE);
        setupRecycleView();
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
    public void onItemClick(Store store, boolean selected) {
        if (selected) {
            selectedPickupBooth = store;
            btnChoosePickupBooth.setEnabled(true);
            btnChoosePickupBooth.setBackgroundColor(ContextCompat.getColor(this, R.color.tkpd_main_green));
        } else {
            selectedPickupBooth = null;
            btnChoosePickupBooth.setEnabled(false);
            btnChoosePickupBooth.setBackgroundColor(ContextCompat.getColor(this, R.color.xco_button_color_disable));
        }
    }

    @Override
    public void onItemShowMapClick(Store store) {
        startActivityForResult(PickupPointMapActivity.createInstance(this, store), REQUEST_CODE_MAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
            setResult(resultCode, data);
            finish();
        }
    }

    @OnClick(R2.id.btn_choose_pickup_booth)
    public void onChoosePickupBooth() {
        if (selectedPickupBooth != null) {
            Intent intent = new Intent();
            intent.putExtra(INTENT_DATA_STORE, selectedPickupBooth);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}