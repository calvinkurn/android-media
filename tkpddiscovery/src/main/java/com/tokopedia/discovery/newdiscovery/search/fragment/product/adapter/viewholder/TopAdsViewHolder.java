package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.TopAdsSwitcher;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsWidgetView;

public class TopAdsViewHolder extends AbstractViewHolder<TopAdsViewModel> implements TopAdsItemClickListener, TopAdsSwitcher {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_item_ads;

    private TopAdsWidgetView adsWidgetView;
    private Context context;
    private ProductListener itemClickListener;
    private String keyword = "";

    public TopAdsViewHolder(View itemView, ProductListener itemClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.itemClickListener = itemClickListener;
        adsWidgetView = itemView.findViewById(R.id.topads_view);
        adsWidgetView.setItemClickListener(this);
        adsWidgetView.setImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.getInstance().addSearchResultProductViewImpressions(product, position);
            }
        });
        adsWidgetView.setDisplayMode(DisplayMode.GRID);
        adsWidgetView.setItemDecoration(new RecyclerView.ItemDecoration() {

            private int getTotalSpanCount(RecyclerView parent) {
                final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
                return layoutManager instanceof GridLayoutManager
                        ? ((GridLayoutManager) layoutManager).getSpanCount()
                        : 1;
            }

            int spacing = context.getResources().getDimensionPixelSize(R.dimen.dp_16);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int spanCount = getTotalSpanCount(parent);
                int position = parent.getChildAdapterPosition(view);
                int column = position % spanCount;
                outRect.left = column * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing / spanCount;
                    outRect.bottom = spacing / spanCount;
                }
                if (parent.getLayoutManager() instanceof GridLayoutManager) {
                    outRect.right = spacing - (column + 1) * spacing / spanCount;
                } else {
                    outRect.right = 0;
                }
            }
        });
    }

    @Override
    public void bind(TopAdsViewModel element) {
        adsWidgetView.setAdapterPosition(getAdapterPosition());
//        adsWidgetView.setData(element.getTopadsData());
        this.keyword = element.getQuery();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        if(context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = getProductIntent(product.getId());
            activity.startActivity(intent);
            TopAdsGtmTracker.eventSearchResultProductClick(context, keyword, product, position);
        }
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context,ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) {
        data.setFavorit(true);
        adsWidgetView.notifyDataChange();
    }

    @Override
    public void switchDisplay(DisplayMode mode) {
        adsWidgetView.setDisplayMode(mode);
    }

}
