package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksViewHolder extends AbstractViewHolder<TopPicksViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_toppicks;
    private TextView titleTxt;
    private RecyclerView recyclerView;
    private TextView seeMoreTxt;

    private ItemAdapter adapter;
    private int spanCount = 3;
    private HomeCategoryListener listener;

    public TopPicksViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        adapter = new ItemAdapter(itemView.getContext());
        titleTxt = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.list);
        seeMoreTxt = itemView.findViewById(R.id.see_more);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                itemView.getResources().getDimensionPixelSize(R.dimen.home_card_page_margin), true));
    }

    @Override
    public void bind(final TopPicksViewModel element) {
        titleTxt.setText(element.getTitle());
        adapter.setData(element.getTopPicksItems());
        seeMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTopPicksMoreClicked(element.getTopPickUrl(), getAdapterPosition());
            }
        });
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<TopPicksItemModel> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<TopPicksItemModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_toppicks_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            Glide.with(context).load(data.get(position).getImageUrl()).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTopPicksItemClicked(data.get(position), getAdapterPosition(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView image;

            public ItemViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
            }
        }
    }


}
