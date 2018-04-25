package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
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
    public TextView visitShop;
    private final TopAdsBannerClickListener topAdsBannerClickListener;


    public BannerShopProductViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        recyclerView = itemView.findViewById(R.id.list);
        visitShop = itemView.findViewById(R.id.visit_btn);
        itemAdapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void bind(final BannerShopProductViewModel element) {
        itemAdapter.setProductList(element.getProductList());
        visitShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topAdsBannerClickListener!=null) {
                    topAdsBannerClickListener.onBannerAdsClicked(element.getAppLink());
                    new ImpresionTask().execute(element.getAdsClickUrl());
                }
            }
        });
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private List<Product> productList = new ArrayList<>();

        public void setProductList(List<Product> productList) {
            this.productList = productList;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_items, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            final Product product = productList.get(position);
            Glide.with(holder.getContext()).load(product.getImageProduct().getImageUrl()).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (topAdsBannerClickListener != null) {
                        topAdsBannerClickListener.onBannerAdsClicked(product.getApplinks());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        private View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.image);
        }

        public Context getContext() {
            return view.getContext();
        }
    }
}
