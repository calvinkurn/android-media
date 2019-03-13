package com.tokopedia.discovery.newdynamicfilter.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterExpandableItemViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterItemPriceViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterItemToggleViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterNoViewHolder;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterTypeFactoryImpl implements DynamicFilterTypeFactory {

    private DynamicFilterView filterView;
    private final FilterController filterController;

    public DynamicFilterTypeFactoryImpl(DynamicFilterView filterView, final FilterController filterController) {
        this.filterView = filterView;
        this.filterController = filterController;
    }

    @Override
    public int type(Filter filter) {
        if (filter.isSeparator()) {
            return R.layout.dynamic_filter_item_separator;
        }
        else if (filter.isPriceFilter()) {
            return R.layout.dynamic_filter_item_price;
        } else if (filter.isExpandableFilter()) {
            return R.layout.dynamic_filter_expandable_item;
        } else {
            return R.layout.dynamic_filter_item_toggle;
        }
    }

    @Override
    public DynamicFilterViewHolder createViewHolder(View view, int viewType) {
        if (viewType == R.layout.dynamic_filter_item_price) {
            return new DynamicFilterItemPriceViewHolder(view, filterView, filterController);
        } else if (viewType == R.layout.dynamic_filter_item_toggle) {
            return new DynamicFilterItemToggleViewHolder(view, filterView, filterController);
        } else if (viewType == R.layout.dynamic_filter_expandable_item) {
            return new DynamicFilterExpandableItemViewHolder(view, filterView, filterController);
        } else {
            return new DynamicFilterNoViewHolder(view);
        }
    }
}
