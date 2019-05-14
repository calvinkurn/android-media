package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.design.price.PriceRangeInputView;
import com.tokopedia.design.text.RangeInputView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;
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
    private Option priceMinOption;
    private Option priceMaxOption;
    private Option priceMinMaxOption;

    public DynamicFilterItemPriceViewHolder(View itemView, final DynamicFilterView dynamicFilterView) {
        super(itemView);
        this.dynamicFilterView = dynamicFilterView;

        wholesaleTitle = itemView.findViewById(R.id.wholesale_title);
        wholesaleToggle = itemView.findViewById(R.id.wholesale_toggle);
        wholesaleContainer = itemView.findViewById(R.id.wholesale_container);
        priceRangeInputView = itemView.findViewById(R.id.price_range_input_view);
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
            String optionValue = dynamicFilterView.getFilterValue(option.getKey());

            if (Option.KEY_PRICE_MIN_MAX_RANGE.equals(option.getKey())) {
                minBound = TextUtils.isEmpty(option.getValMin()) ? 0 : Integer.parseInt(option.getValMin());
                maxBound = TextUtils.isEmpty(option.getValMax()) ? 0 : Integer.parseInt(option.getValMax());
                priceMinMaxOption = option;
            }

            if (Option.KEY_PRICE_MIN.equals(option.getKey())) {
                minLabel = option.getName();
                lastMinValue = TextUtils.isEmpty(optionValue) ? 0 : Integer.parseInt(optionValue);
                priceMinOption = option;
            }

            if (Option.KEY_PRICE_MAX.equals(option.getKey())) {
                maxLabel = option.getName();
                lastMaxValue = TextUtils.isEmpty(optionValue) ? 0 : Integer.parseInt(optionValue);
                priceMaxOption = option;
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

        priceRangeInputView.setGestureListener(getPriceRangeInputViewGestureListener());

        priceRangeInputView.setOnValueChangedListener(getPriceRangeInputViewOnValueChangeListener());

        priceRangeInputView.setData(minLabel, maxLabel, minBound, maxBound,
                defaultMinValue, defaultMaxValue);
    }

    private PriceRangeInputView.GestureListener getPriceRangeInputViewGestureListener() {
        return new RangeInputView.GestureListener() {
            @Override
            public void onButtonRelease(int minValue, int maxValue) {
                dynamicFilterView.onPriceSliderRelease(minValue, maxValue);
            }

            @Override
            public void onButtonPressed(int minValue, int maxValue) {
                dynamicFilterView.onPriceSliderPressed(minValue, maxValue);
            }

            @Override
            public void onValueEditedFromTextInput(int minValue, int maxValue) {
                dynamicFilterView.onPriceEditedFromTextInput(minValue, maxValue);
            }
        };
    }

    private PriceRangeInputView.OnValueChangedListener getPriceRangeInputViewOnValueChangeListener() {
        return (minValue, maxValue, minBound, maxBound) -> {
            applyMinValueFilter(minValue, minBound);

            applyMaxValueFilter(maxValue, maxBound);
        };
    }

    private void applyMinValueFilter(int minValue, int minBound) {
        if (minValue == minBound) {
            dynamicFilterView.removeSavedTextInput(priceMinOption.getUniqueId());
        } else {
            dynamicFilterView.saveTextInput(priceMinOption.getUniqueId(), String.valueOf(minValue));
        }
    }

    private void applyMaxValueFilter(int maxValue, int maxBound) {
        if (maxValue == maxBound) {
            dynamicFilterView.removeSavedTextInput(priceMaxOption.getUniqueId());
        } else {
            dynamicFilterView.saveTextInput(priceMaxOption.getUniqueId(), String.valueOf(maxValue));
        }
    }

    private void bindWholesaleOptionItem(final Option option) {
        wholesaleContainer.setVisibility(View.VISIBLE);
        wholesaleTitle.setText(option.getName());

        wholesaleTitle.setOnClickListener(v -> wholesaleToggle.setChecked(!wholesaleToggle.isChecked()));

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                (buttonView, isChecked) -> dynamicFilterView.saveCheckedState(option, isChecked);

        bindSwitch(wholesaleToggle,
                dynamicFilterView.loadLastCheckedState(option),
                onCheckedChangeListener);
    }
}
