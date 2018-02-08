package com.tokopedia.seller.base.view.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.model.FilterTitleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsFilterAdapter extends BaseLinearRecyclerViewAdapter {

    public interface Callback {

        void onItemSelected(int position);

    }

    private static final int VIEW_DATA = 100;

    private List<FilterTitleItem> data;
    private int selectedPosition;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setData(List<FilterTitleItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public TopAdsFilterAdapter() {
        data = new ArrayList<>();
        selectedPosition = 0;
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DATA:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_base_filter, viewGroup, false));
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
        holder.statusImageView.setVisibility(filterTitleItem.isActive() ? View.VISIBLE : View.INVISIBLE);
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelected(holder.getAdapterPosition());
            }
        });
    }

    private void onItemSelected(int position) {
        selectItem(position);
        if (callback != null) {
            callback.onItemSelected(position);
        }
    }

    public void selectItem(int position) {
        int prevPos = selectedPosition;
        selectedPosition = position;
        // notifyDataSetChanged();
        if (prevPos > -1) {
            notifyItemChanged(prevPos);
        }
        notifyItemChanged(selectedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public ImageView statusImageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_view_title);
            statusImageView = (ImageView) view.findViewById(R.id.image_view_status);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemSelected(getAdapterPosition());
        }
    }
}