package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemToggleViewHolder extends DynamicFilterViewHolder {

    private TextView title;
    private SwitchCompat toggle;

    public DynamicFilterItemToggleViewHolder(View itemView, final DynamicFilterView filterView, final FilterController filterController) {
        super(itemView, filterView, filterController);

        title = itemView.findViewById(R.id.title);
        toggle = itemView.findViewById(R.id.toggle);
    }

    @Override
    public void bind(Filter filter) {
        final Option option = filter.getOptions().get(0);
        title.setText(option.getName());

        itemView.setOnClickListener(v -> toggle.setChecked(!toggle.isChecked()));

        bindSwitchForOption(option);
    }

    private void bindSwitchForOption(Option option) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                (buttonView, isChecked) -> setAndApplyFilter(option, String.valueOf(isChecked));

        String filterValueString = filterController.getFilterValue(option.getKey());
        boolean filterValueBoolean = Boolean.parseBoolean(filterValueString);

        bindSwitch(toggle, filterValueBoolean, onCheckedChangeListener);
    }
}
