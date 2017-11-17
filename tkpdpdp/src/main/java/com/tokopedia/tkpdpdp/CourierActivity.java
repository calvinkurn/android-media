package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.productdetail.ShopShipment;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.tkpdpdp.adapter.CourierAdapter;

import java.util.ArrayList;

/**
 * @author by HenryPri on 12/05/17.
 */

public class CourierActivity extends TActivity {

    public static final String KEY_COURIER_DATA = "courierData";

    private RecyclerView recyclerView;

    private TextView topBarTitle;
    private CourierAdapter courierAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        hideToolbar();
        initView();
        setupTopbar();
        setupAdapter();
        setupRecyclerView();
        showCourierData();
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

    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.courier_page_title));
    }

    private void setupAdapter() {
        courierAdapter = new CourierAdapter(this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(courierAdapter);
        DividerItemDecoration itemDecoration
                = new DividerItemDecoration(CourierActivity.this, R.drawable.divider300);

        recyclerView.addItemDecoration(itemDecoration);
    }

    public void showCourierData() {
        ArrayList<ShopShipment> shopShipments
                = getIntent().getParcelableArrayListExtra(KEY_COURIER_DATA);
        courierAdapter.setData(shopShipments);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
