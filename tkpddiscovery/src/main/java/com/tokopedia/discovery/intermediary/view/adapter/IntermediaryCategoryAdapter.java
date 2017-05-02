package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.discovery.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 3/29/17.
 */

public class IntermediaryCategoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private int categoryWidth;
    private List<ChildCategoryModel> categories;
    private IntermediaryCategoryAdapter.CategoryListener categoryListener;

    public IntermediaryCategoryAdapter(int categoryWidth, List<ChildCategoryModel> categories,
                                       IntermediaryCategoryAdapter.CategoryListener categoryListener) {
        this.categoryWidth = categoryWidth;
        this.categories = categories;
        this.categoryListener = categoryListener;
    }

    @Override
    public IntermediaryCategoryAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(com.tokopedia.core.R.layout.item_revamp_category, null
        );
        v.setMinimumWidth(categoryWidth);
        return new IntermediaryCategoryAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        IntermediaryCategoryAdapter.ItemRowHolder itemRowHolder = (IntermediaryCategoryAdapter.ItemRowHolder) holder;
        itemRowHolder.container.getLayoutParams().width = categoryWidth;
        itemRowHolder.categoryTitle.setText(categories.get(position).getCategoryName().toUpperCase());
        ImageHandler.LoadImage(itemRowHolder.thumbnail,categories.get(position).getCategoryImageUrl());
        itemRowHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryRevampClick(categories.get(position) );
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.categoryTitle)
        TextView categoryTitle;

        @BindView(R2.id.linWrapper)
        LinearLayout container;

        @BindView(R2.id.thumbnail)
        ImageView thumbnail;

        ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface CategoryListener {
        void onCategoryRevampClick(ChildCategoryModel child);
    }

    public void addDataChild(List<ChildCategoryModel> childs) {
        categories.addAll(childs);
        notifyDataSetChanged();
    }

    public void hideExpandable() {
        categories.subList(9,categories.size()).clear();
        notifyDataSetChanged();
    }
}

