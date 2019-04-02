package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

/**
 * Created by henrypriyono on 8/29/17.
 */

public abstract class DynamicFilterDetailViewHolder extends AbstractViewHolder<Option> {

    protected CheckBox checkBox;
    private DynamicFilterDetailView filterDetailView;

    public DynamicFilterDetailViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
        super(itemView);
        this.filterDetailView = filterDetailView;
        checkBox = (CheckBox) itemView.findViewById(R.id.filter_detail_item_checkbox);
    }

    @Override
    public void bind(Option option) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        OptionHelper.bindOptionWithCheckbox(option, checkBox, filterDetailView);
    }
}
