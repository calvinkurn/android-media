package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategoryItemViewHolder extends AbstractViewHolder<CategoryItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private TextView seeMoreBtn;

    private Context context;
    private ItemAdapter adapter;
    private int spanCount = 2;
    private int limitItem = 6;
    private HomeCategoryListener listener;
    private List<CategoryLayoutRowModel> rowModelList = new ArrayList<>();

    public CategoryItemViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        titleTxt = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.list);
        seeMoreBtn = itemView.findViewById(R.id.see_more);
        adapter = new ItemAdapter(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(final CategoryItemViewModel element) {
        titleTxt.setText(element.getTitle());
        rowModelList.clear();
        if (element.getItemList().size() > limitItem) {
            for (int i = 0; i < limitItem; i++) {
                rowModelList.add(element.getItemList().get(i));
            }
            seeMoreBtn.setVisibility(View.VISIBLE);
            int count = element.getItemList().size() - rowModelList.size();
            seeMoreBtn.setText(String.format(context.getString(R.string.format_btn_category_more), count));
        } else {
            seeMoreBtn.setVisibility(View.GONE);
            rowModelList.addAll(element.getItemList());
        }
        adapter.setData(rowModelList);
        seeMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowModelList = new ArrayList<>(element.getItemList());
                adapter.setData(rowModelList);
                seeMoreBtn.setVisibility(View.GONE);
            }
        });
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
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_grid_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            final CategoryLayoutRowModel rowModel = data.get(position);
            holder.title.setText(rowModel.getName());
            Glide.with(context).load(rowModel.getImageUrl()).into(holder.icon);
            holder.conteiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(rowModel.getApplinks())) {
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

            private ImageView icon;
            private TextView title;
            private LinearLayout conteiner;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                title = itemView.findViewById(R.id.title);
                conteiner = itemView.findViewById(R.id.container);
            }
        }
    }
}
