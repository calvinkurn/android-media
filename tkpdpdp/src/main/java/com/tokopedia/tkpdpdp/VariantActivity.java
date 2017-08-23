package com.tokopedia.tkpdpdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.VariantDatum;
import com.tokopedia.core.network.entity.variant.VariantOption;
import com.tokopedia.tkpdpdp.adapter.VariantOptionAdapter;
import com.tokopedia.tkpdpdp.adapter.VariantParentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tokopedia.core.network.entity.variant.VariantOption.IDENTIFIER_COLOUR;

public class VariantActivity extends TActivity  implements VariantOptionAdapter.OnVariantOptionChoosedListener  {

    public static final String KEY_VARIANT_DATA = "VARIANT_DATA";
    public static final String KEY_LEVEL1_SELECTED= "LEVEL1_OPTION";
    public static final String KEY_LEVEL2_SELECTED= "LEVEL2_OPTION";
    public static final int SELECTED_VARIANT_RESULT = 99;

    private TextView topBarTitle;
    private ProductVariant variant;
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private RecyclerView optionRecyclerViewLevel1;
    private TextView optionNameLevel1;
    private RecyclerView optionRecyclerViewLevel2;
    private TextView optionNameLevel2;
    private FrameLayout buttonSave;

    private VariantOptionAdapter variantOptionAdapterLevel2;
    private VariantOptionAdapter variantOptionAdapterLevel1;

    private Option level1Selected;
    private Option level2Selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variant = getIntent().getParcelableExtra(KEY_VARIANT_DATA);
        level1Selected = getIntent().getParcelableExtra(KEY_LEVEL1_SELECTED);
        level2Selected = getIntent().getParcelableExtra(KEY_LEVEL2_SELECTED);
        setContentView(R.layout.activity_variant);
        hideToolbar();
        initView();
        initData();
        setupTopbar();
    }


    private void initView() {
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                    }
                });
        productName = (TextView) findViewById(R.id.variant_product_name);
        productPrice = (TextView) findViewById(R.id.variant_product_price);
        productImage = (ImageView) findViewById(R.id.variant_image_title);
        optionNameLevel1 = (TextView) findViewById(R.id.text_variant_option_level1);
        optionRecyclerViewLevel1 = (RecyclerView) findViewById(R.id.rv_variant_option_level1);
        optionNameLevel2 = (TextView) findViewById(R.id.text_variant_option_level2);
        optionRecyclerViewLevel2 = (RecyclerView) findViewById(R.id.rv_variant_option_level2);
        buttonSave = (FrameLayout) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(KEY_LEVEL1_SELECTED,level1Selected);
                if (level1Selected!=null)  intent.putExtra(KEY_LEVEL2_SELECTED,level2Selected);
                setResult(VariantActivity.SELECTED_VARIANT_RESULT, intent);
                finish();
                VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
            }
        });
    }

    public void initData() {
        productName.setText(variant.getProductName());
        productPrice.setText(variant.getProductPrice());
        ImageHandler.LoadImage(productImage,variant.getProductImageUrl());

        VariantOption optionLevel1 = variant.getVariantOption().get(variant.getLevel1Variant());

        optionNameLevel1.setText(variant.getVariantOption().get(0).getName()+" :");
        variantOptionAdapterLevel1 = new VariantOptionAdapter(VariantActivity.this,optionLevel1.getOption(),
                TextUtils.equals(IDENTIFIER_COLOUR,optionLevel1.getIdentifier()), VariantActivity.this, 1);
        ChipsLayoutManager chipsLayoutManager= ChipsLayoutManager.newBuilder(VariantActivity.this)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        optionRecyclerViewLevel1.setNestedScrollingEnabled(false);
        optionRecyclerViewLevel1.setLayoutManager(chipsLayoutManager);
        optionRecyclerViewLevel1.setAdapter(variantOptionAdapterLevel1);
        optionNameLevel1.setText(optionLevel1.getName()+" :");
        if (level1Selected!=null) {
            for (int i=0; i<variantOptionAdapterLevel1.getVariantOptions().size(); i++) {
                if (level1Selected.getPvoId().equals(variantOptionAdapterLevel1.getVariantOptions().get(i).getPvoId())) {
                    variantOptionAdapterLevel1.setSelectedPosition(i);
                    break;
                }
            }
        } else {
            variantOptionAdapterLevel1.setSelectedPosition(0);
        }


        if (variant.getVariantOption().size()>1) {
            VariantOption optionLevel2 = variant.getVariantOption().get(1-variant.getLevel1Variant());
            variantOptionAdapterLevel2
                    = new VariantOptionAdapter(VariantActivity.this,optionLevel2.getOption(),
                    TextUtils.equals(IDENTIFIER_COLOUR,optionLevel2.getIdentifier()), VariantActivity.this, 2);

            ChipsLayoutManager chipsLayoutManagerLevel2= ChipsLayoutManager.newBuilder(VariantActivity.this)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build();
            optionRecyclerViewLevel2.setNestedScrollingEnabled(false);
            optionRecyclerViewLevel2.setLayoutManager(chipsLayoutManagerLevel2);
            optionRecyclerViewLevel2.setAdapter(variantOptionAdapterLevel2);
            optionNameLevel2.setText(optionLevel2.getName()+" :");
            onVariantChosen(optionLevel1.getOption().get(variantOptionAdapterLevel1.getSelectedPosition()),1);
            if (level2Selected!=null) {
                for (int i=0; i<variantOptionAdapterLevel2.getVariantOptions().size(); i++) {
                    if (level2Selected.getPvoId().equals(variantOptionAdapterLevel2.getVariantOptions().get(i).getPvoId())) {
                        variantOptionAdapterLevel2.setSelectedPosition(i);
                        break;
                    }
                }
            } else {
                variantOptionAdapterLevel2.setSelectedPosition(0);
            }
            onVariantChosen(optionLevel2.getOption().get(variantOptionAdapterLevel2.getSelectedPosition()),2);
        } else {
            optionNameLevel2.setVisibility(View.GONE);
            optionRecyclerViewLevel2.setVisibility(View.GONE);
        }

    }


    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.product_variant));
    }

    @Override
    public void onVariantChosen(Option option, int level) {
        List<Integer> combinations = variant.getCombinationFromSelectedVariant(option.getPvoId());
        if (level==1) {
            level1Selected = option;
            for (Option otherLevelOption: variantOptionAdapterLevel2.getVariantOptions()) {
                if (combinations.contains(otherLevelOption.getPvoId())) {
                    otherLevelOption.setEnabled(true);
                } else {
                    otherLevelOption.setEnabled(false);
                }
            }
            variantOptionAdapterLevel2.notifyDataSetChanged();
            onVariantChosen(variantOptionAdapterLevel2.getVariantOptions().get(variantOptionAdapterLevel2.getSelectedPosition()),2);
        } else {
            level2Selected = option;
            if (!combinations.contains(variantOptionAdapterLevel1.getVariantOptions().get(variantOptionAdapterLevel1.getSelectedPosition()).getPvoId())) {
                combinations = variant.getCombinationFromSelectedVariant(variantOptionAdapterLevel1.getVariantOptions()
                        .get(variantOptionAdapterLevel1.getSelectedPosition()).getPvoId());
                for (int i=0; i<variantOptionAdapterLevel2.getVariantOptions().size(); i++) {
                    if (combinations.contains(variantOptionAdapterLevel2.getVariantOptions().get(i).getPvoId())) {
                        variantOptionAdapterLevel2.setSelectedPosition(i);
                        variantOptionAdapterLevel2.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }
}
