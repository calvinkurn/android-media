package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.helper.StartSnapHelper;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SprintSaleCarouselViewModel;

/**
 * Created by errysuprayogi on 3/22/18.
 */

public class SprintSaleCarouselViewHolder extends AbstractViewHolder<SprintSaleCarouselViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_sprint_card_item;
    private LinearLayout container;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private Context context;

    public SprintSaleCarouselViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        itemAdapter = new ItemAdapter();
        container = itemView.findViewById(R.id.container);
        recyclerView = itemView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacingItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.item_space), SpacingItemDecoration.HORIZONTAL));
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void bind(SprintSaleCarouselViewModel element) {

    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        public ItemAdapter() {
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sprint_product_item, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {


        public ItemViewHolder(View itemView) {
            super(itemView);
        }

    }
}
