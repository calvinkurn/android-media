package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemToggleViewHolder extends DynamicFilterViewHolder {

    TextView title;
    Switch toggle;
    private final DynamicFilterView dynamicFilterView;

    public DynamicFilterItemToggleViewHolder(View itemView, DynamicFilterView dynamicFilterView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        toggle = (Switch) itemView.findViewById(R.id.toggle);
        this.dynamicFilterView = dynamicFilterView;
    }

    @Override
    public void bind(Filter filter) {
        final Option option = filter.getOptions().get(0);
        title.setText(option.getName());

        if (Boolean.TRUE.equals(dynamicFilterView.getLastCheckedState(option))) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dynamicFilterView.saveCheckedState(option, isChecked);
            }
        });
    }
}
