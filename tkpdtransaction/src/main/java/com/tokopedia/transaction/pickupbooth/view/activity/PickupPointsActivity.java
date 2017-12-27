package com.tokopedia.transaction.pickupbooth.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.transaction.pickupbooth.view.contract.PickupPointContract.Constant.INTENT_DATA_PARAMS;

public class PickupPointsActivity extends BaseActivity {

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

    public static Intent createInstance(Activity activity, HashMap<String, String> params) {
        Intent intent = new Intent(activity, PickupPointsActivity.class);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
