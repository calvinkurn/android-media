package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;

/**
 * Created by hangnadi on 10/12/17.
 */

public class GridCatalogViewHolder extends AbstractViewHolder<CatalogViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.gridview_browse_catalog;
    protected final View container;

    protected SquareImageView catalogImage;
    protected TextView catalogTitle;
    protected TextView catalogPrice;
    protected TextView catalogSeller;
    protected LinearLayout badgesContainer;

    protected final Context context;
    protected final CatalogListener listener;

    public GridCatalogViewHolder(View itemView, CatalogListener mCatalogListener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = mCatalogListener;
        this.catalogImage = (SquareImageView) itemView.findViewById(R.id.product_image);
        this.catalogTitle = (TextView) itemView.findViewById(R.id.title);
        this.catalogPrice = (TextView) itemView.findViewById(R.id.price);
        this.catalogSeller = (TextView) itemView.findViewById(R.id.seller);
        this.badgesContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        this.container = itemView;
    }

    @Override
    public void bind(final CatalogViewModel element) {
        setCatalogImage(element);
        catalogSeller.setText(element.getProductCounter() + " " + context.getString(R.string.title_total_prods));
        catalogTitle.setText(MethodChecker.fromHtml(element.getName()));
        catalogPrice.setText(element.getPrice());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setOnCatalogClicked(element.getID(), element.getName());
            }
        });
    }

    public void setCatalogImage(CatalogViewModel catalog) {
        ImageHandler.loadImageThumbs(context, catalogImage, catalog.getImage());
    }
}
