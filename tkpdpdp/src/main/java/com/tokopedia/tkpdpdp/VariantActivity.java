package com.tokopedia.tkpdpdp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.Variant;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;
import com.tokopedia.design.component.EditTextCompat;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tkpdpdp.adapter.VariantOptionAdapter;
import com.tokopedia.tkpdpdp.customview.NumberPickerWithCounterView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.tokopedia.core.var.TkpdCache.Key.STATE_ORIENTATION_CHANGED;
import static com.tokopedia.core.var.TkpdCache.PRODUCT_DETAIL;


public class VariantActivity extends TActivity  implements
        VariantOptionAdapter.OnVariantOptionChoosedListener {

    public static final String KEY_VARIANT_DATA = "VARIANT_DATA";
    public static final String KEY_PRODUCT_DETAIL_DATA = "PRODUCT_DETAIL_DATA";
    public static final String KEY_STATE_OPEN_VARIANT = "KEY_STATE_OPEN_VARIANT";
    public static final String KEY_STATE_RESULT_VARIANT = "KEY_STATE_RESULT_VARIANT";
    public static final String KEY_SELECTED_QUANTIY = "KEY_QUANTITY";
    public static final String KEY_REMARK_FOR_SELLER = "KEY_REMARK_FOR_SELLER";
    public static final String KEY_SELLER_MODE = "ON_SELLER_MODE";
    public static final String KEY_LEVEL1_SELECTED= "LEVEL1_OPTION";
    public static final String KEY_LEVEL2_SELECTED= "LEVEL2_OPTION";
    public static final String IDENTIFIER_COLOUR= "colour";
    public static final String IDENTIFIER_SIZE = "size";

    public static final int SELECTED_VARIANT_RESULT = 99;
    public static final int SELECTED_VARIANT_RESULT_SKIP_TO_CART = 98;
    public static final int SELECTED_VARIANT_RESULT_STAY_IN_PDP = 97;
    public static final int SELECTED_VARIANT_RESULT_CANCEL = 95;
    public static final int KILL_PDP_BACKGROUND = 96;
    private static final String CRASHLYTIC_VARIANT_TAG = "CRASHLYTIC VARIANT";

    public static final int STATE_BUTTON_BUY = 1123;
    public static final int STATE_BUTTON_CART = 2234;
    public static final int STATE_VARIANT_DEFAULT = 0;
    public static final int DEFAULT_MAXIMUM_STOCK_PICKER = 1000;
    public static final int DEFAULT_MINIMUM_STOCK_PICKER = 1;

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
    private TextView textStock;
    private View viewNewCheckoutFlow;
    private View buttonCart;
    private View viewCartPrice;
    private TextView textCartPrice;
    private View buttonBuy;
    private TextView textButtonBuy;
    private NumberPickerWithCounterView widgetQty;
    private EditTextCompat etNotesSeller;
    private View viewContainerQty;
    private View viewContainerRemark;
    private View viewContainerButton;

    private VariantOptionAdapter variantOptionAdapterLevel2;
    private VariantOptionAdapter variantOptionAdapterLevel1;

    private int stateFormVariantPage;
    private int selectedQuantity;
    private String selectedRemarkNotes;
    private ProductVariant productVariant;
    private ProductDetailData productDetailData;
    private String mainImage = "";
    private LocalCacheHandler localCacheHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariant = getIntent().getParcelableExtra(KEY_VARIANT_DATA);
        productDetailData = getIntent().getParcelableExtra(KEY_PRODUCT_DETAIL_DATA);
        stateFormVariantPage = getIntent().getIntExtra(KEY_STATE_OPEN_VARIANT, STATE_VARIANT_DEFAULT);
        selectedQuantity = getIntent().getIntExtra(KEY_SELECTED_QUANTIY, 1);
        selectedRemarkNotes = getIntent().getStringExtra(KEY_REMARK_FOR_SELLER);
        localCacheHandler = new LocalCacheHandler(VariantActivity.this, PRODUCT_DETAIL);
        setContentView(R.layout.activity_variant);
        hideToolbar();
        initView();
        initViewListener();
        initAdapter();
        initVariantData();
        setupTopbar();
    }


    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
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
        textStock = findViewById(R.id.text_variant_stock);
        viewNewCheckoutFlow = findViewById(R.id.container_new_checkout_flow);
        buttonCart = findViewById(R.id.action_button_cart);
        viewCartPrice = findViewById(R.id.view_product_price);
        textCartPrice = findViewById(R.id.text_product_price);
        buttonBuy = findViewById(R.id.new_button_save);
        textButtonBuy = findViewById(R.id.new_text_button_save);
        widgetQty = findViewById(R.id.number_picker_quantitiy_product);
        etNotesSeller = findViewById(R.id.et_remark);
        viewContainerQty = findViewById(R.id.view_qty_product);
        viewContainerRemark = findViewById(R.id.view_remark_for_seller);
        viewContainerButton = findViewById(R.id.all_kind_button_buy);

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
        widgetQty.setMinValue(
                Integer.valueOf(productDetailData.getInfo().getProductMinOrder())
        );
        renderHeaderInfo();
        setUpByConfiguration(getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpByConfiguration(newConfig);
    }

    private void setUpByConfiguration(Configuration configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!localCacheHandler.getBoolean(STATE_ORIENTATION_CHANGED).booleanValue()) {
                UnifyTracking.eventPDPOrientationChanged(this, Integer.toString(productDetailData.getInfo().getProductId()));
                localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED,Boolean.TRUE);
                localCacheHandler.applyEditor();
            }
        }
    }

    private void renderHeaderInfo() {
        productName.setText(productDetailData.getInfo().getProductName());
        productPrice.setText(productDetailData.getInfo().getProductPrice());
        etNotesSeller.setText(selectedRemarkNotes);
        widgetQty.setOnPickerActionListener(new com.tokopedia.design.component.NumberPickerWithCounterView.OnPickerActionListener() {
            @Override
            public void onNumberChange(int num) {
                selectedQuantity = num;
                textCartPrice.setText(VariantActivity.this.generateTextCartPrice());

                if(num < widgetQty.getMinValue()){
                    buttonBuy.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_grey));
                    buttonBuy.setClickable(false);
                } else{
                    buttonBuy.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.orange_button_rounded));
                    buttonBuy.setClickable(true);
                }
            }
        });
        try {
            widgetQty.setInitialState(
                    Integer.parseInt(productDetailData.getInfo().getProductMinOrder()),
                    DEFAULT_MAXIMUM_STOCK_PICKER,
                    selectedQuantity);
        } catch (NumberFormatException e) {
            widgetQty.setInitialState(
                    DEFAULT_MINIMUM_STOCK_PICKER,
                    DEFAULT_MAXIMUM_STOCK_PICKER,
                    selectedQuantity);
        }
        if(isCampaign()) {
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

    private String generateTextCartPrice() {
        if (isCampaign()) {
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(productDetailData.getCampaign().getDiscountedPrice() * selectedQuantity, true);
        } else {
            for (ProductWholesalePrice item : productDetailData.getWholesalePrice()) {
                if (selectedQuantity >= item.getWholesaleMinRaw() && selectedQuantity <= item.getWholesaleMaxRaw()) {
                    return CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            (long) item.getWholesalePriceRaw() * (long) selectedQuantity, true);
                }
            }
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    (long) productDetailData.getInfo().getProductPriceUnformatted() *
                            (long) selectedQuantity, true);
        }
    }

    private boolean isCampaign() {
        return productDetailData.getCampaign() != null
                && productDetailData.getCampaign().getActive();
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
            viewContainerButton.setVisibility(View.GONE);
        }
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    private String generateTextButtonBuy() {
        switch (stateFormVariantPage) {
            case STATE_BUTTON_BUY:
                if (isPreOrder()) {
                    return getResources().getString(R.string.title_pre_order);
                } else {
                    return getResources().getString(R.string.title_buy_now);
                }
            case STATE_BUTTON_CART:
                return getResources().getString(R.string.title_add_to_cart);
            default:
                if (isPreOrder()) {
                    return getResources().getString(R.string.title_pre_order);
                } else {
                    return getResources().getString(R.string.title_buy);
                }
        }
    }

    private boolean isPreOrder() {
        return productDetailData.getPreOrder() != null && productDetailData.getPreOrder().getPreorderStatus().equals("1")
                && !productDetailData.getPreOrder().getPreorderStatus().equals("0")
                && !productDetailData.getPreOrder().getPreorderProcessTime().equals("0")
                && !productDetailData.getPreOrder().getPreorderProcessTimeType().equals("0")
                && !productDetailData.getPreOrder().getPreorderProcessTimeTypeString().equals("0");
    }

    private Drawable generateBackgroundButtonBuy() {
        switch (stateFormVariantPage) {
            case STATE_BUTTON_CART:
                return ContextCompat.getDrawable(VariantActivity.this, R.drawable.white_button_rounded);
            case STATE_BUTTON_BUY:
            default:
                return ContextCompat.getDrawable(VariantActivity.this, R.drawable.orange_button_rounded);
        }
    }

    private int generateColorTextButtonBuy() {
        switch (stateFormVariantPage) {
            case STATE_BUTTON_CART:
                return ContextCompat.getColor(VariantActivity.this, R.color.font_black_primary_70);
            case STATE_BUTTON_BUY:
            default:
                return ContextCompat.getColor(VariantActivity.this, R.color.href_link_rev);
        }
    }

    private View.OnClickListener onButtonCartClick() {
        return view -> {
            Intent intent = generateExtraSelectedIntent();
            intent.putExtra(KEY_STATE_RESULT_VARIANT, VariantActivity.SELECTED_VARIANT_RESULT_STAY_IN_PDP);
            setResult(RESULT_OK, intent);
            finish();
            VariantActivity.this.overridePendingTransition(0,com.tokopedia.core2.R.anim.push_down);
        };
    }

    private View.OnClickListener onButtonBuyClick() {
        return view -> {
            Intent intent = generateExtraSelectedIntent();
            intent.putExtra(KEY_STATE_RESULT_VARIANT, VariantActivity.SELECTED_VARIANT_RESULT_SKIP_TO_CART);
            setResult(RESULT_OK, intent);
            finish();
            VariantActivity.this.overridePendingTransition(0,com.tokopedia.core2.R.anim.push_down);
        };
    }

    public void updateButton(Child child) {
        if (child.isIsBuyable() && productDetailData.getShopInfo().getShopStatus()==1) {
            viewNewCheckoutFlow.setVisibility(VISIBLE);
            buttonSave.setVisibility(View.GONE);
            buttonBuy.setBackground(ContextCompat.getDrawable(VariantActivity.this, R.drawable.orange_button_rounded));

            textButtonBuy.setText(generateTextButtonBuy());
            textCartPrice.setText(generateTextCartPrice());

            buttonCart.setVisibility(stateFormVariantPage == STATE_VARIANT_DEFAULT ? VISIBLE : View.GONE);

            buttonBuy.setClickable(true);
            buttonBuy.setOnClickListener(
                    stateFormVariantPage == STATE_BUTTON_CART ?
                            onButtonCartClick() :
                            onButtonBuyClick()
            );
            buttonCart.setOnClickListener(onButtonCartClick());
        } else if (child.isIsBuyable()==false) {
            viewNewCheckoutFlow.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
            textButtonSave.setText(getResources().getString(R.string.title_warehouse));
            textButtonSave.setTextColor(ContextCompat.getColor(VariantActivity.this,R.color.black_38));
            buttonSave.setBackground(ContextCompat.getDrawable(VariantActivity.this,R.drawable.button_save_grey));
            buttonSave.setClickable(false);
        } else {
            viewNewCheckoutFlow.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
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
        intent.putExtra(KEY_PRODUCT_DETAIL_DATA, productDetailData);
        intent.putExtra(KEY_VARIANT_DATA, productVariant);
        intent.putExtra(KEY_SELECTED_QUANTIY, widgetQty.getValue());
        intent.putExtra(KEY_REMARK_FOR_SELLER, String.valueOf(etNotesSeller.getText()));
        intent.putExtra(KEY_LEVEL1_SELECTED, variantOptionAdapterLevel1.getSelectedOption());
        if (productVariant.getVariant().size() > 1) {
            intent.putExtra(KEY_LEVEL2_SELECTED, variantOptionAdapterLevel2.getSelectedOption());
        }
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

        try {
            if (defaultChild != null && TextUtils.isEmpty(defaultChild.getPicture().getThumbnail())) {
                mainImage = productDetailData.getProductImages().get(0).getImageSrc();
            } else if (productDetailData.getProductImages().size()>1) {
                mainImage = productDetailData.getProductImages().get(1).getImageSrc();
            }
        } catch (Exception e) {
            Crashlytics.log(
                    0,
                    CRASHLYTIC_VARIANT_TAG,
                    String.valueOf(productDetailData.getInfo().getProductId()) + " " + e.getMessage()
            );
        }

        for (Child child: productVariant.getChildren()) {
            if (TextUtils.isEmpty(child.getPicture().getThumbnail())) {
                child.getPicture().setThumbnail(mainImage);
                child.getPicture().setOriginal(mainImage);
            }
        }

        if (defaultChild!=null && defaultChild.getOptionIds() != null && defaultChild.getOptionIds().size()>0) {
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

        if (productVariant.getVariant().size()==2 && defaultChild.getOptionIds().size()>1) {
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
        topBarTitle.setText(getString(R.string.title_activity_product_modal));
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
            if(child.isEnabled()){
                if(child.isLimitedStock()){
                    textStock.setTextColor(ContextCompat.getColor(VariantActivity.this, R.color.tkpd_dark_red));
                } else {
                    textStock.setTextColor(ContextCompat.getColor(VariantActivity.this, R.color.black_70));
                }
                textStock.setText(child.getStockWording());
                textStock.setVisibility(View.VISIBLE);
            }
            productDetailData.getInfo().setProductId(child.getProductId());
            productDetailData.getInfo().setProductName(child.getName());
            productDetailData.getInfo().setProductPrice(child.getPriceFmt());
            productDetailData.getInfo().setProductPriceUnformatted(child.getPrice());
            productDetailData.getInfo().setProductUrl(child.getUrl());
            productDetailData.getInfo().setProductAlreadyWishlist(child.isWishlist()?1:0);
            productDetailData.getInfo().setProductStockWording(child.getStockWordingHtml());
            productDetailData.getInfo().setLimitedStock(child.isLimitedStock());
            productDetailData.setCampaign(child.getCampaign());
            if (!TextUtils.isEmpty(child.getPicture().getThumbnail()))  {
                productDetailData.getProductImages().get(0).setImageSrc300(child.getPicture().getThumbnail());
                productDetailData.getProductImages().get(0).setImageSrc(child.getPicture().getOriginal());
                ImageHandler.LoadImage(productImage, child.getPicture().getThumbnail());
            }
            updateButton(child);
        }
        renderHeaderInfo();
        List<String> joinVariant = productVariant.generateVariantValueIntoList(productDetailData.getInfo().getProductId());

        if (productVariant.getVariant().get(level-1).getIdentifier().equals(IDENTIFIER_SIZE)) {
            UnifyTracking.eventSelectSizeVariant(this, option.getValue());
        } else if (productVariant.getVariant().get(level-1).getIdentifier().equals(IDENTIFIER_COLOUR)) {
            UnifyTracking.eventSelectColorVariant(this, option.getValue());
        }
    }

    @Override
    public void onBackPressed(){
        Child childSelected = getProductDatumSelected();
        Intent intent;
        if (childSelected!=null && childSelected.isIsBuyable() && productDetailData.getShopInfo().getShopStatus()==1) {
            intent = generateExtraSelectedIntent();
            intent.putExtra(KEY_STATE_RESULT_VARIANT, VariantActivity.SELECTED_VARIANT_RESULT_CANCEL);
        } else {
            intent = new Intent();
            intent.putExtra(KEY_PRODUCT_DETAIL_DATA, productDetailData);
            intent.putExtra(KEY_VARIANT_DATA, productVariant);
            intent.putExtra(KEY_STATE_RESULT_VARIANT, VariantActivity.SELECTED_VARIANT_RESULT_CANCEL);
        }
        setResult(RESULT_OK, intent);
        finish();
        VariantActivity.this.overridePendingTransition(0,com.tokopedia.core2.R.anim.push_down);
    }
}