package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.price.PriceRangeInputView;
import com.tokopedia.discovery.R;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemPriceViewHolder extends DynamicFilterViewHolder {

    private TextView wholesaleTitle;
    private Switch wholesaleToggle;
    private PriceRangeInputView sliderView;

    public DynamicFilterItemPriceViewHolder(View itemView) {
        super(itemView);
        wholesaleTitle = (TextView) itemView.findViewById(R.id.wholesale_title);
        wholesaleToggle = (Switch) itemView.findViewById(R.id.wholesale_toggle);
        sliderView = (PriceRangeInputView) itemView.findViewById(R.id.slider);
    }

    @Override
    public void bind(Filter filter) {
        int minBound = 0;
        int maxBound = 0;
        String maxLabel = "";
        String minLabel = "";

        for (Option option : filter.getOptions()) {
            if (!TextUtils.isEmpty(option.getValMin())) {
                minBound = Integer.parseInt(option.getValMin());
            }

            if (!TextUtils.isEmpty(option.getValMax())) {
                maxBound = Integer.parseInt(option.getValMax());
            }

            if (Option.PRICE_MIN_LABEL_KEY.equals(option.getKey())) {
                minLabel = option.getName();
            }

            if (Option.PRICE_MAX_LABEL_KEY.equals(option.getKey())) {
                maxLabel = option.getName();
            }

            if (Option.PRICE_WHOLESALE_KEY.equals(option.getKey())) {
                wholesaleTitle.setText(option.getName());
                wholesaleToggle.setChecked(Boolean.parseBoolean(option.getValue()));
            }
        }

        sliderView.setData(minLabel, maxLabel, minBound, maxBound);
    }
}
