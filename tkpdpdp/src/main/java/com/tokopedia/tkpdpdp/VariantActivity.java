package com.tokopedia.tkpdpdp;

import android.content.Intent;
import android.graphics.Paint;
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
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.Variant;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.adapter.VariantOptionAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;


public class VariantActivity extends TActivity  implements VariantOptionAdapter.OnVariantOptionChoosedListener  {

    public static final String KEY_VARIANT_DATA = "VARIANT_DATA";
    public static final String KEY_PRODUCT_DETAIL_DATA = "PRODUCT_DETAIL_DATA";
    public static final String KEY_SELLER_MODE = "ON_SELLER_MODE";
    public static final String KEY_LEVEL1_SELECTED= "LEVEL1_OPTION";
    public static final String KEY_LEVEL2_SELECTED= "LEVEL2_OPTION";
    public static final String IDENTIFIER_COLOUR= "colour";
    public static final String IDENTIFIER_SIZE = "size";

    public static final int SELECTED_VARIANT_RESULT = 99;
    public static final int SELECTED_VARIANT_RESULT_TO_BUY = 98;
    public static final int KILL_PDP_BACKGROUND = 97;

    private TextView topBarTitle;
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView textOriginalPrice;
    private TextView textDiscount;
    private RecyclerView optionRecyclerViewLevel1;
    private TextView optionNameLevel1;
    private RecyclerView optionRecyclerViewLevel2;
    private TextView optionNameLevel2;
    private FrameLayout buttonSave;
    private TextView textButtonSave;
    private View separator2;
    private TextView selectedLevel1;
    private TextView selectedLevel2;
    private TextView sizeChartLevel1;
    private TextView sizeChartLevel2;

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
        initVariantData();
        setupTopbar();
    }


    private void initView() {
        topBarTitle = findViewById(R.id.simple_top_bar_title);
        productName = findViewById(R.id.variant_product_name);
        productPrice =  findViewById(R.id.variant_product_price);
        productImage = findViewById(R.id.variant_image_title);
        optionNameLevel1 = findViewById(R.id.text_variant_option_level1);
        optionRecyclerViewLevel1 = findViewById(R.id.rv_variant_option_level1);
        optionNameLevel2 = findViewById(R.id.text_variant_option_level2);
        optionRecyclerViewLevel2 = findViewById(R.id.rv_variant_option_level2);
        buttonSave = findViewById(R.id.button_save);
        textButtonSave = findViewById(R.id.text_button_save_variant);
        textOriginalPrice = findViewById(R.id.text_original_price);
        textDiscount = findViewById(R.id.text_discount);
        separator2 = findViewById(R.id.separator2);
        selectedLevel1 = findViewById(R.id.selected_variant_level1);
        selectedLevel2 = findViewById(R.id.selected_variant_level2);
        sizeChartLevel1 = findViewById(R.id.sizechart_level1);
        sizeChartLevel2 = findViewById(R.id.sizechart_level2);
        ImageHandler.LoadImage(productImage, productDetailData.getProductImages().get(0).getImageSrc300());
        if (!TextUtils.isEmpty(productVariant.getSizechart()) &&
                productVariant.getVariant().get(0).getIdentifier().equals(IDENTIFIER_SIZE)) {
            sizeChartLevel1.setVisibility(VISIBLE);
            sizeChartLevel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSizeChart();
                }
            });
        } else if (!TextUtils.isEmpty(productVariant.getSizechart()) &&
                productVariant.getVariant().size()>0
                && productVariant.getVariant().get(1).getIdentifier().equals(IDENTIFIER_SIZE)) {
            sizeChartLevel2.setVisibility(VISIBLE);
            sizeChartLevel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSizeChart();
                }
            });
        }
        renderHeaderInfo();
    }

    private void renderHeaderInfo() {
        productName.setText(productDetailData.getInfo().getProductName());
        productPrice.setText(productDetailData.getInfo().getProductPrice());
        if(productDetailData.getCampaign() != null
                && productDetailData.getCampaign().getActive()) {
            textOriginalPrice.setText(productDetailData.getCampaign().getOriginalPriceFmt());
            textOriginalPrice.setPaintFlags(
                    textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            textDiscount.setText(String.format(
                    VariantActivity.this.getString(R.string.label_discount_percentage),
                    productDetailData.getCampaign().getDiscountedPercentage()
            ));
            productPrice.setText(productDetailData.getCampaign().getDiscountedPriceFmt());

            textDiscount.setVisibility(VISIBLE);
            textOriginalPrice.setVisibility(VISIBLE);
        } else {
            textDiscount.setVisibility(View.GONE);
            textOriginalPrice.setVisibility(View.GONE);
        }

    }

    private void openSizeChart() {
        ArrayList<String> images = new ArrayList<>();
        images.add(productVariant.getSizechart());
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PreviewProductImageDetail.FILELOC, images);
        bundle.putInt(PreviewProductImageDetail.IMG_POSITION, 0);
        Intent intent = new Intent(VariantActivity.this, PreviewProductImageDetail.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initViewListener() {
        if (getIntent().getBooleanExtra(KEY_SELLER_MODE,false)) {
           buttonSave.setVisibility(View.GONE);
        }
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    public void updateButton(Child child) {
        if (child.isIsBuyable() && productDetailData.getShopInfo().getShopStatus()==1) {
            buttonSave.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_orange));
            textButtonSave.setTextColor(ContextCompat.getColor(VariantActivity.this,R.color.href_link_rev));
            if (productDetailData.getPreOrder() != null && productDetailData.getPreOrder().getPreorderStatus().equals("1")
                    && !productDetailData.getPreOrder().getPreorderStatus().equals("0")
                    && !productDetailData.getPreOrder().getPreorderProcessTime().equals("0")
                    && !productDetailData.getPreOrder().getPreorderProcessTimeType().equals("0")
                    && !productDetailData.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
                textButtonSave.setText(getResources().getString(R.string.title_pre_order));
            } else {
                textButtonSave.setText(getResources().getString(R.string.title_buy));
            }
            buttonSave.setClickable(true);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(VariantActivity.SELECTED_VARIANT_RESULT_TO_BUY, generateExtraSelectedIntent());
                    finish();
                    VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                }
            });
        } else if (child.isIsBuyable()==false) {
            textButtonSave.setText(getResources().getString(R.string.title_warehouse));
            textButtonSave.setTextColor(ContextCompat.getColor(VariantActivity.this,R.color.black_38));
            buttonSave.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_grey));
            buttonSave.setClickable(false);
        } else {
            if (productDetailData.getPreOrder() != null && productDetailData.getPreOrder().getPreorderStatus().equals("1")
                    && !productDetailData.getPreOrder().getPreorderStatus().equals("0")
                    && !productDetailData.getPreOrder().getPreorderProcessTime().equals("0")
                    && !productDetailData.getPreOrder().getPreorderProcessTimeType().equals("0")
                    && !productDetailData.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
                textButtonSave.setText(getResources().getString(R.string.title_pre_order));
            } else {
                textButtonSave.setText(getResources().getString(R.string.title_buy));
            }
            textButtonSave.setTextColor(ContextCompat.getColor(VariantActivity.this,R.color.black_38));
            buttonSave.setClickable(false);
            buttonSave.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_grey));
        }
    }

    private Intent generateExtraSelectedIntent() {
        Intent intent = new Intent();
        intent.putExtra(KEY_PRODUCT_DETAIL_DATA,productDetailData);
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

        if (productVariant.getVariant().size()>1) {
            Variant variantLevel2 = productVariant.getVariant().get(1 - productVariant.getLevel1Variant());
            variantOptionAdapterLevel2
                    = new VariantOptionAdapter(VariantActivity.this,variantLevel2.getOption(),
                    TextUtils.equals(IDENTIFIER_COLOUR,variantLevel2.getIdentifier()), VariantActivity.this, 2);
            ChipsLayoutManager chipsLayoutManagerLevel2= ChipsLayoutManager.newBuilder(VariantActivity.this)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build();
            optionRecyclerViewLevel2.setNestedScrollingEnabled(false);
            optionRecyclerViewLevel2.setLayoutManager(chipsLayoutManagerLevel2);
            optionRecyclerViewLevel2.setAdapter(variantOptionAdapterLevel2);
            optionNameLevel2.setText(variantLevel2.getName()+" :");
            optionNameLevel2.setVisibility(VISIBLE);
            optionRecyclerViewLevel2.setVisibility(VISIBLE);
            separator2.setVisibility(VISIBLE);

        }
    }

    private void initVariantData() {

        Child defaultChild = productVariant.getChildFromProductId(productDetailData.getInfo().getProductId());
        if (productDetailData.getInfo().getProductId()==productVariant.getParentId() || defaultChild==null) {
            defaultChild = productVariant.getChildFromProductId(productVariant.getDefaultChild());
        }

        if (defaultChild.getOptionIds() != null && defaultChild.getOptionIds().size()>0) {
            int option1 = defaultChild.getOptionIds().get(0);
            if (getIntent().getParcelableExtra(KEY_LEVEL1_SELECTED) != null
                    && getIntent().getParcelableExtra(KEY_LEVEL1_SELECTED) instanceof Option) {
                option1 = ((Option) getIntent().getParcelableExtra(KEY_LEVEL1_SELECTED)).getId();
            }

            for (int i=0; i<variantOptionAdapterLevel1.getVariantOptions().size(); i++) {
                variantOptionAdapterLevel1.getVariantOptions().get(i).setEnabled(
                        productVariant.isOptionAvailable(variantOptionAdapterLevel1.getVariantOptions().get(i)));
                if (option1 == variantOptionAdapterLevel1.getVariantOptions().get(i).getId()) {
                    variantOptionAdapterLevel1.setSelectedPosition(i);
                }
            }
        }

        if (productVariant.getVariant().size()==2) {
            int option2 = defaultChild.getOptionIds().get(1);
            if (getIntent().getParcelableExtra(KEY_LEVEL2_SELECTED) != null
                    && getIntent().getParcelableExtra(KEY_LEVEL2_SELECTED) instanceof Option) {
                option2 = ((Option) getIntent().getParcelableExtra(KEY_LEVEL2_SELECTED)).getId();
            }
            for (int i=0; i<variantOptionAdapterLevel2.getVariantOptions().size(); i++) {
                variantOptionAdapterLevel2.getVariantOptions().get(i).setEnabled(
                        productVariant.isOptionAvailable(variantOptionAdapterLevel2.getVariantOptions().get(i)));
                if (option2 == variantOptionAdapterLevel2.getVariantOptions().get(i).getId()) {
                    variantOptionAdapterLevel2.setSelectedPosition(i);
                }
            }
            variantOptionAdapterLevel1.notifyItemSelectedChange();
            variantOptionAdapterLevel2.notifyItemSelectedChange();
        } else {
            variantOptionAdapterLevel1.notifyItemSelectedChange();
        }
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
        List<Integer> combinations = productVariant.getCombinationFromSelectedVariant(option.getId());
        if (level==1) {
            if (!productVariant.isOptionAvailable(option)) {
                option.setEnabled(false);
                for (int i=0; i<variantOptionAdapterLevel1.getVariantOptions().size(); i++) {
                    if (productVariant.isOptionAvailable(variantOptionAdapterLevel1.getVariantOptions().get(i))) {
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
            selectedLevel1.setText(option.getValue());
            selectedLevel1.setVisibility(VISIBLE);
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
            selectedLevel2.setText(option.getValue());
            selectedLevel2.setVisibility(VISIBLE);
        }
        Child child = getProductDatumSelected();
        if (child!=null) {
            productDetailData.getInfo().setProductId(child.getProductId());
            productDetailData.getInfo().setProductName(child.getName());
            productDetailData.getInfo().setProductPrice(child.getPriceFmt());
            productDetailData.getInfo().setProductUrl(child.getUrl());
            productDetailData.getInfo().setProductAlreadyWishlist(child.isWishlist()?1:0);
            productDetailData.setCampaign(child.getCampaign());
            if (!TextUtils.isEmpty(child.getPicture().getThumbnail()))  {
                productDetailData.getProductImages().get(0).setImageSrc300(child.getPicture().getThumbnail());
                productDetailData.getProductImages().get(0).setImageSrc(child.getPicture().getOriginal());
                ImageHandler.LoadImage(productImage, child.getPicture().getThumbnail());
            }
            updateButton(child);
        }
        renderHeaderInfo();
        if (productVariant.getVariant().get(level-1).getIdentifier().equals(IDENTIFIER_SIZE)) {
            UnifyTracking.eventSelectSizeVariant(option.getValue());
        } else if (productVariant.getVariant().get(level-1).getIdentifier().equals(IDENTIFIER_COLOUR)) {
            UnifyTracking.eventSelectColorVariant(option.getValue());
        }
    }

    @Override
    public void onBackPressed(){
        Child childSelected = getProductDatumSelected();
        if (childSelected!=null && childSelected.isIsBuyable() && productDetailData.getShopInfo().getShopStatus()==1) {
            setResult(VariantActivity.SELECTED_VARIANT_RESULT, generateExtraSelectedIntent());
        }
        finish();
        VariantActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
    }
}
