package com.tokopedia.ride.completetrip.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.completetrip.view.viewmodel.TokoCashProduct;

import java.util.ArrayList;
import java.util.List;

public class PendingFareChooserActivity extends BaseActivity implements TokoCashProductAdapter.OnActionProductAdapterListener {
    private static final String EXTRA_PRODUCTS = "EXTRA_PRODUCTS";
    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    RecyclerView listsRecyclerView;
    private List<TokoCashProduct> products;
    private TokoCashProductAdapter adapter;

    public static Intent getCallingIntent(Activity activity, List<TokoCashProduct> products) {
        Intent intent = new Intent(activity, PendingFareChooserActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_PRODUCTS, (ArrayList<? extends Parcelable>) products);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_fare_chooser);

        listsRecyclerView = (RecyclerView) findViewById(R.id.rv_lists);
        products = getIntent().getParcelableArrayListExtra(EXTRA_PRODUCTS);
        setupToolbar();
        adapter = new TokoCashProductAdapter(this);
        adapter.setInteractionListener(PendingFareChooserActivity.this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PendingFareChooserActivity.this);
        listsRecyclerView.setLayoutManager(layoutManager);
        listsRecyclerView.setHasFixedSize(true);
        listsRecyclerView.setNestedScrollingEnabled(true);
        listsRecyclerView.setAdapter(adapter);
        adapter.setProducts(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(TokoCashProduct product) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_PRODUCT, product);
        setResult(RESULT_OK);
        finish();
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.pending_fare_choose_nominal));
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
}
