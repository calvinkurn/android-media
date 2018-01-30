package com.tokopedia.home.explore.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.explore.listener.CategoryListener;
import com.tokopedia.home.explore.view.adapter.viewholder.CategoryGridListViewHolder;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreAdapter extends BaseAdapterTypeFactory implements TypeFactory {

    private final CategoryListener listener;

    public ExploreAdapter(CategoryListener listener) {
        this.listener = listener;
    }

    @Override
    public int type(CategoryGridListViewModel viewModel) {
        return CategoryGridListViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == CategoryGridListViewHolder.LAYOUT) {
            return new CategoryGridListViewHolder(parent, listener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
