package com.tokopedia.home.explore.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryFavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 1/31/18.
 */

public class CategoryFavoriteViewHolder extends AbstractViewHolder<CategoryFavoriteViewModel> {

    private static final String MARKETPLACE = "Marketplace";
    private static final String DIGITAL = "Digital";

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_favorite;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private Context context;

    private CategoryAdapterListener listener;
    private ItemAdapter adapter;
    private int spanCount = 2;
    private List<LayoutRows> rowModelList = new ArrayList<>();


    public CategoryFavoriteViewHolder(View itemView, CategoryAdapterListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        titleTxt = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.list);
        adapter = new ItemAdapter(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(CategoryFavoriteViewModel element) {
        titleTxt.setText(element.getTitle());
        rowModelList.addAll(element.getItemList());
        adapter.setData(rowModelList);
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<LayoutRows> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<LayoutRows> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemAdapter.ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_favorite_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, final int position) {
            final LayoutRows rowModel = data.get(position);
            holder.title.setText(rowModel.getName());
            ImageHandler.loadImageAndCache(holder.icon, rowModel.getImageUrl());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rowModel.getType().equalsIgnoreCase(MARKETPLACE)) {
                        listener.onMarketPlaceItemClicked(rowModel);
                    } else if (rowModel.getType().equalsIgnoreCase(DIGITAL)) {
                        listener.onDigitalItemClicked(rowModel);
                    } else if (!TextUtils.isEmpty(rowModel.getApplinks())) {
                        listener.onApplinkClicked(rowModel);
                    } else {
                        listener.onGimickItemClicked(rowModel);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView title;
            CardView container;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                container = itemView.findViewById(R.id.container);
            }
        }
    }
}
