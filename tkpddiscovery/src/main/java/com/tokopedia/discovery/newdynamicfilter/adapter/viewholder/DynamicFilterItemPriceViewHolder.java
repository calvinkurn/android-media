package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.price.PriceRangeInputView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemPriceViewHolder extends DynamicFilterViewHolder {

    private TextView wholesaleTitle;
    private SwitchCompat wholesaleToggle;
    private PriceRangeInputView priceRangeInputView;
    private final DynamicFilterView dynamicFilterView;

    public DynamicFilterItemPriceViewHolder(View itemView, final DynamicFilterView dynamicFilterView) {
        super(itemView);
        this.dynamicFilterView = dynamicFilterView;
        wholesaleTitle = (TextView) itemView.findViewById(R.id.wholesale_title);
        wholesaleToggle = (SwitchCompat) itemView.findViewById(R.id.wholesale_toggle);
        priceRangeInputView = (PriceRangeInputView) itemView.findViewById(R.id.price_range_input_view);
    }

    @Override
    public void bind(Filter filter) {
        int minBound = 0;
        int maxBound = 0;
        String maxLabel = "";
        String minLabel = "";

        for (Option option : filter.getOptions()) {
            if (Option.KEY_PRICE_MIN_MAX_RANGE.equals(option.getKey())) {
                minBound = TextUtils.isEmpty(option.getValMin()) ? 0 : Integer.parseInt(option.getValMin());
                maxBound = TextUtils.isEmpty(option.getValMax()) ? 0 : Integer.parseInt(option.getValMax());
            }

            if (Option.KEY_PRICE_MIN.equals(option.getKey())) {
                minLabel = option.getName();
            }

            if (Option.KEY_PRICE_MAX.equals(option.getKey())) {
                maxLabel = option.getName();
            }

            if (Option.KEY_PRICE_WHOLESALE.equals(option.getKey())) {
                bindWholesaleOptionItem(option);
            }
        }

        String savedMinValue = dynamicFilterView.loadLastTextInput(Option.KEY_PRICE_MIN);
        int defaultMinValue;
        if (TextUtils.isEmpty(savedMinValue)) {
            defaultMinValue = minBound;
        } else {
            defaultMinValue = Integer.parseInt(savedMinValue);
        }

        String savedMaxValue = dynamicFilterView.loadLastTextInput(Option.KEY_PRICE_MAX);
        int defaultMaxValue;
        if (TextUtils.isEmpty(savedMaxValue)) {
            defaultMaxValue = maxBound;
        } else {
            defaultMaxValue = Integer.parseInt(savedMaxValue);
        }

        priceRangeInputView.setOnValueChangedListener(new PriceRangeInputView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue) {
                dynamicFilterView.saveTextInput(Option.KEY_PRICE_MIN, String.valueOf(minValue));
                dynamicFilterView.saveTextInput(Option.KEY_PRICE_MAX, String.valueOf(maxValue));
            }
        });

        priceRangeInputView.setData(minLabel, maxLabel, minBound, maxBound,
                defaultMinValue, defaultMaxValue);
    }

    private void bindWholesaleOptionItem(final Option option) {
        wholesaleTitle.setText(option.getName());

        wholesaleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholesaleToggle.setChecked(!wholesaleToggle.isChecked());
            }
        });

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dynamicFilterView.saveCheckedState(option, isChecked);
                    }
                };

        bindSwitch(wholesaleToggle,
                dynamicFilterView.loadLastCheckedState(option),
                onCheckedChangeListener);
    }
}
