package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class DynamicFilterDetailColorAdapter extends DynamicFilterDetailAdapter {

    public DynamicFilterDetailColorAdapter(DynamicFilterDetailView filterDetailView) {
        super(filterDetailView);
    }

    @Override
    protected int getLayout() {
        return R.layout.filter_detail_color;
    }

    @Override
    protected AbstractViewHolder<Option> getViewHolder(View view) {
        return new ColorItemViewHolder(view, filterDetailView);
    }

    private static class ColorItemViewHolder extends AbstractViewHolder<Option> {

        private ColorSampleView colorIcon;
        private TextView colorTitle;
        private CheckBox colorCheckBox;
        private DynamicFilterDetailView filterDetailView;

        public ColorItemViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
            super(itemView);
            this.filterDetailView = filterDetailView;
            colorIcon = (ColorSampleView) itemView.findViewById(R.id.color_icon);
            colorTitle = (TextView) itemView.findViewById(R.id.color_title);
            colorCheckBox = (CheckBox) itemView.findViewById(R.id.color_checkbox);
        }

        @Override
        public void bind(Option option) {
            colorIcon.setColor(Color.parseColor(option.getHexColor()));
            colorTitle.setText(option.getName());
            OptionHelper.bindOptionWithCheckbox(option, colorCheckBox, filterDetailView);
        }
    }
}
