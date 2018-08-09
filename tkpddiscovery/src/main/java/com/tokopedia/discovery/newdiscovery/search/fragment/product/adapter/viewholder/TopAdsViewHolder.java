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

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.TopAdsSwitcher;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsWidgetView;

public class TopAdsViewHolder extends AbstractViewHolder<TopAdsViewModel> implements TopAdsItemClickListener, TopAdsSwitcher {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_item_ads;

    private TopAdsWidgetView adsWidgetView;
    private Context context;

    public TopAdsViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        adsWidgetView = itemView.findViewById(R.id.topads_view);
        adsWidgetView.setItemClickListener(this);
        adsWidgetView.setMode(DisplayMode.GRID);
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
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        });
    }

    @Override
    public void bind(TopAdsViewModel element) {
        adsWidgetView.setAdapterPosition(getAdapterPosition());
        adsWidgetView.setData(element.getDataList());
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        if(context instanceof Activity) {
            Activity activity = (Activity) context;
            ProductItem data = new ProductItem();
            data.setId(product.getId());
            data.setName(product.getName());
            data.setPrice(product.getPriceFormat());
            data.setImgUri(product.getImage().getM_url());
            Bundle bundle = new Bundle();
            Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(activity);
            bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
            intent.putExtras(bundle);
            activity.startActivity(intent);
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
        adsWidgetView.setMode(mode);
    }

    @Override
    public void onAddWishList(int position, Data data) { }

}
