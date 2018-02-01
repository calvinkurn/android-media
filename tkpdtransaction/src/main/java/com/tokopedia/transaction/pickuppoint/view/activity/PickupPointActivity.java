package com.tokopedia.transaction.pickuppoint.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.pickuppoint.di.DaggerPickupPointComponent;
import com.tokopedia.transaction.pickuppoint.di.PickupPointComponent;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.adapter.PickupPointAdapter;
import com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract;
import com.tokopedia.transaction.pickuppoint.view.model.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_CART_ITEM;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_POSITION;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DISTRICT_NAME;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_REQ_PARAMS;

public class PickupPointActivity extends BaseActivity
        implements PickupPointContract.View, PickupPointAdapter.Listener {

    private static final int REQUEST_CODE_MAP = 100;
    private static final int CONTENT_PADDING_BOTTOM = 80;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R2.id.app_bar)
    AppBarLayout appBar;
    @BindView(R2.id.nested_scroll_view)
    NestedScrollView contentScrollView;
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

    public static Intent createInstance(Activity activity, int cartPosition,
                                        String districtName, HashMap<String, String> params) {
        Intent intent = new Intent(activity, PickupPointActivity.class);
        intent.putExtra(INTENT_REQ_PARAMS, params);
        intent.putExtra(INTENT_DISTRICT_NAME, districtName);
        intent.putExtra(INTENT_DATA_POSITION, cartPosition);
        return intent;
    }

    public static Intent createInstance(Activity activity, String districtName, HashMap<String, String> params) {
        Intent intent = new Intent(activity, PickupPointActivity.class);
        intent.putExtra(INTENT_REQ_PARAMS, params);
        intent.putExtra(INTENT_DISTRICT_NAME, districtName);
        return intent;
    }

    public static Intent createInstance(Activity activity, CartItem cartItem, HashMap<String, String> params) {
        Intent intent = new Intent(activity, PickupPointActivity.class);
        intent.putExtra(INTENT_REQ_PARAMS, params);
        intent.putExtra(INTENT_DISTRICT_NAME, cartItem.getCartDestination().getAddressDistrict());
        intent.putExtra(INTENT_CART_ITEM, cartItem);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_point);
        ButterKnife.bind(this);

        setupToolbar();

        setupPickupPointSpinner();

        HashMap<String, String> queryParams = (HashMap<String, String>) getIntent().getSerializableExtra(INTENT_REQ_PARAMS);
        setupSearchView(queryParams);

        initializeInjector();

        presenter.attachView(this);

        setupRecycleView();

        doQuery(queryParams);
    }

    private void setupPickupPointSpinner() {
        ArrayAdapter<String> pickupPointAdapter = new ArrayAdapter<>(this,
                R.layout.item_pickup_point_spinner, getResources().getStringArray(R.array.pickup_point));
        pickupPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickupBooth.setAdapter(pickupPointAdapter);
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
            String districtName = String.format(getString(R.string.title_send_to_pick_up_booth),
                    getIntent().getStringExtra(INTENT_DISTRICT_NAME));
            tvExpandedTitle.setText(districtName);
            getSupportActionBar().setTitle(districtName);

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
        setContentPaddingVisibility(false);
        btnChoosePickupBooth.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), networkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        setContentPaddingVisibility(true);
                        btnChoosePickupBooth.setVisibility(View.VISIBLE);
                        doQuery((HashMap<String, String>) getIntent().getSerializableExtra(INTENT_REQ_PARAMS));
                    }
                });
    }

    private void setContentPaddingVisibility(boolean visible) {
        if (visible) {
            int paddingInDp = (int) (CONTENT_PADDING_BOTTOM * getResources().getDisplayMetrics().density);
            contentScrollView.setPadding(0, 0, 0, paddingInDp);
        } else {
            contentScrollView.setPadding(0, 0, 0, 0);
        }
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
            btnChoosePickupBooth.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_green_enabled));
        } else {
            selectedPickupBooth = null;
            btnChoosePickupBooth.setEnabled(false);
            btnChoosePickupBooth.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_disabled));
        }
    }

    @Override
    public void onItemShowMapClick(Store store) {
        startActivityForResult(PickupPointMapActivity.createInstance(this,
                getIntent().getStringExtra(INTENT_DISTRICT_NAME), store), REQUEST_CODE_MAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
            if (getIntent().getIntExtra(INTENT_DATA_POSITION, 0) != 0) {
                data.putExtra(INTENT_DATA_POSITION, getIntent().getIntExtra(INTENT_DATA_POSITION, 0));
            }
            setResult(resultCode, data);
            finish();
        }
    }

    @OnClick(R2.id.btn_choose_pickup_booth)
    public void onChoosePickupBooth() {
        if (selectedPickupBooth != null) {
            Intent intent = new Intent();
            intent.putExtra(INTENT_DATA_POSITION, getIntent().getIntExtra(INTENT_DATA_POSITION, 0));
            intent.putExtra(INTENT_DATA_STORE, selectedPickupBooth);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}