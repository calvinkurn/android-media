package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.CourierItem;
import com.tokopedia.core.product.model.productdetail.ShopShipment;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.tkpdpdp.adapter.CourierAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HenryPri on 12/05/17.
 */

public class CourierActivity extends TActivity {

    public static final String KEY_COURIER_DATA = "courierData";

    @BindView(R2.id.courier_list)
    RecyclerView recyclerView;

    @BindView(R2.id.simple_top_bar_title)
    TextView topBarTitle;

    List<CourierItem> courierItemList = new ArrayList<>();

    private CourierAdapter courierAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        hideToolbar();
        ButterKnife.bind(this);
        setupTopbar();
        setupAdapter();
        setupRecyclerView();
        showCourierData();
    }

    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.courier_page_title));
    }

    private void setupAdapter() {
        courierAdapter = new CourierAdapter(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(courierAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(CourierActivity.this, R.drawable.divider300));
    }

    @OnClick(R2.id.simple_top_bar_close_button)
    public void onCloseButtonClick() {
        finish();
    }

    public void showCourierData() {
        ArrayList<ShopShipment> shopShipments = getIntent().getParcelableArrayListExtra(KEY_COURIER_DATA);
        courierAdapter.setData(shopShipments);
    }
}
