package com.tokopedia.home.explore.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class CategoryGridListViewHolder extends AbstractViewHolder<CategoryGridListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_grid;
    private static final String MARKETPLACE = "Marketplace";
    private static final String DIGITAL = "Digital";
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private Context context;
    private ItemAdapter adapter;
    private int spanCount = 2;
    private int limitItem = 6;
    private List<CategoryLayoutRowModel> rowModelList = new ArrayList<>();

    public CategoryGridListViewHolder(View itemView) {
        super(itemView);
        titleTxt = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.list);
        this.context = itemView.getContext();
        adapter = new ItemAdapter(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(CategoryGridListViewModel element) {
        titleTxt.setText(element.getTitle());
        rowModelList.addAll(element.getItemList());
        adapter.setData(rowModelList);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<CategoryLayoutRowModel> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<CategoryLayoutRowModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            final CategoryLayoutRowModel rowModel = data.get(position);
            holder.title.setText(rowModel.getName());
            Glide.with(context).load(rowModel.getImageUrl()).into(holder.icon);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ((AbstractionRouter) getActivity().getApplication()).actionApplinkFromActivity(getActivity()
//                            , url);
//                    DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
                    if (rowModel.getType().equalsIgnoreCase(MARKETPLACE)) {
                        listener.onMarketPlaceItemClicked(rowModel, getAdapterPosition(), position);
                    } else if (rowModel.getType().equalsIgnoreCase(DIGITAL)) {
                        listener.onDigitalItemClicked(rowModel, getAdapterPosition(), position);
                    } else if (!TextUtils.isEmpty(rowModel.getApplinks()) && deepLinkDelegate.supportsUri(rowModel.getApplinks())) {
                        listener.onApplinkClicked(rowModel, getAdapterPosition(), position);
                    } else {
                        listener.onGimickItemClicked(rowModel, getAdapterPosition(), position);
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
            LinearLayout container;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                title = itemView.findViewById(R.id.title);
                container = itemView.findViewById(R.id.container);
            }
        }
    }
}
