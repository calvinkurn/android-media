package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantCombinationSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.util.ProductVariantViewConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductManageViewHolder extends ProductViewHolder{

    public static final int REQUEST_CODE_VARIANT = 2;

    public static final String IS_ACTIVE_STOCK = "IS_ACTIVE_STOCK";
    private static final String IS_STOCKTOTAL_VISIBLE = "IS_STOCKTOTAL_VISIBLE";

    public static final String SAVED_PRD_VARIANT_SUBMIT = "svd_variant";
    public static final String SAVED_VARIANT_CAT = "svd_var";
    public static final String SAVED_OPTION_SUBMIT_LV_1 = "svd_opt_sub_lv1";

    public static final int DEFAULT_STOCK_VALUE = 0;

    private SpinnerTextView stockStatusSpinnerTextView;
    private ExpandableOptionSwitch stockTotalExpandableOptionSwitch;
    private CounterInputView stockTotalCounterInputView;

    private LabelView variantLabelView;

    private ProductVariantDataSubmit productVariantDataSubmit;
    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;

    private ArrayList<ProductVariantOptionSubmit> productVariantOptionSubmitList;

    private Listener listener;

    public ProductManageViewHolder(View view) {

        stockStatusSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_stock_status);
        stockTotalExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_stock_total);
        stockTotalCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_stock_total);

        stockStatusSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                if (value.equalsIgnoreCase(stockStatusSpinnerTextView.getContext().getString(R.string.product_stock_not_available_value))) {
                    stockTotalExpandableOptionSwitch.setExpand(false);
                    stockTotalExpandableOptionSwitch.setVisibility(View.GONE);
                } else {
                    stockTotalExpandableOptionSwitch.setVisibility(View.VISIBLE);
                }
            }
        });
        stockTotalExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                listener.onTotalStockUpdated(isExpand ? (int) stockTotalCounterInputView.getDoubleValue() : 0);
            }
        });
        stockTotalCounterInputView.addTextChangedListener(new NumberTextWatcher(stockTotalCounterInputView.getEditText()) {
            @Override
            public void onNumberChanged(double number) {
                if (isTotalStockValid()) {
                    stockTotalCounterInputView.setError(null);
                }
                listener.onTotalStockUpdated((int) number);
            }
        });

        variantLabelView = (LabelView) view.findViewById(R.id.label_view_variant);
        variantLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductManageViewHolder.this.listener.startProductVariantActivity(
                        productVariantByCatModelList,
                        productVariantDataSubmit,
                        productVariantOptionSubmitList);
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }



    public int getStatusStock() {
        return Integer.parseInt(stockStatusSpinnerTextView.getSpinnerValue());
    }

    public void setStockStatus(int unit) {
        stockStatusSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public boolean isStockManaged() {
        return stockTotalExpandableOptionSwitch.isExpanded();
    }

    public void setStockManaged(boolean stockManaged) {
        stockTotalExpandableOptionSwitch.setExpand(stockManaged);
    }

    public int getTotalStock() {
        if(isStockManaged()) {
            return (int) stockTotalCounterInputView.getDoubleValue();
        }else{
            return DEFAULT_STOCK_VALUE;
        }
    }

    public void setTotalStock(int value) {
        stockTotalCounterInputView.setValue(value);
    }


    private boolean isTotalStockValid() {
        if (!stockTotalExpandableOptionSwitch.isExpanded()) {
            return true;
        }
        String minStockString = CurrencyFormatHelper.removeCurrencyPrefix(stockTotalCounterInputView.getContext().getString(R.string.product_minimum_total_stock));
        String maxStockString = CurrencyFormatHelper.removeCurrencyPrefix(stockTotalCounterInputView.getContext().getString(R.string.product_maximum_total_stock));
        double minStock = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minStockString));
        double maxStock = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxStockString));
        if (minStock > getTotalStock() || getTotalStock() > maxStock) {
            stockTotalCounterInputView.setError(stockTotalCounterInputView.getContext().getString(R.string.product_error_total_stock_not_valid, minStockString, maxStockString));
            return false;
        }
        return true;
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        if (!isTotalStockValid()) {
            stockTotalCounterInputView.requestFocus();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_STOCK_STATUS);
        }
        return new Pair<>(true, "");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_VARIANT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data!= null && data.hasExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION)) {
                        setProductVariantDataSubmit((ProductVariantDataSubmit) data.getParcelableExtra(
                                ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION), null);
                    }
                }
                break;
        }
    }

    @Deprecated
    public ProductVariantDataSubmit getProductVariantDataSubmit() {
        return productVariantDataSubmit;
    }

    public ProductVariantViewModel getProductVariant() {
        return new ProductVariantViewModel();
    }

    public String getVariantStringSelection(){
        return variantLabelView.getContent();
    }

    public void onSuccessGetProductVariantCat(List<ProductVariantByCatModel> productVariantByCatModelList) {
        this.productVariantByCatModelList = (ArrayList<ProductVariantByCatModel>) productVariantByCatModelList;
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            variantLabelView.setVisibility(View.GONE);
        } else {
            variantLabelView.setVisibility(View.VISIBLE);
        }
        setUiVariantSelection(variantLabelView.getContent());
    }

    /**
     * this is to retrieve the original image from server, since client cannot upload image,
     * so this is to save the original image to retain the image url
     */
    public void setOptionSubmitLv1(ProductVariantDataSubmit productVariantDataSubmit) {
        productVariantOptionSubmitList = ProductVariantUtils.getProductVariantOptionSubmitLv1(productVariantDataSubmit);
    }

    public void setProductVariantDataSubmit(ProductVariantDataSubmit productVariantDataSubmit, String defaultStringSelection) {
        this.productVariantDataSubmit = productVariantDataSubmit;
        setUiVariantSelection(defaultStringSelection);
    }

    private void setUiVariantSelection(String defaultStringSelection) {
        if (productVariantDataSubmit == null || productVariantDataSubmit.getProductVariantUnitSubmitList() == null ||
                productVariantDataSubmit.getProductVariantUnitSubmitList().size() == 0) {
            variantLabelView.resetContentText();
            listener.onVariantCountChange(false);
        } else {
            String selectedVariantString = "";
            boolean hasActiveVariant = false;
            if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
                selectedVariantString = defaultStringSelection;
            } else {
                List<ProductVariantUnitSubmit> productVariantUnitSubmitList = productVariantDataSubmit.getProductVariantUnitSubmitList();
                for (int i = 0, sizei = productVariantUnitSubmitList.size(); i < sizei; i++) {
                    ProductVariantUnitSubmit productVariantUnitSubmit = productVariantUnitSubmitList.get(i);
                    int position = productVariantUnitSubmit.getPosition();
                    ProductVariantByCatModel productVariantByCatModel =
                            ProductVariantViewConverter.getProductVariantByCatModel(position, productVariantByCatModelList);
                    if (productVariantByCatModel == null) {
                        continue;
                    }
                    String variantName = productVariantByCatModel.getName();
                    List<ProductVariantOptionSubmit> optionList = productVariantUnitSubmit.getProductVariantOptionSubmitList();
                    if (optionList == null || optionList.size() == 0) {
                        continue;
                    }
                    if (i != 0 && !TextUtils.isEmpty(selectedVariantString)) {
                        selectedVariantString += "\n";
                    }
                    selectedVariantString += optionList.size() + " " + variantName;
                }
                List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = productVariantDataSubmit.getProductVariantCombinationSubmitList();
                if (productVariantCombinationSubmitList != null && productVariantCombinationSubmitList.size() > 0) {
                    hasActiveVariant = true;
                }
            }
            if (TextUtils.isEmpty(selectedVariantString)) {
                variantLabelView.resetContentText();
                listener.onVariantCountChange(false);
            } else {
                variantLabelView.setContent(selectedVariantString);
                listener.onVariantCountChange(hasActiveVariant);
            }
            variantLabelView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(IS_ACTIVE_STOCK, stockTotalExpandableOptionSwitch.isEnabled());
        savedInstanceState.putBoolean(IS_STOCKTOTAL_VISIBLE, stockTotalExpandableOptionSwitch.getVisibility() == View.VISIBLE);
        savedInstanceState.putParcelable(SAVED_PRD_VARIANT_SUBMIT, productVariantDataSubmit);
        savedInstanceState.putParcelableArrayList(SAVED_VARIANT_CAT, productVariantByCatModelList);
        savedInstanceState.putParcelableArrayList(SAVED_OPTION_SUBMIT_LV_1, productVariantOptionSubmitList);
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {

        stockTotalExpandableOptionSwitch.setEnabled(savedInstanceState.getBoolean(IS_ACTIVE_STOCK));

        boolean isStockTotalVisible = savedInstanceState.getBoolean(IS_STOCKTOTAL_VISIBLE, false);
        stockTotalExpandableOptionSwitch.setVisibility(isStockTotalVisible ? View.VISIBLE : View.GONE);
        productVariantDataSubmit = savedInstanceState.getParcelable(SAVED_PRD_VARIANT_SUBMIT);
        productVariantByCatModelList = savedInstanceState.getParcelableArrayList(SAVED_VARIANT_CAT);
        productVariantOptionSubmitList = savedInstanceState.getParcelableArrayList(SAVED_OPTION_SUBMIT_LV_1);
    }

    public interface Listener {

        void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelArrayList,
                                         ProductVariantDataSubmit productVariantSubmit,
                                         ArrayList<ProductVariantOptionSubmit> productVariantOptionSubmitArrayList);

        void onTotalStockUpdated(int total);

        void onVariantCountChange(boolean hasActiveVariant);
    }
}