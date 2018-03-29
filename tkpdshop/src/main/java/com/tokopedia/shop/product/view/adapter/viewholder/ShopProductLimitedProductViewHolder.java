package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedProductAdapter;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedProductViewHolder extends AbstractViewHolder<ShopProductLimitedProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_product;
    private static final int SPAN_COUNT = 2;
    private RecyclerView recyclerView;

    private ShopProductClickedListener shopProductClickedListener;


    public ShopProductLimitedProductViewHolder(View itemView,
                                               ShopProductClickedListener shopProductClickedListener) {
        super(itemView);
        this.shopProductClickedListener = shopProductClickedListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void bind(ShopProductLimitedProductViewModel shopProductLimitedProductViewModel) {
        ShopProductLimitedProductAdapter adapter = new ShopProductLimitedProductAdapter(shopProductClickedListener);
        adapter.setList(shopProductLimitedProductViewModel.getShopProductViewModelList().getList());
        LinearLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();
    }
}