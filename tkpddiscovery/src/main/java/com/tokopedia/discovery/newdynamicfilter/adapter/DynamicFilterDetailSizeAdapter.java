package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

/**
 * Created by henrypriyono on 8/28/17.
 */

public class DynamicFilterDetailSizeAdapter extends DynamicFilterDetailAdapter {

    public DynamicFilterDetailSizeAdapter(DynamicFilterDetailView filterDetailView) {
        super(filterDetailView);
    }

    @Override
    protected DynamicFilterDetailViewHolder getViewHolder(View view) {
        return new SizeItemViewHolder(view, filterDetailView);
    }

    private static class SizeItemViewHolder extends DynamicFilterDetailViewHolder {

        public SizeItemViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
            super(itemView, filterDetailView);
        }

        @Override
        public void bind(final Option option) {
            super.bind(option);
            checkBox.setText(option.getName() + " " + option.getMetric());
        }
    }
}
