package com.tokopedia.seller.product.view.holder;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDetailViewHolder {

    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private SpinnerCounterInputView weightSpinnerCounterInputView;
    private CounterInputView minimumBuyCounterInputView;
    private SpinnerTextView stockStatusSpinnerTextView;
    private CounterInputView stockTotalCounterInputView;
    private LabelView etalaseLabelView;
    private SpinnerTextView conditionSpinnerTextView;
    private SpinnerTextView insuranceSpinnerTextView;
    private SpinnerTextView freeReturnsSpinnerTextView;

    public ProductDetailViewHolder(View view) {
        priceSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_price);
        weightSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_weight);
        minimumBuyCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_buy);
        stockStatusSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_stock_status);
        stockTotalCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_stock_total);
        etalaseLabelView = (LabelView) view.findViewById(R.id.label_view_shop_window);
        conditionSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_condition);
        insuranceSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_insurance);
        freeReturnsSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_free_returns);
    }
}
