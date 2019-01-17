package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;

import java.util.List;

/**
 * @author by alifa on 11/1/17.
 */

public class RevampCategoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private int categoryWidth;
    private List<ChildCategoryModel> categories;
    private CategoryListener categoryListener;

    public RevampCategoryAdapter(int categoryWidth, List<ChildCategoryModel> categories, CategoryListener categoryListener) {
        this.categoryWidth = categoryWidth;
        this.categories = categories;
        this.categoryListener = categoryListener;
    }

    @Override
    public RevampCategoryAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(com.tokopedia.core2.R.layout.item_revamp_category, null
        );
        v.setMinimumWidth(categoryWidth);
        return new RevampCategoryAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        ItemRowHolder itemRowHolder = (RevampCategoryAdapter.ItemRowHolder) holder;
        itemRowHolder.container.getLayoutParams().width = categoryWidth;
        itemRowHolder.categoryTitle.setText(categories.get(position).getCategoryName().toUpperCase());
        ImageHandler.LoadImage(itemRowHolder.thumbnail,categories.get(position).getCategoryImageUrl());
        itemRowHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryRevampClick(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        private TextView categoryTitle;
        private LinearLayout container;
        private ImageView thumbnail;

        ItemRowHolder(View view) {
            super(view);
            initView(view);
        }

        private void initView(View view) {
            categoryTitle = view.findViewById(R.id.categoryTitle);
            container = view.findViewById(R.id.linWrapper);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }

    public interface CategoryListener {
        void onCategoryRevampClick(ChildCategoryModel child);

        void onBannerAdsClicked(String appLink);

        boolean isUserHasLogin();

        String getUserId();
    }

    public void addDataChild(List<ChildCategoryModel> children) {
        categories.addAll(children);
        notifyDataSetChanged();
    }

    public void hideExpandable() {
        categories.subList(9,categories.size()).clear();
        notifyDataSetChanged();
    }
}
