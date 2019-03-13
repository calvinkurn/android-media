package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public abstract class DynamicFilterViewHolder extends AbstractViewHolder<Filter> {

    final DynamicFilterView filterView;
    final FilterController filterController;

    public DynamicFilterViewHolder(View itemView,
                                   final DynamicFilterView filterView,
                                   final FilterController filterController) {
        super(itemView);

        this.filterView = filterView;
        this.filterController = filterController;
    }

    void bindSwitch(SwitchCompat switchView,
                           Boolean isChecked,
                           CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {

        switchView.setOnCheckedChangeListener(null);

        if (Boolean.TRUE.equals(isChecked)) {
            switchView.setChecked(true);
        } else {
            switchView.setChecked(false);
        }

        switchView.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    void setAndApplyFilter(Option option, String value) {
        filterController.setFilterValue(option, value);
        filterView.trackSearch(option.getName(), value, !isFilterApplied(value));
        filterView.applyFilter();
    }

    private boolean isFilterApplied(String value) {
        if(Boolean.parseBoolean(value)) return true;
        else return isValueNotEmptyAndNotFalse(value);
    }

    private boolean isValueNotEmptyAndNotFalse(String value) {
        return !TextUtils.isEmpty(value) && value.equals(Boolean.FALSE.toString());
    }
}
