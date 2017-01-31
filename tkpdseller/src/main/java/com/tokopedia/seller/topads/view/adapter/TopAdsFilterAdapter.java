package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.other.FilterTitleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsFilterAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_DATA = 100;

    private List<FilterTitleItem> data;
    private int checkedPosition;

    public void setData(List<FilterTitleItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public TopAdsFilterAdapter() {
        data = new ArrayList<>();
        checkedPosition = 0;
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DATA:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top_ads_filter, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_DATA:
                bindProduct((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DATA;
        }
    }

    private void bindProduct(final ViewHolder holder, int position) {
        FilterTitleItem filterTitleItem = data.get(position);
        holder.titleTextView.setText(filterTitleItem.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
    }

    private void onSelectItem(int position) {
        checkedPosition = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        }
    }
}