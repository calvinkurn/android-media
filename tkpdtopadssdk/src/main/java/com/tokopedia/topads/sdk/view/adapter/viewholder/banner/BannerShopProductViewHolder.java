package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewHolder extends AbstractViewHolder<BannerShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_banner_shop_product;
    private static final String TAG = BannerShopProductViewHolder.class.getSimpleName();
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;


    public BannerShopProductViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.list);
        itemAdapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void bind(BannerShopProductViewModel element) {
        itemAdapter.setProductList(element.getProductList());
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private List<Product> productList = new ArrayList<>();

        public void setProductList(List<Product> productList) {
            this.productList = productList;
            this.productList.remove(0); //remove index 0 because already show on first page
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_items, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            Product product = productList.get(position);
            Glide.with(holder.getContext()).load(product.getImageProduct().getImageUrl()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView visitShop;
        public ImageView imageView;
        private View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            visitShop = itemView.findViewById(R.id.visit_btn);
            imageView = itemView.findViewById(R.id.image);
        }

        public Context getContext() {
            return view.getContext();
        }
    }
}
