package com.tokopedia.seller.product.edit.view.holder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.product.edit.constant.FreeReturnTypeDef;
import com.tokopedia.seller.product.edit.constant.ProductInsuranceValueTypeDef;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreorderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDeliveryInfoViewHolder extends ProductViewHolder {

    public static final int INACTIVE_PREORDER = -1;
    public static final int PREORDER_STATUS_ACTIVE = 1;

    private ExpandableOptionSwitch preOrderExpandableOptionSwitch;
    private SpinnerCounterInputView preOrderSpinnerCounterInputView;
    private LabelSwitch shareLabelSwitch;
    private Listener listener;

    private SpinnerCounterInputView weightSpinnerCounterInputView;
    private SpinnerTextView insuranceSpinnerTextView;
    private SpinnerTextView freeReturnsSpinnerTextView;

    public ProductDeliveryInfoViewHolder(View view, Listener listener) {
        preOrderExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_pre_order);
        preOrderSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_pre_order);
        shareLabelSwitch = (LabelSwitch) view.findViewById(R.id.label_switch_share);

        preOrderExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                if (!isExpand) {
                    preOrderSpinnerCounterInputView.setCounterValue(Double.parseDouble(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                }
            }
        });
        preOrderSpinnerCounterInputView.addTextChangedListener(new NumberTextWatcher(preOrderSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                if (isPreOrderValid()) {
                    preOrderSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        preOrderSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preOrderSpinnerCounterInputView.setCounterValue(Double.parseDouble(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                preOrderSpinnerCounterInputView.setCounterError(null);
            }
        });


        weightSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_weight);

        insuranceSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_insurance);
        freeReturnsSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_free_returns);

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

        freeReturnsSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                ProductDeliveryInfoViewHolder.this.listener.onFreeReturnChecked(value.equals(freeReturnsSpinnerTextView.getContext().getString(R.string.product_free_return_values_active)));
            }
        });

        setListener(listener);
    }

    @Override
    public void renderData(ProductViewModel model) {
        setWeightUnit((int)model.getProductWeightUnit());
        if (model.getProductWeight() > 0) {
            setWeightValue((int)model.getProductWeight());
        }
        setInsurance(model.isProductMustInsurance());
        setFreeReturn(model.isProductFreeReturn());
        if (model.getProductPreorder().getPreorderProcessTime() > 0) {
            expandPreOrder(true);
            setPreOrderUnit((int)model.getProductPreorder().getPreorderTimeUnit());
            setPreOrderValue((int)model.getProductPreorder().getPreorderProcessTime());
        }
    }

    @Override
    public void updateModel(ProductViewModel model) {
        model.setProductWeightUnit(getWeightUnit());
        model.setProductWeight(getWeightValue());
        model.setProductMustInsurance(isMustInsurance());
        model.setProductFreeReturn(isFreeReturns());
        model.setProductPreorder(getPreOrder());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void updateViewFreeReturn(boolean isFreeReturn) {
        if (isFreeReturn) {
            freeReturnsSpinnerTextView.setVisibility(View.VISIBLE);
        } else {
            freeReturnsSpinnerTextView.setVisibility(View.GONE);
        }
        listener.onFreeReturnChecked(getFreeReturns() == Integer.parseInt(freeReturnsSpinnerTextView.getContext().getString(R.string.product_free_return_values_active)));
    }

    public int getWeightUnit() {
        return Integer.parseInt(weightSpinnerCounterInputView.getSpinnerValue());
    }

    public void setWeightUnit(int unit) {
        weightSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public int getWeightValue() {
        return (int) weightSpinnerCounterInputView.getCounterValue();
    }

    public void setWeightValue(int value) {
        weightSpinnerCounterInputView.setCounterValue(value);
    }

    public void expandPreOrder(boolean expand) {
        preOrderExpandableOptionSwitch.setExpand(expand);
    }

    public int getPreOrderUnit() {
        if (preOrderExpandableOptionSwitch.isExpanded()) {
            return Integer.parseInt(preOrderSpinnerCounterInputView.getSpinnerValue());
        } else {
            return INACTIVE_PREORDER;
        }
    }

    public ProductPreorderViewModel getPreOrder() {
        ProductPreorderViewModel productPreorderViewModel = new ProductPreorderViewModel();
        if(getPreOrderValue() > 0) {
            productPreorderViewModel.setPreorderStatus(PREORDER_STATUS_ACTIVE);
            productPreorderViewModel.setPreorderProcessTime(getPreOrderValue());
            productPreorderViewModel.setPreorderTimeUnit(getPreOrderUnit());
        }else{
            productPreorderViewModel.setPreorderStatus(INACTIVE_PREORDER);
        }
        return productPreorderViewModel;
    }

    public void setPreOrderUnit(int unit) {
        preOrderSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public int getPreOrderValue() {
        if (preOrderExpandableOptionSwitch.isExpanded()) {
            return (int) preOrderSpinnerCounterInputView.getCounterValue();
        } else {
            return INACTIVE_PREORDER;
        }
    }

    public void setPreOrderValue(int value) {
        preOrderSpinnerCounterInputView.setCounterValue(value);
    }

    public boolean isShare() {
        return shareLabelSwitch.isChecked();
    }

    private boolean isPreOrderValid() {
        if (!preOrderExpandableOptionSwitch.isExpanded()) {
            return true;
        }
        String minPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_minimum_pre_order_day));
        String maxPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_maximum_pre_order_day));
        if (preOrderSpinnerCounterInputView.getSpinnerValue().equalsIgnoreCase(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_pre_order_value_week))) {
            minPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_minimum_pre_order_week));
            maxPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_maximum_pre_order_week));
        }
        double minValue = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minPreOrderString));
        double maxValue = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxPreOrderString));
        if (minValue > getPreOrderValue() || getPreOrderValue() > maxValue) {
            preOrderSpinnerCounterInputView.setCounterError(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_error_product_pre_order_not_valid, minPreOrderString, maxPreOrderString));
            preOrderSpinnerCounterInputView.clearFocus();
            preOrderSpinnerCounterInputView.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isFreeReturns() {
        return getFreeReturns() == FreeReturnTypeDef.TYPE_ACTIVE;
    }

    public void setFreeReturn(boolean isFreeReturn) {
        if(isFreeReturn) {
            freeReturnsSpinnerTextView.setSpinnerValue(String.valueOf(FreeReturnTypeDef.TYPE_ACTIVE));
        }else{
            freeReturnsSpinnerTextView.setSpinnerValue(String.valueOf(FreeReturnTypeDef.TYPE_INACTIVE));
        }
    }

    public boolean isMustInsurance() {
        return Integer.valueOf(insuranceSpinnerTextView.getSpinnerValue()) == ProductInsuranceValueTypeDef.TYPE_YES;
    }

    public void setInsurance(boolean isMustInsurance) {
        if(isMustInsurance) {
            insuranceSpinnerTextView.setSpinnerValue(String.valueOf(ProductInsuranceValueTypeDef.TYPE_YES));
        }else{
            insuranceSpinnerTextView.setSpinnerValue(String.valueOf(ProductInsuranceValueTypeDef.TYPE_OPTIONAL));
        }
    }

    public int getFreeReturns() {
        if (freeReturnsSpinnerTextView.getVisibility() != View.VISIBLE || freeReturnsSpinnerTextView.getSpinnerValue() == null) {
            return Integer.parseInt(freeReturnsSpinnerTextView.getContext().getString(R.string.product_free_return_values_inactive));
        } else {
            return Integer.parseInt(freeReturnsSpinnerTextView.getSpinnerValue());
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
    public Pair<Boolean, String> isDataValid() {
        if (!isWeightValid()) {
            weightSpinnerCounterInputView.requestFocus();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT);
        }
        if (!isPreOrderValid()) {
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER);
        }
        return new Pair<>(true, "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {

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
