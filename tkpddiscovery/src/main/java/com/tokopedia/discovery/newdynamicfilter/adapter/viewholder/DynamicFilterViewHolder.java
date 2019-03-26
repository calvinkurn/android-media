package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public abstract class DynamicFilterViewHolder extends AbstractViewHolder<Filter> {

    final DynamicFilterView dynamicFilterView;

    public DynamicFilterViewHolder(View itemView,
                                   final DynamicFilterView dynamicFilterView) {
        super(itemView);

        this.dynamicFilterView = dynamicFilterView;
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
}
