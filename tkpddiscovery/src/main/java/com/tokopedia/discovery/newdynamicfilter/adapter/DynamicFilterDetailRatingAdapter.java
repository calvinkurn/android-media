package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.RatingHelper;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;

/**
 * Created by henrypriyono on 8/24/17.
 */

public class DynamicFilterDetailRatingAdapter extends DynamicFilterDetailAdapter {
    @Override
    protected int getLayout() {
        return R.layout.filter_detail_rating;
    }

    @Override
    protected AbstractViewHolder<Option> getViewHolder(View view) {
        return new RatingItemViewHolder(view);
    }

    private static class RatingItemViewHolder extends AbstractViewHolder<Option> {

        private ImageView ratingView;
        private CheckBox checkBox;

        public RatingItemViewHolder(View itemView) {
            super(itemView);
            ratingView = (ImageView) itemView.findViewById(R.id.filter_rating_view);
            checkBox = (CheckBox) itemView.findViewById(R.id.rating_checkbox);
        }

        @Override
        public void bind(final Option option) {
            int ratingCount = Integer.parseInt(option.getName());
            ratingView.setImageResource(RatingHelper.getRatingDrawable(ratingCount));

            OptionHelper.bindOptionWithCheckbox(option, checkBox);
        }
    }
}
