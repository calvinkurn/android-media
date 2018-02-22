package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductManageViewHolder extends ProductViewHolder{

    public static final int REQUEST_CODE_VARIANT = 2;

    public static final String IS_ACTIVE_STOCK = "IS_ACTIVE_STOCK";
    private static final String IS_STOCKTOTAL_VISIBLE = "IS_STOCKTOTAL_VISIBLE";

    public static final int DEFAULT_STOCK_VALUE = 0;

    private SpinnerTextView stockStatusSpinnerTextView;
    private ExpandableOptionSwitch stockTotalExpandableOptionSwitch;
    private CounterInputView stockTotalCounterInputView;

    private LabelView variantLabelView;

    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;

    private Listener listener;

    public ProductManageViewHolder(View view, Listener listener) {

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
                ProductManageViewHolder.this.listener.onTotalStockUpdated(isExpand ? (int) stockTotalCounterInputView.getDoubleValue() : 0);
            }
        });
        stockTotalCounterInputView.addTextChangedListener(new NumberTextWatcher(stockTotalCounterInputView.getEditText()) {
            @Override
            public void onNumberChanged(double number) {
                if (isTotalStockValid()) {
                    stockTotalCounterInputView.setError(null);
                }
                ProductManageViewHolder.this.listener.onTotalStockUpdated((int) number);
            }
        });

        variantLabelView = (LabelView) view.findViewById(R.id.label_view_variant);
        variantLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductManageViewHolder.this.listener.startProductVariantActivity(
                        productVariantByCatModelList);
            }
        });

        setListener(listener);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }



    public int getStatusStock() {
        return Integer.parseInt(stockStatusSpinnerTextView.getSpinnerValue());
    }

    public boolean isStockAvailable(){
        return getStatusStock() == Integer.parseInt(stockStatusSpinnerTextView.getContext().getString(R.string.product_stock_available_value));
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
    public boolean isDataValid() {
        if (!isTotalStockValid()) {
            stockTotalCounterInputView.requestFocus();
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_STOCK_STATUS);
            return false;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_VARIANT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data!= null && data.hasExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION)) {
                        ProductVariantViewModel productVariantViewModel =
                                data.getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION);
                        listener.updateVariantModel(productVariantViewModel);
                        setUiVariantSelection();
                    }
                }
                break;
        }
    }

    public void onSuccessGetProductVariantCat(List<ProductVariantByCatModel> productVariantByCatModelList) {
        this.productVariantByCatModelList = (ArrayList<ProductVariantByCatModel>) productVariantByCatModelList;
        setUiVariantSelection();
    }

    private void setUiVariantSelection() {
        ProductVariantViewModel productVariantViewModel = listener.getCurrentVariantModel();
        if (productVariantViewModel == null || !productVariantViewModel.hasSelectedVariant()) {
            variantLabelView.resetContentText();
            listener.onVariantCountChange(false);

            if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
                variantLabelView.setVisibility(View.GONE);
            } else {
                variantLabelView.setVisibility(View.VISIBLE);
            }
        } else {
            List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList = productVariantViewModel.getProductVariant();
            int variantSize = productVariantCombinationViewModelList.size();

            int activeVariantCount = 0;
            for (int i = 0; i< variantSize; i++) {
                ProductVariantCombinationViewModel productVariantCombinationViewModel = productVariantCombinationViewModelList.get(i);
                if (productVariantCombinationViewModel.isActive()) {
                    activeVariantCount++;
                }
            }
            /*boolean hasActiveVariant = false;
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
            }*/
            if (activeVariantCount == 0) {
                variantLabelView.resetContentText();
                listener.onVariantCountChange(false);
            } else {
                String selectedVariantString = variantSize + " " + variantLabelView.getContext().getString(R.string.product_label_variant);
                variantLabelView.setContent(selectedVariantString);
                listener.onVariantCountChange(true);
            }
            variantLabelView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderData(ProductViewModel model) {
        setStockStatus(model.getProductStatus());
        setStockManaged(model.getProductStatus() == UploadToTypeDef.TYPE_ACTIVE && model.getProductStock() > 0);
        setTotalStock((int)model.getProductStock());

        setUiVariantSelection();
    }

    @Override
    public void updateModel(ProductViewModel model) {
        model.setProductStock(getTotalStock());
        model.setProductStatus(getStatusStock());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(IS_ACTIVE_STOCK, stockTotalExpandableOptionSwitch.isEnabled());
        savedInstanceState.putBoolean(IS_STOCKTOTAL_VISIBLE, stockTotalExpandableOptionSwitch.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        stockTotalExpandableOptionSwitch.setEnabled(savedInstanceState.getBoolean(IS_ACTIVE_STOCK));

        boolean isStockTotalVisible = savedInstanceState.getBoolean(IS_STOCKTOTAL_VISIBLE, false);
        stockTotalExpandableOptionSwitch.setVisibility(isStockTotalVisible ? View.VISIBLE : View.GONE);
    }

    public interface Listener {

        void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelArrayList);

        void onTotalStockUpdated(int total);

        void onVariantCountChange(boolean hasActiveVariant);

        ProductVariantViewModel getCurrentVariantModel();
        void updateVariantModel(ProductVariantViewModel productVariantViewModel);
    }
}