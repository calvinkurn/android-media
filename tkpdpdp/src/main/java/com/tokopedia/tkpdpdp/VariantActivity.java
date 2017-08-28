package com.tokopedia.tkpdpdp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.VariantDatum;
import com.tokopedia.core.network.entity.variant.VariantOption;
import com.tokopedia.tkpdpdp.adapter.VariantOptionAdapter;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;

import java.util.List;

import static com.tokopedia.core.network.entity.variant.VariantOption.IDENTIFIER_COLOUR;

public class VariantActivity extends TActivity  implements VariantOptionAdapter.OnVariantOptionChoosedListener  {

    public static final String KEY_VARIANT_DATA = "VARIANT_DATA";
    public static final String KEY_BUY_MODE = "ON_BUY_MODE";
    public static final String KEY_SELLER_MODE = "ON_SELLER_MODE";
    public static final String KEY_LEVEL1_SELECTED= "LEVEL1_OPTION";
    public static final String KEY_LEVEL2_SELECTED= "LEVEL2_OPTION";

    public static final int SELECTED_VARIANT_RESULT = 99;
    public static final int SELECTED_VARIANT_RESULT_TO_BUY = 98;
    public static final int KILL_PDP_BACKGROUND = 97;

    private TextView topBarTitle;
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private RecyclerView optionRecyclerViewLevel1;
    private TextView optionNameLevel1;
    private RecyclerView optionRecyclerViewLevel2;
    private TextView optionNameLevel2;
    private FrameLayout buttonSave;
    private TextView textButtonSave;

    private VariantOptionAdapter variantOptionAdapterLevel2;
    private VariantOptionAdapter variantOptionAdapterLevel1;

    private ProductVariant variant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variant = getIntent().getParcelableExtra(KEY_VARIANT_DATA);
        setContentView(R.layout.activity_variant);
        hideToolbar();
        initView();
        initViewListener();
        initAdapter();
        setupTopbar();
    }


    private void initView() {
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        productName = (TextView) findViewById(R.id.variant_product_name);
        productPrice = (TextView) findViewById(R.id.variant_product_price);
        productImage = (ImageView) findViewById(R.id.variant_image_title);
        optionNameLevel1 = (TextView) findViewById(R.id.text_variant_option_level1);
        optionRecyclerViewLevel1 = (RecyclerView) findViewById(R.id.rv_variant_option_level1);
        optionNameLevel2 = (TextView) findViewById(R.id.text_variant_option_level2);
        optionRecyclerViewLevel2 = (RecyclerView) findViewById(R.id.rv_variant_option_level2);
        buttonSave = (FrameLayout) findViewById(R.id.button_save);
        textButtonSave = (TextView) findViewById(R.id.text_button_save_variant);
    }

    private void initViewListener() {
        if (getIntent().getBooleanExtra(KEY_SELLER_MODE,false)) {
            textButtonSave.setText(getResources().getString(R.string.change_variant));
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getApplication() instanceof TkpdCoreRouter){
                        Intent intent = ((TkpdCoreRouter)getApplication()).goToEditProduct(VariantActivity.this, true, Integer.toString(variant.getProductId()));
                        startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_EDIT_PRODUCT);
                    }
                    finish();
                }
            });
        } else if (getIntent().getBooleanExtra(KEY_BUY_MODE,false)) {
            buttonSave.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_orange));
            textButtonSave.setText(getResources().getString(R.string.title_buy));
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(VariantActivity.SELECTED_VARIANT_RESULT_TO_BUY, generateExtraSelectedIntent());
                    finish();
                    VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                }
            });
        } else {
            buttonSave.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_green));
            textButtonSave.setText(getResources().getString(R.string.title_save));
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VariantDatum variantDatumSelected = getProductDatumSelected();
                    if (variantDatumSelected!=null && variantDatumSelected.getProductId().equals(variant.getProductId())) {
                        setResult(VariantActivity.SELECTED_VARIANT_RESULT, generateExtraSelectedIntent());
                        finish();
                        VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                    } else {
                        Intent intent = ProductInfoActivity.createInstance(VariantActivity.this, Integer.toString(variantDatumSelected.getProductId()));
                        VariantActivity.this.startActivity(intent);
                        setResult(VariantActivity.KILL_PDP_BACKGROUND, generateExtraSelectedIntent());
                        finish();
                    }
                }
            });
        }
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                    }
                });
    }

    private Intent generateExtraSelectedIntent() {
        Intent intent = new Intent();
        intent.putExtra(KEY_LEVEL1_SELECTED,variantOptionAdapterLevel1.getSelectedOption());
        if (variant.getVariantOption().size()>1)intent.putExtra(KEY_LEVEL2_SELECTED,variantOptionAdapterLevel2.getSelectedOption());
        return intent;
    }

    public void initAdapter() {
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
        Option level1Selected = getIntent().getParcelableExtra(KEY_LEVEL1_SELECTED);
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

        Option level2Selected = getIntent().getParcelableExtra(KEY_LEVEL2_SELECTED);
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
            variantOptionAdapterLevel1.notifyItemSelectedChange();
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
            variantOptionAdapterLevel2.notifyItemSelectedChange();
            optionNameLevel2.setVisibility(View.VISIBLE);
            optionRecyclerViewLevel2.setVisibility(View.VISIBLE);
        } else {
            variantOptionAdapterLevel1.notifyItemSelectedChange();
        }
    }


    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.product_variant));
    }

    private VariantDatum getProductDatumSelected() {
        VariantDatum variantDatumSelected = null;
        if (variant.getVariantOption().size()==1) {
            for (VariantDatum variantDatum: variant.getVariantData()) {
                if (variantDatum.getOptionIds().contains(variantOptionAdapterLevel1.getSelectedOption().getPvoId())) {
                    variantDatumSelected = variantDatum; break;
                }
            }
        } else {
            for (VariantDatum variantDatum: variant.getVariantData()) {
                if (variantDatum.getOptionIds().contains(variantOptionAdapterLevel1.getSelectedOption().getPvoId())
                        && variantDatum.getOptionIds().contains(variantOptionAdapterLevel2.getSelectedOption().getPvoId())) {
                    variantDatumSelected = variantDatum; break;
                }
            }
        }
        return variantDatumSelected;
    }

    @Override
    public void onVariantChosen(Option option, int level) {
        List<Integer> combinations = variant.getCombinationFromSelectedVariant(option.getPvoId());
        if (level==1) {
            if (combinations.size()==1 && variant.getVariantOption().size()>1) {
                option.setEnabled(false);
                for (int i=0; i<variantOptionAdapterLevel1.getVariantOptions().size(); i++) {
                    combinations = variant.getCombinationFromSelectedVariant(variantOptionAdapterLevel1.getVariantOptions().get(i).getPvoId());
                    if (combinations.size() > 1) {
                        variantOptionAdapterLevel1.setSelectedPosition(i);
                        variantOptionAdapterLevel1.notifyItemSelectedChange();
                        break;
                    }
                }
            } else if (variant.getVariantOption().size()>1) {
                for (Option otherLevelOption: variantOptionAdapterLevel2.getVariantOptions()) {
                    if (combinations.contains(otherLevelOption.getPvoId())) {
                        otherLevelOption.setEnabled(true);
                    } else {
                        otherLevelOption.setEnabled(false);
                    }
                }
                variantOptionAdapterLevel2.notifyItemSelectedChange();
            }

        } else {
            if (!combinations.contains(variantOptionAdapterLevel1.getVariantOptions().get(variantOptionAdapterLevel1.getSelectedPosition()).getPvoId())) {
                combinations = variant.getCombinationFromSelectedVariant(variantOptionAdapterLevel1.getVariantOptions()
                        .get(variantOptionAdapterLevel1.getSelectedPosition()).getPvoId());
                for (int i=0; i<variantOptionAdapterLevel2.getVariantOptions().size(); i++) {
                    if (combinations.contains(variantOptionAdapterLevel2.getVariantOptions().get(i).getPvoId())) {
                        variantOptionAdapterLevel2.setSelectedPosition(i);
                        break;
                    }
                }
            }
        }
    }
}
