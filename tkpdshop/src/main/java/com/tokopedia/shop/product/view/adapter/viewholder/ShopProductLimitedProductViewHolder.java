package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedFeaturedAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedProductAdapter;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedProductViewHolder extends AbstractViewHolder<ShopProductLimitedProductViewModel> {

    private static final int SPAN_COUNT = 2;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_product;

    private RecyclerView recyclerView;

    public ShopProductLimitedProductViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void bind(ShopProductLimitedProductViewModel shopProductLimitedProductViewModel) {
        ShopProductLimitedProductAdapter adapter = new ShopProductLimitedProductAdapter();
        adapter.setList(shopProductLimitedProductViewModel.getShopProductViewModelList());
        LinearLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}