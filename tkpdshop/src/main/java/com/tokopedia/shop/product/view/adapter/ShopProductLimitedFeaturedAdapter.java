package com.tokopedia.shop.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */

public class ShopProductLimitedFeaturedAdapter extends RecyclerView.Adapter<ShopProductFeaturedViewHolder> {

    private final ShopProductClickedListener shopProductClickedListener;
    private ShopProductFeaturedViewHolder.ShopProductFeaturedListener shopProductFeaturedListener;
    private List<ShopProductViewModel> list;

    public List<ShopProductViewModel> getList() {
        return list;
    }

    public void setList(List<ShopProductViewModel> list) {
        this.list = list;
    }

    public ShopProductLimitedFeaturedAdapter(ShopProductClickedListener shopProductClickedListener,
                                             ShopProductFeaturedViewHolder.ShopProductFeaturedListener shopProductFeaturedListener) {
        this.shopProductClickedListener = shopProductClickedListener;
        this.shopProductFeaturedListener = shopProductFeaturedListener;
        list = new ArrayList<>();
    }

    @Override
    public ShopProductFeaturedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ShopProductFeaturedViewHolder.LAYOUT, parent, false);
        return new ShopProductFeaturedViewHolder(view, shopProductClickedListener, shopProductFeaturedListener);
    }

    @Override
    public void onBindViewHolder(ShopProductFeaturedViewHolder holder, int position) {
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
