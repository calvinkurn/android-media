package com.tokopedia.shop.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */

public class ShopProductLimitedFeaturedAdapter extends RecyclerView.Adapter<ShopProductListViewHolder> {

    private final ShopProductClickedListener shopProductClickedListener;
    private List<ShopProductViewModel> list;

    public ShopProductLimitedFeaturedAdapter(ShopProductClickedListener shopProductClickedListener) {
        this.shopProductClickedListener = shopProductClickedListener;
        list = new ArrayList<>();
    }

    @Override
    public ShopProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ShopProductListViewHolder.LAYOUT, parent, false);
        return new ShopProductListViewHolder(view, shopProductClickedListener);
    }

    @Override
    public void onBindViewHolder(ShopProductListViewHolder holder, int position) {
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ShopProductViewModel> list) {
        this.list = list;
    }
}
