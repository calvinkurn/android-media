package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedFeaturedAdapter;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedFeaturedViewHolder extends AbstractViewHolder<ShopProductLimitedFeaturedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_featured;

    private RecyclerView recyclerView;
    private ShopProductClickedListener shopProductClickedListener;
    private ShopProductFeaturedViewHolder.ShopProductFeaturedListener shopProductFeaturedListener;

    public ShopProductLimitedFeaturedViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener,
                                                ShopProductFeaturedViewHolder.ShopProductFeaturedListener shopProductFeaturedListener) {
        super(itemView);
        this.shopProductClickedListener = shopProductClickedListener;
        this.shopProductFeaturedListener = shopProductFeaturedListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void bind(ShopProductLimitedFeaturedViewModel shopProductLimitedFeaturedViewModel) {
        ShopProductLimitedFeaturedAdapter adapter = new ShopProductLimitedFeaturedAdapter(shopProductClickedListener, shopProductFeaturedListener);
        adapter.setList(shopProductLimitedFeaturedViewModel.getShopProductViewModelList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext()));
        adapter.notifyDataSetChanged();
    }
}