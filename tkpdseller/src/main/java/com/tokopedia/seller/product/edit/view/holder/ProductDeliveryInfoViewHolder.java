package com.tokopedia.seller.product.edit.view.holder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDeliveryInfoViewHolder extends ProductViewHolder {
    private Listener listener;

    private SpinnerCounterInputView weightSpinnerCounterInputView;

    private LabelSwitch freeReturnsSwitch;
    private LabelSwitch insuranceSwitch;

    public ProductDeliveryInfoViewHolder(View view, Listener listener) {
        weightSpinnerCounterInputView = view.findViewById(R.id.spinner_counter_input_view_weight);
        insuranceSwitch = view.findViewById(R.id.label_switch_insurance);
        freeReturnsSwitch = view.findViewById(R.id.label_switch_free_return);
        weightSpinnerCounterInputView.addTextChangedListener(new NumberTextWatcher(weightSpinnerCounterInputView.getCounterEditText(), weightSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)) {
            @Override
            public void onNumberChanged(double number) {
                if (isWeightValid()) {
                    weightSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        weightSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weightSpinnerCounterInputView.setCounterValue(Double.parseDouble(weightSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                weightSpinnerCounterInputView.setCounterError(null);
            }
        });

        freeReturnsSwitch.setListenerValue(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ProductDeliveryInfoViewHolder.this.listener.onFreeReturnChecked(isChecked);
            }
        });

        setListener(listener);
    }

    @Override
    public void renderData(ProductViewModel model) {
        setWeightUnit((int) model.getProductWeightUnit());
        if (model.getProductWeight() > 0) {
            setWeightValue((int) model.getProductWeight());
        }
        setInsurance(model.isProductMustInsurance());
        setFreeReturn(model.isProductFreeReturn());
    }

    @Override
    public void updateModel(ProductViewModel model) {
        model.setProductWeightUnit(getWeightUnit());
        model.setProductWeight(getWeightValue());
        model.setProductMustInsurance(isMustInsurance());
        model.setProductFreeReturn(isFreeReturns());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void showViewFreeReturn(boolean isFreeReturn) {
        if (isFreeReturn) {
            freeReturnsSwitch.setVisibility(View.VISIBLE);
        } else {
            freeReturnsSwitch.setVisibility(View.GONE);
        }
        listener.onFreeReturnChecked(freeReturnsSwitch.isChecked());
    }

    private int getWeightUnit() {
        return Integer.parseInt(weightSpinnerCounterInputView.getSpinnerValue());
    }

    private void setWeightUnit(int unit) {
        weightSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    private int getWeightValue() {
        return (int) weightSpinnerCounterInputView.getCounterValue();
    }

    private void setWeightValue(int value) {
        weightSpinnerCounterInputView.setCounterValue(value);
    }

    private boolean isFreeReturns() {
        return freeReturnsSwitch.getVisibility() == View.VISIBLE && freeReturnsSwitch.isChecked();
    }

    private void setFreeReturn(boolean isFreeReturn) {
        if (isFreeReturn) {
            freeReturnsSwitch.setChecked(true);
        } else {
            freeReturnsSwitch.setChecked(false);
        }
    }

    private boolean isMustInsurance() {
        return insuranceSwitch.isChecked();
    }

    public void setInsurance(boolean isMustInsurance) {
        if (isMustInsurance) {
            insuranceSwitch.setChecked(true);
        } else {
            insuranceSwitch.setChecked(false);
        }
    }

    private boolean isWeightValid() {
        String minWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_minimum_weight_gram));
        String maxWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_maximum_weight_gram));
        if (weightSpinnerCounterInputView.getSpinnerValue().equalsIgnoreCase(weightSpinnerCounterInputView.getContext().getString(R.string.product_weight_value_kg))) {
            minWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_minimum_weight_kg));
            maxWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_maximum_weight_kg));
        }
        double minWeight = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minWeightString));
        double maxWeight = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxWeightString));
        if (minWeight > getWeightValue() || getWeightValue() > maxWeight) {
            weightSpinnerCounterInputView.setCounterError(weightSpinnerCounterInputView.getContext().getString(R.string.product_error_product_weight_not_valid, minWeightString, maxWeightString));
            return false;
        }
        return true;
    }

    @Override
    public boolean isDataValid() {
        if (!isWeightValid()) {
            weightSpinnerCounterInputView.requestFocus();
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT);
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // no need; already on the model
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        // no need; already on the model
    }

    /**
     * @author normansyahputa on 4/18/17.
     *         <p>
     *         this represent contract for {@link ProductDeliveryInfoViewHolder}
     *         <p>
     *         for example calling {@link ProductAddFragment#startActivityForResult(Intent, int)}
     *         this will delefate to {@link ProductAddFragment} for doing that
     */
    public interface Listener {
        void onFreeReturnChecked(boolean checked);
    }
}
