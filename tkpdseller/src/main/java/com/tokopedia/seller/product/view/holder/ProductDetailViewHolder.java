package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDetailViewHolder extends ProductViewHolder {

    public interface Listener {

        /**
         * @param baseValue means for single price tag.
         */
        void startAddWholeSaleDialog(WholesaleModel baseValue);
    }

    public static final int REQUEST_CODE_ETALASE = 301;
    private Listener listener;

    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private SpinnerCounterInputView weightSpinnerCounterInputView;
    private CounterInputView minimumOrderCounterInputView;
    private SpinnerTextView stockStatusSpinnerTextView;
    private CounterInputView stockTotalCounterInputView;
    private LabelView etalaseLabelView;
    private SpinnerTextView conditionSpinnerTextView;
    private SpinnerTextView insuranceSpinnerTextView;
    private SpinnerTextView freeReturnsSpinnerTextView;

    private TextView textViewAddWholesale;

    private Fragment fragment;
    private int etalaseId;

    public ProductDetailViewHolder(final Fragment fragment, View view) {
        this.fragment = fragment;
        priceSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_price);
        weightSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_weight);
        minimumOrderCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_order);
        stockStatusSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_stock_status);
        stockTotalCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_stock_total);
        etalaseLabelView = (LabelView) view.findViewById(R.id.label_view_shop_window);
        conditionSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_condition);
        insuranceSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_insurance);
        freeReturnsSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_free_returns);
        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment.getActivity(), EtalasePickerActivity.class);
                fragment.startActivityForResult(intent, REQUEST_CODE_ETALASE);
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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        etalaseId = data.getIntExtra(EtalasePickerActivity.ETALASE_ID, -1);
        String etalaseName = data.getStringExtra(EtalasePickerActivity.ETALASE_NAME);
        etalaseLabelView.setContent(etalaseName);
    }

    public int getPriceCurrency() {
        return Integer.parseInt(priceSpinnerCounterInputView.getSpinnerValue());
    }

    public float getPriceValue() {
        return priceSpinnerCounterInputView.getCounterValue();
    }

    public int getWeightUnit() {
        return Integer.parseInt(weightSpinnerCounterInputView.getSpinnerValue());
    }

    public int getWeightValue() {
        return (int) weightSpinnerCounterInputView.getCounterValue();
    }

    public int getMinimumOrder() {
        return (int) minimumOrderCounterInputView.getFloatValue();
    }

    public int getStatusStock() {
        return Integer.parseInt(stockStatusSpinnerTextView.getSpinnerValue());
    }

    public int getTotalStock() {
        return (int) stockTotalCounterInputView.getFloatValue();
    }

    public int getCondition() {
        return Integer.parseInt(conditionSpinnerTextView.getSpinnerValue());
    }

    public int getInsurance() {
        return Integer.parseInt(insuranceSpinnerTextView.getSpinnerValue());
    }

    public int getFreeReturns() {
        return Integer.parseInt(freeReturnsSpinnerTextView.getSpinnerValue());
    }

    public int getEtalaseId() {
        return etalaseId;
    }

    public void addWholesaleItem(WholesaleModel wholesaleModel) {
        // TODO insert into wholesale adapter.
    }

    private WholesaleModel getBaseValue() {
        return new WholesaleModel(
                1, 1, getPriceValue()
        );
    }

    @Override
    public boolean isDataValid() {
        if (getPriceValue() < 0) {
            Snackbar.make(priceSpinnerCounterInputView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_price_empty, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(priceSpinnerCounterInputView.getContext(), R.color.green_400))
                    .show();
            return false;
        }
        if (getWeightValue() < 0) {
            Snackbar.make(weightSpinnerCounterInputView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_weight_not_valid, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(weightSpinnerCounterInputView.getContext(), R.color.green_400))
                    .show();
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