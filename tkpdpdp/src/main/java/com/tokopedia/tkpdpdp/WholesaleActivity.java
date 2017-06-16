package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.tkpdpdp.adapter.WholesaleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angga.Prasetiyo on 02/11/2015.
 */

public class WholesaleActivity extends TActivity {
    public static final String KEY_WHOLESALE_DATA = "WHOLESALE_DATA";

    private RecyclerView recyclerView;
    private TextView topBarTitle;

    List<ProductWholesalePrice> wholesalePrices = new ArrayList<>();

    private WholesaleAdapter wholesaleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        hideToolbar();
        initView();
        setupAdapter();
        setupRecyclerView();
        showCourierData();
        setupTopbar();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.courier_list);
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    private void setupAdapter() {
        wholesaleAdapter = new WholesaleAdapter(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(wholesaleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(WholesaleActivity.this, R.drawable.divider300));
    }

    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.wholesale_page_title));
    }

    public void showCourierData() {
        ArrayList<ProductWholesalePrice> productWholesalePrices =
                getIntent().getParcelableArrayListExtra(KEY_WHOLESALE_DATA);
        wholesaleAdapter.setData(productWholesalePrices);
    }
}
