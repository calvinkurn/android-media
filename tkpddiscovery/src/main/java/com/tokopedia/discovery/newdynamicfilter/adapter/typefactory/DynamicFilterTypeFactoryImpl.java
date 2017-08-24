package com.tokopedia.discovery.newdynamicfilter.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterExpandableItemViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterItemPriceViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterItemToggleViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterNoViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import static com.tokopedia.core.discovery.model.Filter.TEMPLATE_NAME_PRICE;
import static com.tokopedia.core.discovery.model.Filter.TITLE_SEPARATOR;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterTypeFactoryImpl implements DynamicFilterTypeFactory {

    private DynamicFilterView filterView;

    public DynamicFilterTypeFactoryImpl(DynamicFilterView filterView) {
        this.filterView = filterView;
    }

    @Override
    public int type(Filter filter) {
        if (TITLE_SEPARATOR.equals(filter.getTitle())) {
            return R.layout.dynamic_filter_item_separator;
        }
        else if (TEMPLATE_NAME_PRICE.equals(filter.getTemplateName())) {
            return R.layout.dynamic_filter_item_price;
        } else if (filter.getOptions().size() == 1) {
            return R.layout.dynamic_filter_item_toggle;
        } else {
            return R.layout.dynamic_filter_expandable_item;
        }
    }

    @Override
    public DynamicFilterViewHolder createViewHolder(View view, int viewType) {
        if (viewType == R.layout.dynamic_filter_item_price) {
            return new DynamicFilterItemPriceViewHolder(view, filterView);
        } else if (viewType == R.layout.dynamic_filter_item_toggle) {
            return new DynamicFilterItemToggleViewHolder(view, filterView);
        } else if (viewType == R.layout.dynamic_filter_expandable_item) {
            return new DynamicFilterExpandableItemViewHolder(view, filterView);
        } else {
            return new DynamicFilterNoViewHolder(view);
        }
    }
}
