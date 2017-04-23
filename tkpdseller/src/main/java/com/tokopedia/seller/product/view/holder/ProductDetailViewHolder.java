package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;
import com.tokopedia.seller.util.CurrencyTextWatcher;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDetailViewHolder extends ProductViewHolder {

    public interface Listener {

        void onUSDClickedNotAllowed();

        /**
         * @param baseValue means for single price tag.
         */
        void startAddWholeSaleDialog(WholesaleModel baseValue);

        void onTotalStockUpdated(int total);

        void onEtalaseViewClicked();

        void onFreeReturnChecked(boolean checked);
    }

    public static final int REQUEST_CODE_ETALASE = 301;
    public static final int POSITION_IDR = 0;
    public static final int POSITION_USD = 1;

    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private SpinnerCounterInputView weightSpinnerCounterInputView;
    private CounterInputView minimumOrderCounterInputView;
    private SpinnerTextView stockStatusSpinnerTextView;
    private ExpandableOptionSwitch stockTotalExpandableOptionSwitch;
    private CounterInputView stockTotalCounterInputView;
    private LabelView etalaseLabelView;
    private SpinnerTextView conditionSpinnerTextView;
    private SpinnerTextView insuranceSpinnerTextView;
    private SpinnerTextView freeReturnsSpinnerTextView;

    private TextView textViewAddWholesale;

    private long etalaseId;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ProductDetailViewHolder(View view) {
        etalaseId = -1;
        priceSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_price);
        weightSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_weight);
        minimumOrderCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_order);
        stockStatusSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_stock_status);
        stockTotalExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_stock_total);
        stockTotalCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_stock_total);
        etalaseLabelView = (LabelView) view.findViewById(R.id.label_view_shop_window);
        conditionSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_condition);
        insuranceSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_insurance);
        freeReturnsSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_free_returns);
        priceSpinnerCounterInputView.addTextChangedListener(new CurrencyTextWatcher(priceSpinnerCounterInputView.getCounterEditText(), priceSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)) {
            @Override
            public void onCurrencyChanged(float currencyValue) {
                if (isPriceValid()) {
                    priceSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        weightSpinnerCounterInputView.addTextChangedListener(new CurrencyTextWatcher(weightSpinnerCounterInputView.getCounterEditText(), weightSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)) {
            @Override
            public void onCurrencyChanged(float currencyValue) {
                if (isWeightValid()) {
                    weightSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        weightSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weightSpinnerCounterInputView.setCounterValue(Float.parseFloat(priceSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                priceSpinnerCounterInputView.post(new Runnable() {
                    @Override
                    public void run() {
                        isWeightValid();
                    }
                });
            }
        });
        minimumOrderCounterInputView.addTextChangedListener(new CurrencyTextWatcher(minimumOrderCounterInputView.getEditText(), minimumOrderCounterInputView.getContext().getString(R.string.product_default_counter_minimum_order_text)) {
            @Override
            public void onCurrencyChanged(float currencyValue) {
                if (currencyValue > 0) {
                    minimumOrderCounterInputView.setError(null);
                }
            }
        });
        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onEtalaseViewClicked();
                }
            }
        });
        if (view.getContext() != null && view.getContext() instanceof Listener) {
            listener = (Listener) view.getContext();
        }
        textViewAddWholesale = (TextView) view.findViewById(R.id.text_view_add_wholesale);
        textViewAddWholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.startAddWholeSaleDialog(getBaseValue());
                }
            }
        });

        freeReturnsSpinnerTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onFreeReturnChecked(false);
            }
        });
        stockTotalExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                listener.onTotalStockUpdated(isExpand ? (int) stockTotalCounterInputView.getFloatValue() : 0);
            }
        });

        stockTotalCounterInputView.addTextChangedListener(new CurrencyTextWatcher(stockTotalCounterInputView.getEditText(), stockTotalCounterInputView.getContext().getString(R.string.product_default_counter_text)) {
            @Override
            public void onCurrencyChanged(float currencyValue) {
                listener.onTotalStockUpdated((int) currencyValue);
                if (currencyValue > 0) {
                    stockTotalCounterInputView.setError(null);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        etalaseId = data.getIntExtra(EtalasePickerActivity.ETALASE_ID, -1);
        String etalaseName = data.getStringExtra(EtalasePickerActivity.ETALASE_NAME);
        etalaseLabelView.setContent(etalaseName);
    }

    public void updateViewGoldMerchant(final boolean isGoldMerchant) {
        priceSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isGoldMerchant) {
                    priceSpinnerCounterInputView.setCounterValue(Float.parseFloat(priceSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                    priceSpinnerCounterInputView.post(new Runnable() {
                        @Override
                        public void run() {
                            isPriceValid();
                        }
                    });
                    return;
                }
                if (position == POSITION_USD) {
                    if (listener != null) {
                        listener.onUSDClickedNotAllowed();
                    }
                    priceSpinnerCounterInputView.post(new Runnable() {
                        @Override
                        public void run() {
                            priceSpinnerCounterInputView.setSpinnerPosition(POSITION_IDR);
                        }
                    });
                }
            }
        });
    }

    public void updateViewFreeReturn(boolean isFreeReturn) {
        if (isFreeReturn) {
            freeReturnsSpinnerTextView.setVisibility(View.VISIBLE);
        } else {
            freeReturnsSpinnerTextView.setVisibility(View.GONE);
        }
    }

    public int getPriceCurrency() {
        return Integer.parseInt(priceSpinnerCounterInputView.getSpinnerValue());
    }

    public float getPriceValue() {
        return priceSpinnerCounterInputView.getCounterValue();
    }

    public void setPriceCurrency(int unit) {
        priceSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public void setPriceValue(float price) {
        priceSpinnerCounterInputView.setCounterValue(price);
    }

    public int getWeightUnit() {
        return Integer.parseInt(weightSpinnerCounterInputView.getSpinnerValue());
    }

    public int getWeightValue() {
        return (int) weightSpinnerCounterInputView.getCounterValue();
    }

    public void setWeightUnit(int unit) {
        weightSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public void setWeightValue(float value) {
        weightSpinnerCounterInputView.setCounterValue(value);
    }

    public int getMinimumOrder() {
        return (int) minimumOrderCounterInputView.getFloatValue();
    }

    public void setMinimumOrder(float value) {
        minimumOrderCounterInputView.setValue(value);
    }

    public int getStatusStock() {
        return Integer.parseInt(stockStatusSpinnerTextView.getSpinnerValue());
    }

    public void setStockStatus(int unit) {
        stockStatusSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public void setTotalStock(float value) {
        stockTotalCounterInputView.setValue(value);
    }

    public int getTotalStock() {
        return (int) stockTotalCounterInputView.getFloatValue();
    }

    public int getCondition() {
        return Integer.parseInt(conditionSpinnerTextView.getSpinnerValue());
    }

    public void setCondition(int unit) {
        conditionSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public int getInsurance() {
        return Integer.parseInt(insuranceSpinnerTextView.getSpinnerValue());
    }

    public void setInsurance(int unit) {
        insuranceSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public int getFreeReturns() {
        return Integer.parseInt(freeReturnsSpinnerTextView.getSpinnerValue());
    }

    public void setFreeReturn(int unit) {
        freeReturnsSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public void setEtalaseName(String name) {
        this.etalaseLabelView.setContent(name);
    }

    public void addWholesaleItem(WholesaleModel wholesaleModel) {
        // TODO insert into wholesale adapter.
    }

    private WholesaleModel getBaseValue() {
        return new WholesaleModel(
                1, 1, getPriceValue()
        );
    }

    private boolean isPriceValid() {
        String minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(priceSpinnerCounterInputView.getContext().getString(R.string.product_minimum_price_rp));
        String maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(priceSpinnerCounterInputView.getContext().getString(R.string.product_maximum_price_rp));
        if (priceSpinnerCounterInputView.getSpinnerValue().equalsIgnoreCase(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_usd))) {
            minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(priceSpinnerCounterInputView.getContext().getString(R.string.product_minimum_price_usd));
            maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(priceSpinnerCounterInputView.getContext().getString(R.string.product_maximum_price_usd));
        }
        float minPrice = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(minPriceString));
        float maxPrice = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(maxPriceString));
        if (minPrice > getPriceValue() || getPriceValue() > maxPrice) {
            priceSpinnerCounterInputView.setCounterError(priceSpinnerCounterInputView.getContext().getString(R.string.product_error_product_price_not_valid, minPriceString, maxPriceString));
            priceSpinnerCounterInputView.clearFocus();
            priceSpinnerCounterInputView.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isWeightValid() {
        String minWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_minimum_weight_gram));
        String maxWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_maximum_weight_gram));
        if (weightSpinnerCounterInputView.getSpinnerValue().equalsIgnoreCase(weightSpinnerCounterInputView.getContext().getString(R.string.product_weight_value_kg))) {
            minWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_minimum_weight_kg));
            maxWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_maximum_weight_kg));
        }
        float minWeight = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(minWeightString));
        float maxWeight = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(maxWeightString));
        if (minWeight > getWeightValue() || getWeightValue() > maxWeight) {
            weightSpinnerCounterInputView.setCounterError(weightSpinnerCounterInputView.getContext().getString(R.string.product_error_product_weight_not_valid, minWeightString, maxWeightString));
            weightSpinnerCounterInputView.clearFocus();
            weightSpinnerCounterInputView.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean isDataValid() {
        if (!isPriceValid()) {
            return false;
        }
        if (!isWeightValid()) {
            return false;
        }
        if (getMinimumOrder() <= 0) {
            minimumOrderCounterInputView.setError(minimumOrderCounterInputView.getContext().getString(R.string.product_error_product_minimum_order_not_valid));
            minimumOrderCounterInputView.clearFocus();
            minimumOrderCounterInputView.requestFocus();
            return false;
        }
        if (getEtalaseId() < 0) {
            Snackbar.make(etalaseLabelView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_etalase_empty, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(etalaseLabelView.getContext(), R.color.green_400))
                    .show();
            return false;
        }
        return true;
    }
}