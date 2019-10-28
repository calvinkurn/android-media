package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;

/**
 * Created by hangnadi on 10/12/17.
 */

public class BigGridCatalogViewHolder extends GridCatalogViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.gridview_big_browse_catalog;

    public BigGridCatalogViewHolder(View itemView, CatalogListener mCatalogListener) {
        super(itemView, mCatalogListener);
    }

    @Override
    public void setCatalogImage(CatalogViewModel catalog) {
        ImageHandler.loadImageThumbs(context, catalogImage, catalog.getImage300());
    }
}
