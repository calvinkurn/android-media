package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;

/**
 * Created by hangnadi on 10/12/17.
 */

public class BigGridCatalogViewHolder extends GridCatalogViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.gridview_big_browse_catalog;

    public BigGridCatalogViewHolder(View itemView, ItemClickListener mItemClickListener) {
        super(itemView, mItemClickListener);
    }

    @Override
    public void setCatalogImage(CatalogViewModel catalog) {
        ImageHandler.loadImageThumbs(context, catalogImage, catalog.getImage300());
    }
}
