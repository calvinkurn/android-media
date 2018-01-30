package com.tokopedia.home.explore.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.explore.domain.model.CategoryItemModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class CategoryGridListViewHolder extends AbstractViewHolder<CategoryGridListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_grid;

    public CategoryGridListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(CategoryGridListViewModel element) {

    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<CategoryItemModel> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<CategoryItemModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            final CategoryItemModel rowModel = data.get(position);
            holder.title.setText(rowModel.getName());
            Glide.with(context).load(rowModel.getImageUrl()).into(holder.icon);
            holder.conteiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
//                    if (rowModel.getType().equalsIgnoreCase(MARKETPLACE)) {
//                        listener.onMarketPlaceItemClicked(rowModel, getAdapterPosition(), position);
//                    } else if (rowModel.getType().equalsIgnoreCase(DIGITAL)) {
//                        listener.onDigitalItemClicked(rowModel, getAdapterPosition(), position);
//                    } else if (!TextUtils.isEmpty(rowModel.getApplinks()) && deepLinkDelegate.supportsUri(rowModel.getApplinks())) {
//                        listener.onApplinkClicked(rowModel, getAdapterPosition(), position);
//                    } else {
//                        listener.onGimickItemClicked(rowModel, getAdapterPosition(), position);
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.icon)
            ImageView icon;
            @BindView(R.id.title)
            TextView title;
            @BindView(R.id.container)
            LinearLayout conteiner;

            public ItemViewHolder(View itemView) {
                super(itemView);
                ico
            }
        }
    }
}
