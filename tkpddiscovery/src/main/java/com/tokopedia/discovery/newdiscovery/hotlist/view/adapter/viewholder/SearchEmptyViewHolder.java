package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;

/**
 * Created by hangnadi on 10/25/17.
 */

public class SearchEmptyViewHolder extends AbstractViewHolder<SearchEmptyViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.design_error_empty_search;

    private final Context context;
    private final ItemClickListener mItemClickListener;

    public SearchEmptyViewHolder(View itemView, ItemClickListener mItemClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void bind(SearchEmptyViewModel element) {

    }

}
