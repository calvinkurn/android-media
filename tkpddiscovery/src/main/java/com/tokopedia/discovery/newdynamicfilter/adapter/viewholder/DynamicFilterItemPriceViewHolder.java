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
    private View wholesaleContainer;
    private PriceRangeInputView priceRangeInputView;
    private final DynamicFilterView dynamicFilterView;

    public DynamicFilterItemPriceViewHolder(View itemView, final DynamicFilterView dynamicFilterView) {
        super(itemView);
        this.dynamicFilterView = dynamicFilterView;
        wholesaleTitle = (TextView) itemView.findViewById(R.id.wholesale_title);
        wholesaleToggle = (SwitchCompat) itemView.findViewById(R.id.wholesale_toggle);
        wholesaleContainer = itemView.findViewById(R.id.wholesale_container);
        priceRangeInputView = (PriceRangeInputView) itemView.findViewById(R.id.price_range_input_view);
    }

    @Override
    public void bind(Filter filter) {
        int minBound = 0;
        int maxBound = 0;
        String maxLabel = "";
        String minLabel = "";

        wholesaleContainer.setVisibility(View.GONE);

        int lastMinValue = 0;
        int lastMaxValue = 0;

        for (Option option : filter.getOptions()) {
            if (Option.KEY_PRICE_MIN_MAX_RANGE.equals(option.getKey())) {
                minBound = TextUtils.isEmpty(option.getValMin()) ? 0 : Integer.parseInt(option.getValMin());
                maxBound = TextUtils.isEmpty(option.getValMax()) ? 0 : Integer.parseInt(option.getValMax());
            }

            if (Option.KEY_PRICE_MIN.equals(option.getKey())) {
                minLabel = option.getName();
                lastMinValue = TextUtils.isEmpty(option.getValue()) ? 0 : Integer.parseInt(option.getValue());
            }

            if (Option.KEY_PRICE_MAX.equals(option.getKey())) {
                maxLabel = option.getName();
                lastMaxValue = TextUtils.isEmpty(option.getValue()) ? 0 : Integer.parseInt(option.getValue());
            }

            if (Option.KEY_PRICE_WHOLESALE.equals(option.getKey())) {
                bindWholesaleOptionItem(option);
            }
        }

        int defaultMinValue;
        if (lastMinValue == 0) {
            defaultMinValue = minBound;
        } else {
            defaultMinValue = lastMinValue;
        }

        int defaultMaxValue;
        if (lastMaxValue == 0) {
            defaultMaxValue = maxBound;
        } else {
            defaultMaxValue = lastMaxValue;
        }

        priceRangeInputView.setOnValueChangedListener(new PriceRangeInputView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue, int minBound, int maxBound) {
                if (minValue == minBound) {
                    dynamicFilterView.removeSavedTextInput(Option.KEY_PRICE_MIN);
                } else {
                    dynamicFilterView.saveTextInput(Option.KEY_PRICE_MIN, String.valueOf(minValue));
                }

                if (maxValue == maxBound) {
                    dynamicFilterView.removeSavedTextInput(Option.KEY_PRICE_MAX);
                } else {
                    dynamicFilterView.saveTextInput(Option.KEY_PRICE_MAX, String.valueOf(maxValue));
                }

                dynamicFilterView.updateLastRangeValue(minValue, maxValue);
            }
        });

        priceRangeInputView.setData(minLabel, maxLabel, minBound, maxBound,
                defaultMinValue, defaultMaxValue);
    }

    private void bindWholesaleOptionItem(final Option option) {
        wholesaleContainer.setVisibility(View.VISIBLE);
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
