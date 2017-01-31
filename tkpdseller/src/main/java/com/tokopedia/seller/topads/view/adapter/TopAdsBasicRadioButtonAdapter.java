package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.other.RadioButtonItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsBasicRadioButtonAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_DATA = 100;

    private List<RadioButtonItem> data;
    private int checkedPosition;

    public void setData(List<RadioButtonItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    public TopAdsBasicRadioButtonAdapter() {
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
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top_ads_basic_checkbox, viewGroup, false));
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
        RadioButtonItem radioButtonItem = data.get(position);
        holder.radioButton.setChecked(position == checkedPosition);
        holder.radioButton.setText(radioButtonItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
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

    public RadioButtonItem getSelectedItem() {
        return data.get(checkedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radioButton;

        public ViewHolder(View view) {
            super(view);
            radioButton = (RadioButton) view.findViewById(R.id.radio_button);
        }
    }
}