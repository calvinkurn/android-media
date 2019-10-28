package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;

/**
 * Created by hangnadi on 10/12/17.
 */

public class ListCatalogViewHolder extends GridCatalogViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.listview_browse_catalog;

    public ListCatalogViewHolder(View itemView, CatalogListener mCatalogListener) {
        super(itemView, mCatalogListener);
    }

    @Override
    public void bind(CatalogViewModel element) {
        super.bind(element);
    }
}
