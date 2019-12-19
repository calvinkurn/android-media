package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;

/**
 * Created by hangnadi on 10/25/17.
 */

public class SearchEmptyViewHolder extends AbstractViewHolder<SearchEmptyViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.design_error_empty_search;

    private final Context context;
    private final HotlistListener mHotlistListener;

    public SearchEmptyViewHolder(View itemView, HotlistListener mHotlistListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.mHotlistListener = mHotlistListener;
    }

    @Override
    public void bind(SearchEmptyViewModel element) {

    }

}
