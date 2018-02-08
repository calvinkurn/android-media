package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alifa on 2/28/2017.
 */

public class DefaultCategoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private int categoryWidth;
    private List<ChildCategoryModel> categories;
    private DefaultCategoryAdapter.CategoryListener categoryListener;

    public DefaultCategoryAdapter(int categoryWidth, List<ChildCategoryModel> categories, CategoryListener categoryListener) {
        this.categoryWidth = categoryWidth;
        this.categories = categories;
        this.categoryListener = categoryListener;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_default_category, null
        );
        v.setMinimumWidth(categoryWidth);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        ItemRowHolder itemRowHolder = (ItemRowHolder) holder;
        itemRowHolder.container.getLayoutParams().width = categoryWidth;
        itemRowHolder.categoryTitle.setText(categories.get(position).getCategoryName());
        itemRowHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryClick(categories.get(position));
            }
        });
        if(position % 2 != 0 ){
            itemRowHolder.separator.setVisibility(View.GONE);
        } else {
            itemRowHolder.separator.setVisibility(View.VISIBLE);
        }
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

        @BindView(R2.id.separator)
        View separator;

        ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface CategoryListener {
        void onCategoryClick(ChildCategoryModel child);

        void onBannerAdsClicked(String appLink);
    }

    public void addDataChild(List<ChildCategoryModel> children) {
        categories.addAll(children);
        notifyDataSetChanged();
    }

    public void hideExpandable() {
        categories.subList(6,categories.size()).clear();
        notifyDataSetChanged();
    }
}
