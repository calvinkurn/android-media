package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.view.fragment.ProductAddView;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDetailViewHolder {

    public static final int REQUEST_CODE_ETALASE = 301;

    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private SpinnerCounterInputView weightSpinnerCounterInputView;
    private CounterInputView minimumOrderCounterInputView;
    private SpinnerTextView stockStatusSpinnerTextView;
    private CounterInputView stockTotalCounterInputView;
    private LabelView etalaseLabelView;
    private SpinnerTextView conditionSpinnerTextView;
    private SpinnerTextView insuranceSpinnerTextView;
    private SpinnerTextView freeReturnsSpinnerTextView;
    private ProductAddView productAddView;

    private Fragment fragment;

    public ProductDetailViewHolder(final Fragment fragment, View view, final ProductAddView productAddView) {
        this.fragment = fragment;
        this.productAddView = productAddView;
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

        freeReturnsSpinnerTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productAddView.updateProductScoring();
            }
        });
        stockTotalCounterInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                productAddView.updateProductScoring();
            }
        });
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
}