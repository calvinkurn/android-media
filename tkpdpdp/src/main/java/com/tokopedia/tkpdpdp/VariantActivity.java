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
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.Variant;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.adapter.VariantOptionAdapter;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;

import java.util.List;


public class VariantActivity extends TActivity  implements VariantOptionAdapter.OnVariantOptionChoosedListener  {

    public static final String KEY_VARIANT_DATA = "VARIANT_DATA";
    public static final String KEY_PRODUCT_DETAIL_DATA = "PRODUCT_DETAIL_DATA";
    public static final String KEY_BUY_MODE = "ON_BUY_MODE";
    public static final String KEY_SELLER_MODE = "ON_SELLER_MODE";
    public static final String KEY_LEVEL1_SELECTED= "LEVEL1_OPTION";
    public static final String KEY_LEVEL2_SELECTED= "LEVEL2_OPTION";
    public static final String IDENTIFIER_COLOUR= "colour";

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

    private ProductVariant productVariant;
    private ProductDetailData productDetailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariant = getIntent().getParcelableExtra(KEY_VARIANT_DATA);
        productDetailData = getIntent().getParcelableExtra(KEY_PRODUCT_DETAIL_DATA);
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

        productName.setText(productDetailData.getInfo().getProductName());
        productPrice.setText(productDetailData.getInfo().getProductPrice());
        ImageHandler.LoadImage(productImage, productDetailData.getProductImages().get(0).getImageSrc300());
        //TODO bedges, promo
    }

    private void initViewListener() {
        //TODO
       /* if (getIntent().getBooleanExtra(KEY_SELLER_MODE,false)) {
            textButtonSave.setText(getResources().getString(R.string.change_variant));
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getApplication() instanceof TkpdCoreRouter){
                        Intent intent = ((TkpdCoreRouter)getApplication()).goToEditProduct(VariantActivity.this, true, Long.toString(productVariant.getParentId()));
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
                    Child childSelected = getProductDatumSelected();
                    if (childSelected!=null && childSelected.getProductId()==productVariant.getParentId()) {
                        setResult(VariantActivity.SELECTED_VARIANT_RESULT, generateExtraSelectedIntent());
                        finish();
                        VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                    } else {
                        Intent intent = ProductInfoActivity.createInstance(VariantActivity.this, Long.toString(childSelected.getProductId()));
                        VariantActivity.this.startActivity(intent);
                        setResult(VariantActivity.KILL_PDP_BACKGROUND, generateExtraSelectedIntent());
                        finish();
                    }
                }
            });
        }*/
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
        if (productVariant.getVariant().size()>1)intent.putExtra(KEY_LEVEL2_SELECTED,variantOptionAdapterLevel2.getSelectedOption());
        return intent;
    }

    public void initAdapter() {
        Variant variantLevel1 = productVariant.getVariant().get(productVariant.getLevel1Variant());
        optionNameLevel1.setText(variantLevel1.getName()+" :");
        variantOptionAdapterLevel1 = new VariantOptionAdapter(VariantActivity.this,variantLevel1.getOption(),
                TextUtils.equals(IDENTIFIER_COLOUR,variantLevel1.getIdentifier()), VariantActivity.this, 1);
        ChipsLayoutManager chipsLayoutManager= ChipsLayoutManager.newBuilder(VariantActivity.this)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        optionRecyclerViewLevel1.setNestedScrollingEnabled(false);
        optionRecyclerViewLevel1.setLayoutManager(chipsLayoutManager);
        optionRecyclerViewLevel1.setAdapter(variantOptionAdapterLevel1);
        optionNameLevel1.setText(variantLevel1.getName()+" :");
        //TODO handle selected variant
        Option optionLevel1 = getIntent().getParcelableExtra(KEY_LEVEL1_SELECTED);
        if (optionLevel1==null) {
            Child childProduct = productVariant.getChildFromProductId(productVariant.getDefaultChild());
            for (int i=0; i<variantOptionAdapterLevel1.getVariantOptions().size(); i++) {
                if (childProduct.getOptionIds().get(0) == variantOptionAdapterLevel1.getVariantOptions().get(i).getId()) {
                    variantOptionAdapterLevel1.setSelectedPosition(i);
                    break;
                }
            }
        }

//        variantOptionAdapterLevel1.notifyItemSelectedChange();
//
//        if (productVariant.getVariant().size()>1) {
//            Variant variantLevel2 = productVariant.getVariant().get(1- productVariant.getLevel1Variant());
//            variantOptionAdapterLevel2
//                    = new VariantOptionAdapter(VariantActivity.this,variantLevel2.getOption(),
//                    TextUtils.equals(IDENTIFIER_COLOUR,variantLevel2.getIdentifier()), VariantActivity.this, 2);
//            ChipsLayoutManager chipsLayoutManagerLevel2= ChipsLayoutManager.newBuilder(VariantActivity.this)
//                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
//                    .build();
//            optionRecyclerViewLevel2.setNestedScrollingEnabled(false);
//            optionRecyclerViewLevel2.setLayoutManager(chipsLayoutManagerLevel2);
//            optionRecyclerViewLevel2.setAdapter(variantOptionAdapterLevel2);
//            optionNameLevel1.setText(variantLevel2.getName()+" :");
//            variantOptionAdapterLevel1.notifyItemSelectedChange();
//            //TODO handle selected variant
//            Option level2Selected = getIntent().getParcelableExtra(KEY_LEVEL2_SELECTED);
//            if (level2Selected==null) {
//                Child childProduct = productVariant.getChildFromProductId(productVariant.getDefaultChild());
//                for (int i=0; i<variantOptionAdapterLevel2.getVariantOptions().size(); i++) {
//                    if (childProduct.getOptionIds().get(1) == variantOptionAdapterLevel2.getVariantOptions().get(i).getId()) {
//                        variantOptionAdapterLevel2.setSelectedPosition(i);
//                        break;
//                    }
//                }
//            }
//            optionNameLevel2.setVisibility(View.VISIBLE);
//            optionRecyclerViewLevel2.setVisibility(View.VISIBLE);
//            variantOptionAdapterLevel2.notifyItemSelectedChange();
//        } else {
//            variantOptionAdapterLevel1.notifyItemSelectedChange();
//        }
    }


    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.product_variant));
    }

    private Child getProductDatumSelected() {
        Child childSelected = null;
        if (productVariant.getVariant().size()==1) {
            for (Child child: productVariant.getChildren()) {
                if (child.getOptionIds().contains(variantOptionAdapterLevel1.getSelectedOption().getId())) {
                    childSelected = child; break;
                }
            }
        } else {
            for (Child child: productVariant.getChildren()) {
                if (child.getOptionIds().contains(variantOptionAdapterLevel1.getSelectedOption().getId())
                        && child.getOptionIds().contains(variantOptionAdapterLevel2.getSelectedOption().getId())) {
                    childSelected = child; break;
                }
            }
        }
        return childSelected;
    }

    @Override
    public void onVariantChosen(Option option, int level) {
        List<Long> combinations = productVariant.getCombinationFromSelectedVariant(option.getId());
        if (level==1) {
            if (combinations.size()==1 && productVariant.getVariant().size()>1) {
                option.setEnabled(false);
                for (int i=0; i<variantOptionAdapterLevel1.getVariantOptions().size(); i++) {
                    combinations = productVariant.getCombinationFromSelectedVariant(variantOptionAdapterLevel1.getVariantOptions().get(i).getId());
                    if (combinations.size() > 1) {
                        variantOptionAdapterLevel1.setSelectedPosition(i);
                        variantOptionAdapterLevel1.notifyItemSelectedChange();
                        break;
                    }
                }
            } else if (productVariant.getVariant().size()>1) {
                for (Option otherLevelOption: variantOptionAdapterLevel2.getVariantOptions()) {
                    if (combinations.contains(otherLevelOption.getId())) {
                        otherLevelOption.setEnabled(true);
                    } else {
                        otherLevelOption.setEnabled(false);
                    }
                }
                variantOptionAdapterLevel2.notifyItemSelectedChange();
            }

        } else {
            if (!combinations.contains(variantOptionAdapterLevel1.getVariantOptions().get(variantOptionAdapterLevel1.getSelectedPosition()).getId())) {
                combinations = productVariant.getCombinationFromSelectedVariant(variantOptionAdapterLevel1.getVariantOptions()
                        .get(variantOptionAdapterLevel1.getSelectedPosition()).getId());
                for (int i=0; i<variantOptionAdapterLevel2.getVariantOptions().size(); i++) {
                    if (combinations.contains(variantOptionAdapterLevel2.getVariantOptions().get(i).getId())) {
                        variantOptionAdapterLevel2.setSelectedPosition(i);
                        break;
                    }
                }
            }
        }
    }
}
