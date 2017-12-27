package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.EmptyStateClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

/**
 * @author by errysuprayogi on 10/30/17.
 */

public class EmptyViewHolder extends AbstractViewHolder<EmptyModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_item_product_empty;

    private TextView txtEmptyContent;
    private Button emptyButtonItemButton;
    private final EmptyStateClickListener clickListener;

    public EmptyViewHolder(View itemView, EmptyStateClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        txtEmptyContent = (TextView) itemView.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = (Button) itemView.findViewById(R.id.button_add_promo);
        emptyButtonItemButton.setOnClickListener(this);
    }

    @Override
    public void bind(EmptyModel element) {
        txtEmptyContent.setText(element.getMessage());
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onEmptyButtonClicked();
        }
    }
}
