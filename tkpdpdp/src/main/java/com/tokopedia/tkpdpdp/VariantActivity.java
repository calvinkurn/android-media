package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.VariantDatum;
import com.tokopedia.tkpdpdp.adapter.VariantOptionAdapter;
import com.tokopedia.tkpdpdp.adapter.VariantParentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VariantActivity extends TActivity {

    public static final String KEY_VARIANT_DATA = "VARIANT_DATA";

    //private RecyclerView parentRecyclerView;
    private TextView topBarTitle;
    private ProductVariant variant;
   // private VariantParentAdapter parentAdapter;
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variant = getIntent().getParcelableExtra(KEY_VARIANT_DATA);
        setContentView(R.layout.activity_variant);
        hideToolbar();
        initView();
        setupData();
        //setupRecyclerView();
        setupTopbar();
    }


    private void initView() {
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        //parentRecyclerView = (RecyclerView) findViewById(R.id.variant_parent_list);
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        productName = (TextView) itemView.findViewById(R.id.variant_product_name);
        productPrice = (TextView) itemView.findViewById(R.id.variant_product_price);
        productImage = (ImageView) itemView.findViewById(R.id.variant_image_title);
    }

    public void setupData() {

    }

    private void setupAdapter() {
        //parentAdapter = new VariantParentAdapter(this, variant);
    }

    private void setupRecyclerView() {
        //parentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //parentRecyclerView.setAdapter(parentAdapter);
    }

    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.product_variant));
    }

}
