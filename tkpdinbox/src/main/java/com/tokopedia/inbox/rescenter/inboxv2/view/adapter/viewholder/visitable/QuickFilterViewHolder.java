package com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.multiple.view.QuickMultipleFilterView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterListViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 01/02/18.
 */

public class QuickFilterViewHolder extends AbstractViewHolder<FilterListViewModel> {
    ResoInboxFragmentListener.View mainView;
    private QuickMultipleFilterView filterView;

    public QuickFilterViewHolder(View itemView, ResoInboxFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        filterView = (QuickMultipleFilterView) itemView.findViewById(R.id.view_quick_filter);
    }

    @Override
    public void bind(FilterListViewModel element) {
        filterView.renderFilter(convertQuickFilterModel(element.getFilterList()));
        filterView.setListener(quickFilterListener);
    }

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_quick_filter;

    private List<QuickFilterItem> convertQuickFilterModel(List<FilterViewModel> filterList) {
        List<QuickFilterItem> itemList = new ArrayList<>();
        for (FilterViewModel filter : filterList) {
            QuickFilterItem item = new QuickFilterItem();
            item.setId(filter.getOrderValue());
            item.setName(filter.getTypeNameQuickFilter());
            item.setColorBorder(R.color.tkpd_main_green);
            item.setType(filter.getType());
            item.setSelected(mainView.getInboxFilterModel().getSelectedFilterList().contains(filter.getOrderValue()));
            itemList.add(item);
        }
        return itemList;
    }

    private QuickMultipleFilterView.ActionListener quickFilterListener
            = new QuickMultipleFilterView.ActionListener() {
        @Override
        public void filterClicked(List<Integer> selectedIdList) {
            mainView.getInboxFilterModel().setSelectedFilterList(selectedIdList);
            mainView.getInboxWithParams(mainView.getInboxFilterModel());
        }
    };

    public void renderFilter(List<FilterViewModel> filterList) {
        filterView.renderFilter(convertQuickFilterModel(filterList));
    }
}
