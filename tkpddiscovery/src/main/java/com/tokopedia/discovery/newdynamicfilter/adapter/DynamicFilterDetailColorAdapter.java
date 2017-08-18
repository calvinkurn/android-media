package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class DynamicFilterDetailColorAdapter extends DynamicFilterDetailAdapter {
    @Override
    protected int getLayout() {
        return R.layout.filter_detail_color;
    }

    @Override
    protected AbstractViewHolder<Option> getViewHolder(View view) {
        return new ColorItemViewHolder(view);
    }

    private static class ColorItemViewHolder extends AbstractViewHolder<Option> {

        private View colorIcon;
        private TextView colorTitle;
        private CheckBox colorCheckBox;

        public ColorItemViewHolder(View itemView) {
            super(itemView);
            colorIcon = itemView.findViewById(R.id.color_icon);
            colorTitle = (TextView) itemView.findViewById(R.id.color_title);
            colorCheckBox = (CheckBox) itemView.findViewById(R.id.color_checkbox);
        }

        @Override
        public void bind(Option option) {
            colorIcon.getBackground().setColorFilter(Color.parseColor(option.getHexColor()), PorterDuff.Mode.ADD);
            colorTitle.setText(option.getName());
        }
    }
}
