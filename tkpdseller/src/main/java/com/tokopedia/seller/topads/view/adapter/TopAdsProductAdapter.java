package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsProductAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_PRODUCT = 100;

    private ArrayList<Product> data;

    public TopAdsProductAdapter() {
        this.data = new ArrayList<>();
    }

    public void clearData() {
        data = new ArrayList<>();
    }

    public void addProductList(List<Product> productList) {
        data.addAll(productList);
    }

    public int getDataSize() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_PRODUCT:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_top_ads_product, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_PRODUCT:
                bindProduct((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_PRODUCT;
        }
    }

    private void bindProduct(ViewHolder holder, final int position) {
        Product product = data.get(position);
        ImageHandler.loadImageCircle2(holder.iconImageView.getContext(), holder.iconImageView, product.getImageUrl());
        holder.titleTextView.setText(product.getName());
        holder.promotedTextView.setVisibility(product.isPromoted() ? View.VISIBLE : View.GONE);
        holder.groupNameTextView.setText(product.getGroupName());
        holder.groupNameTextView.setVisibility(!TextUtils.isEmpty(product.getGroupName()) ? View.VISIBLE : View.GONE);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_view_icon)
        ImageView iconImageView;

        @BindView(R2.id.text_view_title)
        TextView titleTextView;

        @BindView(R2.id.text_view_promoted)
        TextView promotedTextView;

        @BindView(R2.id.text_view_group_name)
        TextView groupNameTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}