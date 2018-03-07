package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductManageViewHolder extends ProductViewHolder {

    public static final int REQUEST_CODE_VARIANT = 2;

    public static final int DEFAULT_STOCK_VALUE = 0;

    private SpinnerTextView stockStatusSpinnerTextView;
    private CounterInputView stockTotalCounterInputView;
    private LabelView variantLabelView;
    private EditText skuEditText;
    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private Listener listener;

    public ProductManageViewHolder(View view, Listener listener) {
        setListener(listener);

        stockStatusSpinnerTextView = view.findViewById(R.id.spinner_text_view_stock_status);
        stockTotalCounterInputView = view.findViewById(R.id.counter_input_view_stock_total);
        stockStatusSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                onStockSpinnerValueChanged(value);
            }
        });
        onStockSpinnerValueChanged(stockStatusSpinnerTextView.getSpinnerValue());

        stockTotalCounterInputView.addTextChangedListener(new NumberTextWatcher(stockTotalCounterInputView.getEditText()) {
            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                if (isTotalStockValid()) {
                    stockTotalCounterInputView.setError(null);
                }
                ProductManageViewHolder.this.listener.onTotalStockUpdated((int) number);
            }
        });

        variantLabelView = view.findViewById(R.id.label_view_variant);
        variantLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductManageViewHolder.this.listener.startProductVariantActivity(productVariantByCatModelList);
            }
        });

        skuEditText = view.findViewById(R.id.edit_text_sku);
    }

    private void onStockSpinnerValueChanged(String value){
        if (value.equalsIgnoreCase(stockStatusSpinnerTextView.getContext().getString(R.string.product_stock_not_available_value)) || value.equalsIgnoreCase(stockStatusSpinnerTextView.getContext().getString(R.string.product_stock_available_value))) {
            stockTotalCounterInputView.setVisibility(View.GONE);
            ProductManageViewHolder.this.listener.onTotalStockUpdated(0);
        } else {
            stockTotalCounterInputView.setVisibility(View.VISIBLE);
            stockTotalCounterInputView.setValue(1);
            ProductManageViewHolder.this.listener.onTotalStockUpdated((int) stockTotalCounterInputView.getDoubleValue());
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public int getViewStatusStock() {
        return Integer.parseInt(stockStatusSpinnerTextView.getSpinnerValue());
    }

    private int getStatusStock() {
        int status = getViewStatusStock();
        if (status == StockTypeDef.TYPE_ACTIVE_LIMITED) {
            return StockTypeDef.TYPE_ACTIVE;
        }
        return status;
    }

    public boolean isStockAvailable() {
        return getStatusStock() == Integer.parseInt(stockStatusSpinnerTextView.getContext().getString(R.string.product_stock_available_value));
    }

    private void setStockStatus(int unit) {
        stockStatusSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    private boolean isStockViewVisible() {
        return stockTotalCounterInputView.getVisibility() == View.VISIBLE;
    }

    private void setStockManaged(boolean stockManaged) {
        stockTotalCounterInputView.setVisibility(stockManaged ? View.VISIBLE : View.GONE);
    }

    private int getTotalStock() {
        if (isStockViewVisible()) {
            return (int) stockTotalCounterInputView.getDoubleValue();
        } else {
            return DEFAULT_STOCK_VALUE;
        }
    }

    private void setTotalStock(int value) {
        stockTotalCounterInputView.setValue(value);
    }

    private boolean isTotalStockValid() {
        if (!isStockViewVisible()) {
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

    private String getSkuText() {
        return skuEditText.getText().toString();
    }

    private void setSkuText(String value) {
        skuEditText.setText(value);
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
                    if (data != null && data.hasExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION)) {
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
            ProductVariantOptionParent productVariantOptionParentLv1 =
                    productVariantViewModel.getVariantOptionParent(1);
            ProductVariantOptionParent productVariantOptionParentLv2 =
                    productVariantViewModel.getVariantOptionParent(2);
            StringBuilder selectedVariantString = new StringBuilder(productVariantOptionParentLv1.getProductVariantOptionChild().size()
                    + " " + productVariantOptionParentLv1.getName());
            if (productVariantOptionParentLv2 != null && productVariantOptionParentLv2.hasProductVariantOptionChild()) {
                selectedVariantString.append("\n");
                selectedVariantString.append(productVariantOptionParentLv2.getProductVariantOptionChild().size());
                selectedVariantString.append(" ");
                selectedVariantString.append(productVariantOptionParentLv2.getName());
            }
            variantLabelView.setContent(selectedVariantString);
            listener.onVariantCountChange(true);
            variantLabelView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderData(ProductViewModel model) {
        if (model.getProductStock() == 0) {
            int productStatus = model.getProductStatus();
            if (productStatus == StockTypeDef.TYPE_ACTIVE) {
                setStockStatus(StockTypeDef.TYPE_ACTIVE);
            } else {
                setStockStatus(StockTypeDef.TYPE_WAREHOUSE);
            }
            setStockManaged(false);
        } else {
            setStockStatus(StockTypeDef.TYPE_ACTIVE_LIMITED);
            setStockManaged(true);
        }
        setTotalStock((int) model.getProductStock());
        setSkuText(model.getProductSku());
        setUiVariantSelection();
    }

    @Override
    public void updateModel(ProductViewModel model) {
        model.setProductStock(getTotalStock());
        model.setProductStatus(getStatusStock());
        model.setProductSku(getSkuText());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {

    }

    public interface Listener {

        void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelArrayList);

        void onTotalStockUpdated(int total);

        void onVariantCountChange(boolean hasActiveVariant);

        ProductVariantViewModel getCurrentVariantModel();

        void updateVariantModel(ProductVariantViewModel productVariantViewModel);
    }
}