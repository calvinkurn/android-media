package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemToggleViewHolder extends DynamicFilterViewHolder {

    TextView title;
    Switch toggle;

    public DynamicFilterItemToggleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        toggle = (Switch) itemView.findViewById(R.id.toggle);
    }

    @Override
    public void bind(Filter filter) {
        Option itemData = filter.getOptions().get(0);
        title.setText(itemData.getName());
        toggle.setChecked(Boolean.parseBoolean(itemData.getValue()));
    }
}
